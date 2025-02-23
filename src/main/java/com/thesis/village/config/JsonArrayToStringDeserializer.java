package com.thesis.village.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

/**
 * @author yh
 */
public class JsonArrayToStringDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        // 读取为 List 对象
        List<?> list = mapper.readValue(p, List.class);
        // 将 List 转换为 JSON 字符串
        return mapper.writeValueAsString(list);
    }
}
