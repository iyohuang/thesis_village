package com.thesis.village.service.impl;

import com.thesis.village.dao.CommentMapper;
import com.thesis.village.dao.UserMapper;
import com.thesis.village.model.auth.User;
import com.thesis.village.model.social.Comment;
import com.thesis.village.model.social.CommentRequest;
import com.thesis.village.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yh
 */
@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String COMMENT_LIKES_KEY_PREFIX = "comment:likes:";
    private static final String COMMENT_LIKED_USERS_KEY_PREFIX = "comment:likedUsers:";

    @Override
    public List<Comment> getCommentsByMomentId(Long momentId, Long currentUserId) {
        List<Comment> topComments = commentMapper.findTopLevelComments(momentId);
        log.info("topComments: {}", topComments);
        for (Comment comment : topComments) {
            setLikeInfo(comment, currentUserId);
            comment.setChildren(getChildComments(comment.getId(), currentUserId));
        }
        return topComments;
    }

    private List<Comment> getChildComments(Long parentId, Long currentUserId) {
        List<Comment> children = commentMapper.findChildComments(parentId);
        for (Comment child : children) {
            setLikeInfo(child, currentUserId);
            child.setChildren(getChildComments(child.getId(), currentUserId));
        }
        return children;
    }

    private void setLikeInfo(Comment comment, Long currentUserId) {
        // 从 StringRedisTemplate 获取点赞数量（存储为字符串）
        String countStr = stringRedisTemplate.opsForValue().get(COMMENT_LIKES_KEY_PREFIX + comment.getId());
        int count = countStr != null ? Integer.parseInt(countStr) : 0;
        comment.setLikesCount(count);

        // 判断当前用户是否点赞，注意转换 userId 为字符串进行比较
        Boolean liked = stringRedisTemplate.opsForSet().isMember(COMMENT_LIKED_USERS_KEY_PREFIX + comment.getId(), currentUserId.toString());
        comment.setLiked(liked != null && liked);
    }

    @Override
    public boolean addComment(CommentRequest request) {
        Comment comment = new Comment();
        comment.setMomentId(request.getMomentId());
        comment.setUserId(request.getUserId());
        // 从 userMapper 获取用户信息（示例）
        User u = userMapper.findById(request.getUserId());
        comment.setUserAvatar(u.getAvatar());
        comment.setUserName(u.getUsername());
        comment.setContent(request.getContent());
        comment.setParentCommentId(request.getParentCommentId());
        if (request.getParentCommentId() != null) {
            // 查询父评论获取用户名（示例代码）
            Comment parentComment = commentMapper.findByParentId(request.getParentCommentId());
            comment.setParentUserName(parentComment.getUserName());
        }
        int result = commentMapper.insertComment(comment);
        return result > 0;
    }

    @Override
    public boolean toggleLike(Long commentId, Long userId) {
        String likeKey = COMMENT_LIKES_KEY_PREFIX + commentId;
        String likedUsersKey = COMMENT_LIKED_USERS_KEY_PREFIX + commentId;
        // 注意：userId 转换为字符串
        Boolean isMember = stringRedisTemplate.opsForSet().isMember(likedUsersKey, userId.toString());
        if (Boolean.TRUE.equals(isMember)) {
            // 取消点赞：移除用户 ID 并减少点赞数
            stringRedisTemplate.opsForSet().remove(likedUsersKey, userId.toString());
            Long newCount = stringRedisTemplate.opsForValue().decrement(likeKey, 1);
            stringRedisTemplate.expire(likeKey, 30, TimeUnit.DAYS);
            stringRedisTemplate.expire(likedUsersKey, 30, TimeUnit.DAYS);
            return newCount != null;
        } else {
            // 点赞：添加用户 ID 并增加点赞数
            stringRedisTemplate.opsForSet().add(likedUsersKey, userId.toString());
            Long newCount = stringRedisTemplate.opsForValue().increment(likeKey, 1);
            stringRedisTemplate.expire(likeKey, 30, TimeUnit.DAYS);
            stringRedisTemplate.expire(likedUsersKey, 30, TimeUnit.DAYS);
            return newCount != null;
        }
    }
}
