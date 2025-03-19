package com.thesis.village.service.impl;

import com.thesis.village.dao.MomentMapper;
import com.thesis.village.model.social.Moment;
import com.thesis.village.service.MomentService;
import com.thesis.village.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MomentServiceImpl implements MomentService {

    @Autowired
    private MomentMapper momentMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 文件上传目录（配置文件中读取）
    @Value("${file.upload-dir-moment}")
    private String UPLOAD_DIR;

    // 点赞相关的 Redis 键前缀
    private static final String MOMENT_LIKES_KEY_PREFIX = "moment:likes:";
    private static final String MOMENT_LIKED_USERS_KEY_PREFIX = "moment:likedUsers:";

    @Override
    public String uploadImage(MultipartFile image) {
        try {
            String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/momentpics/" + filename; // 返回存储的相对路径
        } catch (IOException e) {
            throw new RuntimeException("图片上传失败", e);
        }
    }

    @Override
    public void createMoment(Moment moment) {
        momentMapper.insertMoment(moment);
    }

    @Override
    public List<Moment> getMoments() {
        List<Moment> moments = momentMapper.findAllMoments();
        Map<String, Object> map = ThreadLocalUtil.get();
        Long currentUserId = ((Number) map.get("id")).longValue();
        for (Moment moment : moments) {
            // 获取并设置动态的点赞数和当前用户是否点赞的信息
            setLikeInfo(moment,currentUserId);
        }
        return moments;
    }

    @Override
    public void deleteMomentById(Long momentId) {
        momentMapper.deleteById(momentId);
    }

    // 设置点赞信息，包括点赞数和当前用户是否点赞
    private void setLikeInfo(Moment moment,Long currentUserId) {
        String likeKey = MOMENT_LIKES_KEY_PREFIX + moment.getId();
        String likedUsersKey = MOMENT_LIKED_USERS_KEY_PREFIX + moment.getId();

        // 获取点赞数量
        String countStr = stringRedisTemplate.opsForValue().get(likeKey);
        int count = countStr != null ? Integer.parseInt(countStr) : 0;
        moment.setLikesCount(count);

        // 获取当前用户是否点赞
        Boolean liked = stringRedisTemplate.opsForSet().isMember(likedUsersKey, currentUserId.toString());
        moment.setLiked(liked != null && liked);
    }

    // 切换动态的点赞状态
    @Override
    public boolean toggleLike(Long momentId, Long userId) {
        String likeKey = MOMENT_LIKES_KEY_PREFIX + momentId;
        String likedUsersKey = MOMENT_LIKED_USERS_KEY_PREFIX + momentId;

        // 判断当前用户是否已点赞
        Boolean isMember = stringRedisTemplate.opsForSet().isMember(likedUsersKey, userId.toString());
        if (Boolean.TRUE.equals(isMember)) {
            // 用户已点赞，执行取消点赞操作
            stringRedisTemplate.opsForSet().remove(likedUsersKey, userId.toString());
            Long newCount = stringRedisTemplate.opsForValue().decrement(likeKey, 1);
            // 设置过期时间，防止 Redis 中的点赞数和用户集合数据长期存在
            stringRedisTemplate.expire(likeKey, 30, TimeUnit.DAYS);
            stringRedisTemplate.expire(likedUsersKey, 30, TimeUnit.DAYS);
            return newCount != null;
        } else {
            // 用户未点赞，执行点赞操作
            stringRedisTemplate.opsForSet().add(likedUsersKey, userId.toString());
            Long newCount = stringRedisTemplate.opsForValue().increment(likeKey, 1);
            // 设置过期时间，防止 Redis 中的点赞数和用户集合数据长期存在
            stringRedisTemplate.expire(likeKey, 30, TimeUnit.DAYS);
            stringRedisTemplate.expire(likedUsersKey, 30, TimeUnit.DAYS);
            return newCount != null;
        }
    }
}
