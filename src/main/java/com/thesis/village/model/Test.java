package com.thesis.village.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yh
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Test {
    private String name;
    private String password;
}
