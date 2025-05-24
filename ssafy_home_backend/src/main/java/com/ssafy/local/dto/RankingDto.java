package com.ssafy.local.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankingDto {
    private String aptSeq;
    private String aptName;
    private String address;
    private double rateChange;   // 전월 대비 증감률(%)
    private String latitude;
    private String longitude;
}
