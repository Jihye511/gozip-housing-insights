package com.ssafy.local.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HouseDealDto {
	String no;
    String apt_seq;
    String apt_dong;
    String floor;
    String deal_year;
    String deal_month;
    String deal_day;
    String exclu_use_ar;
    String deal_amount;
}
