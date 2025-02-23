package com.thesis.village.model.aq;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author yh
 */
@Data
public class PageResult<T> implements Serializable {
    private Long total;
    private List<T> records;
    private Integer size;
    private Integer current;
    private Boolean hasPrevious;
    private Boolean hasNext;
    private Integer pages;
    public PageResult(Long total, List<T> records, Integer size,
                      Integer current, boolean hasPrevious, boolean hasNext, Integer pages) {
        this.total = total;
        this.records = records;
        this.size = size;
        this.current = current;
        this.hasPrevious = hasPrevious;
        this.hasNext = hasNext;
        this.pages = pages;
    }
}
