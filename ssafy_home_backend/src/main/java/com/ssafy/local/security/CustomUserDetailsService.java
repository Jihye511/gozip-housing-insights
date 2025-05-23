package com.ssafy.local.security;

import com.ssafy.local.dto.UserDto;
import com.ssafy.local.repository.UserRepository;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto u = userRepo.selectUserByID(username);
        if (u == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return User.builder()
            .username(u.getUser_id())
            .password(u.getPw())    // DB엔 반드시 BCrypt 해시로 저장돼 있어야 함
            .roles(u.getRole().replace("ROLE_",""))
            .build();
    }
}
