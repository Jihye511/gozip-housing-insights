package com.ssafy.local.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HouseInfoDto {
    String apt_seq;
    String sgg_cd;
    String umd_cd;
    String umd_nm;
    String jibun;
    String road_nm_sgg_cd;
    String road_nm;
    String road_nm_bonbun;
    String road_nm_bubun;
    String apt_nm;
    String build_year;
    String latitude;
    String longitude;
}
