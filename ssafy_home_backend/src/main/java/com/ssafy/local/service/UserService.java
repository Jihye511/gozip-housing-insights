package com.ssafy.local.service;

import java.util.List;

import com.ssafy.local.dto.UserDto;

public interface UserService {
	UserDto selectUserByIDPW(String id, String pw) throws Exception;
	UserDto selectUserByIDEmail(String id, String email) throws Exception;
	List<UserDto> selectUserAll() throws Exception;
	int insertUser(UserDto dto) throws Exception;
	UserDto selectUserByID(String id) throws Exception;
	int deleteUser(String id) throws Exception;
	boolean isUserIdExists(String userId) throws Exception;
	boolean isEmailExists(String email) throws Exception;
	UserDto selectUserByEmail(String email) throws Exception;
}
