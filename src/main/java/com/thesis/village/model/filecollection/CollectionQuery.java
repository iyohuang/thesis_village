package com.thesis.village.model.filecollection;

import lombok.Data;

/**
 * @author yh
 */
@Data
public class CollectionQuery {
    private Integer page = 1;
    private Integer pageSize = 10;
    private Long userId;
    private Long createUserId;
}
