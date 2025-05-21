package com.ssafy.local.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.local.dto.UserDto;
@Mapper
public interface UserRepository {
	UserDto selectUserByIDPW(String id, String pw);
	UserDto selectUserByIDEmail(String id, String email);
	List<UserDto> selectUserAll();
	int insertUser(UserDto dto);
	UserDto selectUserByID(String id);
	int deleteUser(String id);
	int updateUser(UserDto dto);
	UserDto selectUserByEmail(String email);
}
