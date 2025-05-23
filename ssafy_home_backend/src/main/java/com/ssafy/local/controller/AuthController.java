package com.ssafy.local.controller;

import com.ssafy.local.dto.UserDto;
import com.ssafy.local.jwt.JWTUtil;
import com.ssafy.local.service.UserService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService      userService;
    private final PasswordEncoder  passwordEncoder;
    private final JWTUtil          jwtUtil;

    // 1) 회원가입
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> register(@RequestBody UserDto dto) {
        try {
            // 중복 확인
            if (userService.isUserIdExists(dto.getUser_id())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                     .body("이미 사용 중인 아이디입니다.");
            }
            String raw = dto.getPw();
            String enc = passwordEncoder.encode(raw);
            System.out.println("🛠 rawPw = " + raw + "  ->  encPw = " + enc);

            dto.setPw(enc);
            userService.insertUser(dto);
            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("회원가입 실패: " + e.getMessage());
        }

    }

    // 2) 로그인
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(
            @RequestBody LoginRequest req,
            HttpServletResponse response
    ) {
        try {
            // DB에서 user_id로 조회
            UserDto user = userService.selectUserByID(req.getUsername());
            if (user == null ||
                !passwordEncoder.matches(req.getPassword(), user.getPw())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body("아이디 또는 비밀번호가 틀렸습니다.");
            }
            // JWT 생성 (24h 예시)
            long expMs = 24L * 60 * 60 * 1000;
            String token = jwtUtil.createJwt(
                user.getUser_id(),
                user.getRole(),
                expMs
            );
            // 쿠키에 담기
            Cookie c = new Cookie("Authorization", token);
            c.setHttpOnly(true);
            c.setMaxAge((int)(expMs/1000));
            c.setPath("/");
            response.addCookie(c);

            return ResponseEntity.ok("로그인 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("로그인 실패: " + e.getMessage());
        }
    }

    @Data
    static class LoginRequest {
        private String username;
        private String password;
    }
}
