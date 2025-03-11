package com.thesis.village.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.thesis.village.model.email.EmailHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yh
 */
@Mapper
public interface EmailHistoryMapper extends BaseMapper<EmailHistory> {
}
