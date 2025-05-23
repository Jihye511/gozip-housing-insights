package com.ssafy.local.dto;

import lombok.Data;
import java.util.List;
@Data
public class AiRequestDto {
    private String sido;
    private String gugun;
    private String dong;
    private String dongCode; // 🔥 이거 추가
    private List<String> env; // 주거 환경
    private String purpose;   // 목적
    private String budget;    // 예산
    private String area;      // 평수
}
