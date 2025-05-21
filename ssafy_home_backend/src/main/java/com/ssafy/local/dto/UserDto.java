package com.ssafy.local.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
	private String user_id;
	private String pw;
	private String email;
	private String name;
	private String phone;
	private String c_date;
	private String role;
}
