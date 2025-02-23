package com.thesis.village.controller;

import com.thesis.village.model.ResponseResult;
import com.thesis.village.model.social.Comment;
import com.thesis.village.model.social.CommentRequest;
import com.thesis.village.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yh
 */
@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/moment/{momentId}")
    public ResponseResult<List<Comment>> getComments(@PathVariable Long momentId, @RequestParam("userId") Long userId) {
        List<Comment> comments = commentService.getCommentsByMomentId(momentId, userId);
        return ResponseResult.success(comments);
    }

    @PostMapping("/add")
    public ResponseResult<?> addComment(@RequestBody CommentRequest request) {
        boolean success = commentService.addComment(request);
        return success ? ResponseResult.success("评论成功") : ResponseResult.fail("评论失败");
    }

    @PostMapping("/{commentId}/toggleLike")
    public ResponseResult<?> toggleLike(@PathVariable Long commentId, @RequestParam("userId") Long userId) {
        boolean success = commentService.toggleLike(commentId, userId);
        return success ? ResponseResult.success("点赞成功") : ResponseResult.fail("点赞失败");
    }
}
