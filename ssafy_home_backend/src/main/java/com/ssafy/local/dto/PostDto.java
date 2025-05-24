package com.ssafy.local.dto;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
	int board_id;
	String user_id;
	String title;
	String category;
	String content;
	String date;
}
