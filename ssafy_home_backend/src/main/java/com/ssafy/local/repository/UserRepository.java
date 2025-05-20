package com.ssafy.local.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.local.dto.UserDto;
@Mapper
public interface UserRepository {
	UserDto selectUserByIDPW(String id, String pw) throws Exception;
	UserDto selectUserByIDEmail(String id, String email) throws Exception;
	List<UserDto> selectUserAll() throws Exception;
	int insertUser(UserDto dto) throws Exception;
	UserDto selectUserByID(String id) throws Exception;
	int deleteUser(String id) throws Exception;
	int updateUser(UserDto dto) throws Exception;
	UserDto selectUserByEmail(String email) throws Exception;
}
