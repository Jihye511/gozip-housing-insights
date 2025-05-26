package com.ssafy.local.dto;
import lombok.Builder;

import java.time.LocalDateTime;

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
	String username;
	String title;
	String category;
	String content;
	LocalDateTime created_at;
}
