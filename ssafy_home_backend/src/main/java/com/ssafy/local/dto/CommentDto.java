package com.ssafy.local.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
	int board_id;
	int comment_id;
	String user_id;
	String title;
	String category;
	String content;
//	String date;
}
