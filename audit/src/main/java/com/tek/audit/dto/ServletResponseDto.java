package com.tek.audit.dto;

import lombok.*;

import java.util.HashMap;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ServletResponseDto {

    private Integer status;
    private Long duration;
}
