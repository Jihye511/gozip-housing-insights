package com.ssafy.local.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.local.dto.UserDto;
import com.ssafy.local.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository repo;
	
	

	@Override
	public UserDto selectUserByIDPW(String id, String pw) throws Exception {
		return repo.selectUserByIDPW(id,pw);
	}

	@Override
	public UserDto selectUserByIDEmail(String id, String email) throws Exception {
		return null;
	}

	@Override
	public List<UserDto> selectUserAll() throws Exception {
		return repo.selectUserAll();
	}

	@Override
	public int insertUser(UserDto dto) throws Exception {
		return repo.insertUser(dto);
	}

	@Override
	public UserDto selectUserByID(String id) throws Exception {
	    return repo.selectUserByID(id);
	}

    @Override
    public int deleteUser(String userId) throws Exception {
        return repo.deleteUser(userId);
    }
	
	@Override
	public boolean isUserIdExists(String id) throws Exception {
	    return repo.selectUserByID(id) != null;
	}

	@Override
	public boolean isEmailExists(String email) throws Exception {
	    return repo.selectUserByEmail(email) != null;
	}

	@Override
	public UserDto selectUserByEmail(String email) throws Exception {
	    return repo.selectUserByEmail(email);
	}

}
