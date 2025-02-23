package com.thesis.village.service;

import com.thesis.village.model.social.Comment;
import com.thesis.village.model.social.CommentRequest;

import java.util.List;

/**
 * @author yh
 */
public interface CommentService {
    List<Comment> getCommentsByMomentId(Long momentId,Long currentUserId);
    boolean addComment(CommentRequest request);
    boolean toggleLike(Long commentId, Long userId);
    
    
}
