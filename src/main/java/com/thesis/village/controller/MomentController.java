package com.thesis.village.controller;

import com.thesis.village.model.ResponseResult;
import com.thesis.village.model.auth.User;
import com.thesis.village.model.social.Moment;
import com.thesis.village.service.MomentService;
import com.thesis.village.service.UserService;
import com.thesis.village.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author yh
 */
@RestController
@Slf4j
@RequestMapping("/moments")
public class MomentController {

    @Autowired
    private MomentService momentService;

    @Autowired
    private UserService userService;
    
    @PostMapping("/create")
    public ResponseResult<?> createMoment(@RequestBody Moment moment) {
        log.info("createMoment: {}", moment);
        Map<String, Object> map = ThreadLocalUtil.get();
        Long id = ((Number) map.get("id")).longValue();
        User user = userService.getUserById(id);
        moment.setUserId(user.getId());
        moment.setUserName(user.getUsername());
        moment.setUserAvatar(user.getAvatar());
        
        momentService.createMoment(moment);
        return ResponseResult.success("发布成功");
    }

    @GetMapping("/list")
    public ResponseResult<List<Moment>> getMoments() {
        List<Moment> list = momentService.getMoments();
        return ResponseResult.success(list);
    }
    
    //删除
    @DeleteMapping("/delete/{id}")
    public ResponseResult<?> deleteMoment(@PathVariable Long id) {
        momentService.deleteMomentById(id);
        return ResponseResult.success("删除成功");
    }
    
    @PostMapping("/{momentid}/toggleLike")
    public ResponseResult<?> toggleLike(@PathVariable Long momentid,@RequestParam("userId") Long userId) {
        boolean success = momentService.toggleLike(momentid,userId);
        return success ? ResponseResult.success("点赞成功") : ResponseResult.fail("点赞失败");
    }
    
}
