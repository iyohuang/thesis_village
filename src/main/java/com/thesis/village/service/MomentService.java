package com.thesis.village.service;

import com.thesis.village.model.social.Moment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author yh
 */
public interface MomentService {
    void createMoment(Moment moment);
    List<Moment> getMoments();
    
    String uploadImage(MultipartFile image);
    
    void deleteMomentById(Long momentId);

    boolean toggleLike(Long momentId, Long userId);
}
