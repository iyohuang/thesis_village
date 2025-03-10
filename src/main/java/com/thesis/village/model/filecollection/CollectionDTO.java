package com.thesis.village.model.filecollection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionDTO {
    private Long id;
    private String name;
    private LocalDateTime deadline;
    private LocalDateTime createTime;
    private String status;
    private Long createUserId;
    private List<ParticipantDTO> participants;
}
