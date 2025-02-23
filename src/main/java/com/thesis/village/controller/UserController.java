package com.thesis.village.controller;

import com.thesis.village.model.ResponseResult;
import com.thesis.village.model.auth.User;
import com.thesis.village.model.user.PasswordUpdateRequest;
import com.thesis.village.model.user.UserUpdateRequest;
import com.thesis.village.service.UserService;
import com.thesis.village.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.thesis.village.enums.HttpStatusEnum.BAD_REQUEST;

/**
 * @author yh
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;  // 用户服务层
    @GetMapping("/userInfo")
    public ResponseResult<User> userInfo() {
        log.info("获取用户信息");
        //根据用户名查询用户
        Map<String, Object> map = ThreadLocalUtil.get();
        Long id = ((Number) map.get("id")).longValue();
        User user = userService.getUserById(id);
        return ResponseResult.success(user);
    }
    @PutMapping("/userInfo/{id}")
    public ResponseResult<?> updateUserProfile(@PathVariable Long id,
                                               @RequestBody UserUpdateRequest updateRequest) {
        try {
            boolean updated = userService.updateUserProfile(id, updateRequest);
            return updated ? ResponseResult.success("个人信息更新成功")
                    : ResponseResult.fail("更新失败");
        }catch (DuplicateKeyException e) {
            return ResponseResult.fail(400, "用户名|手机号|邮箱已存在", null);
        } catch (Exception e) {
            return ResponseResult.fail("服务器异常");
        }
    }

    @PutMapping("/updatePassword")
    public ResponseResult<?> updatePassword(@RequestBody PasswordUpdateRequest request) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Long id = ((Number) map.get("id")).longValue();
        request.setId(id);
        boolean updated = userService.updatePassword(request);
        if (updated) {
            return ResponseResult.success("密码修改成功");
        } else {
            return ResponseResult.fail("密码修改失败，请检查旧密码是否正确");
        }
    }
}
