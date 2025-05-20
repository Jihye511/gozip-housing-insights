package com.ssafy.local.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
	private int review_id;
	private String apt_id;
	private String user_id;
	private int score;
	private String content;
	private String image_file;
}
