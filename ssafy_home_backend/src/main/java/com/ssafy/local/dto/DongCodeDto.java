package com.ssafy.local.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DongCodeDto {
    String dong_code;
    String sido_name;
    String gugun_name;
    String dong_name;
}
