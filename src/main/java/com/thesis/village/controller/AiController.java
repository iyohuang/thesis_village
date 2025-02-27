package com.thesis.village.controller;

import com.google.common.collect.ImmutableMap;
import com.thesis.village.model.ai.ChatRequest;
import com.thesis.village.model.ai.RoleProfile;
import io.github.lnyocly.ai4j.listener.SseListener;
import io.github.lnyocly.ai4j.platform.openai.chat.entity.ChatCompletion;
import io.github.lnyocly.ai4j.platform.openai.chat.entity.ChatCompletionResponse;
import io.github.lnyocly.ai4j.platform.openai.chat.entity.ChatMessage;
import io.github.lnyocly.ai4j.service.IChatService;
import io.github.lnyocly.ai4j.service.PlatformType;
import io.github.lnyocly.ai4j.service.factor.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yh
 */

@RestController
@Slf4j
@RequestMapping("/ai")
public class AiController {
    @Autowired
    private AiService aiService;

    @Autowired
    private Map<String, RoleProfile> roleProfiles;
//    @GetMapping("/chat")
//    public String chat(@RequestParam String question) {
//        IChatService chatService = aiService.getChatService(PlatformType.MOONSHOT);
//        ChatCompletion request = ChatCompletion.builder()
//                .model("moonshot-v1-8k")
//                .message(ChatMessage.withUser(question))
//                .build();
//        try {
//            ChatCompletionResponse response = chatService.chatCompletion(request);
//            return response.getChoices().get(0).getMessage().getContent();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return "";
//    }

    @GetMapping("/roles")
    public List<Map<String, Object>> getRoles() {
        return roleProfiles.values().stream()
                .map(role -> ImmutableMap.<String, Object>builder()
                        .put("id", role.getName().toLowerCase())
                        .put("name", role.getName())
                        .put("model", role.getModel())
                        .put("platform", role.getPlatform().name())
                        .build())
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/chat/{roleId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<StringBuilder> chat(@PathVariable String roleId,@RequestBody ChatRequest request) {
        
        RoleProfile role = roleProfiles.getOrDefault(
                roleId.toLowerCase(),
                roleProfiles.get("default")
        );

//        if (!role.getRateLimiter().tryAcquire()) {
//            return Flux.error(new RateLimitException("请求过于频繁"));
//        }

        // 构造监听器
        SseListener sseListener = new SseListener() {
            @Override
            protected void send() {
                System.out.println(this.getCurrStr());
            }
        };
        // 显示函数参数，默认不显示
        sseListener.setShowToolArgs(true);
        
        try {
             aiService.getChatService(role.getPlatform())
                    .chatCompletionStream(buildRequest(role, request),sseListener);
            return Flux.interval(Duration.ofSeconds(1))
                    .map(sequence -> sseListener.getOutput());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private ChatCompletion buildRequest(RoleProfile role, ChatRequest req) {
        return ChatCompletion.builder()
                .model(role.getModel())
                .temperature(role.getTemperature())
                .maxTokens(role.getMaxTokens())
                .message(ChatMessage.withSystem(role.getSystemPrompt()))
                .messages(req.getMessages().stream()
                        .map(m -> new ChatMessage(m.getRole(), m.getContent()))
                        .collect(Collectors.toList()))
                .build();
    }


    @GetMapping(value = "/chat-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamChat() {
        log.info("yhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        return Flux.interval(Duration.ofMillis(500))
                .map(seq -> ServerSentEvent.<String>builder()
                        .event("message") // 事件类型
                        .id(String.valueOf(seq))
                        .data("AI回复内容-" + seq + " @ " + LocalDateTime.now())
                        .build());
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public void test(@RequestParam String q, HttpServletResponse response)throws Exception {
        response.setContentType("text/event-stream; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        // 中文乱码问题
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();

        IChatService chatService = aiService.getChatService(PlatformType.MOONSHOT);
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model("moonshot-v1-8k")
                .message(ChatMessage.withUser(q))
                .build();
        SseListener sseListener = new SseListener() {
            @Override
            protected void send() {
                writer.write("data: "+this.getCurrStr()+ "\n\n");
                writer.flush();
                System.out.println(this.getCurrStr());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        chatService.chatCompletionStream(chatCompletion, sseListener);
        writer.close();
        System.out.println(sseListener.getOutput());
    }
}
