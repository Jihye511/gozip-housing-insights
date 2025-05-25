package com.ssafy.local.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.ssafy.local.dto.CertificationDto;
import com.ssafy.local.dto.UserDto;
import com.ssafy.local.oauth.CustomOAuth2User;
import com.ssafy.local.service.CertificationService;
import com.ssafy.local.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@Slf4j
public class UserRestController {

    private final UserService userService;
    
    // ✅ [READ] My Page
    @GetMapping("/mypage")
    public ResponseEntity<?> mypage(@AuthenticationPrincipal CustomOAuth2User customUser) {
        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
        }
        System.out.print("사용자 : "+customUser);
        String userId = customUser.getName();
        
        try {
            UserDto userFromDb = userService.selectUserByID(userId);
            if (userFromDb != null) {
                return ResponseEntity.ok(userFromDb);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        } catch (Exception e) {
            log.error("Error fetching user data for ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user data.");
        }
    }

    // ✅ [UPDATE] User Info
    //수정 필요
    @PutMapping("/update")
    public ResponseEntity<?> update(@AuthenticationPrincipal CustomOAuth2User customUser,
                                    @RequestBody UserDto updateRequest) {
        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
        }

        String userId = customUser.getUsername();
        if (!userId.equals(updateRequest.getUser_id())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot update another user's profile.");
        }

        Map<String, String> response = new HashMap<>();
        try {
            if (updateRequest.getEmail() != null &&
                !updateRequest.getEmail().equals(customUser.getAttribute(userId)) &&
                userService.isEmailExists(updateRequest.getEmail())) {
                response.put("error", "Email already in use.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            UserDto userToUpdate = userService.selectUserByID(userId);
            if (userToUpdate == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            if (updateRequest.getName() != null) userToUpdate.setName(updateRequest.getName());
            if (updateRequest.getPhone() != null) userToUpdate.setPhone(updateRequest.getPhone());
            if (updateRequest.getEmail() != null) userToUpdate.setEmail(updateRequest.getEmail());

            if (updateRequest.getPw() != null && !updateRequest.getPw().trim().isEmpty()) {
                userToUpdate.setPw(updateRequest.getPw()); // 암호화 필요
            } else {
                userToUpdate.setPw(null); // null 처리로 update문에서 제외
            }

            int result = userService.updateUser(userToUpdate);
            if (result > 0) {
                response.put("message", "User information updated successfully.");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Failed to update user information.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            log.error("Error updating user ID: {}", userId, e);
            response.put("error", "An error occurred during update.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ✅ [DELETE] User
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@AuthenticationPrincipal CustomOAuth2User customUser) {
        if (customUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
        }

        String userId = customUser.getName();
        Map<String, String> response = new HashMap<>();
        try {
            int result = userService.deleteUser(userId);
            if (result > 0) {
                response.put("message", "User deleted successfully.");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "Failed to delete user.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            log.error("Error deleting user ID: {}", userId, e);
            response.put("error", "An error occurred during deletion.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // ✅ [CREATE] Signup (비로그인 상태)
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDto userDto) {
        Map<String, String> response = new HashMap<>();
        try {
            if (userService.isUserIdExists(userDto.getUser_id())) {
                response.put("error", "User ID already exists.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (userService.isEmailExists(userDto.getEmail())) {
                response.put("error", "Email already exists.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            userService.insertUser(userDto);
            response.put("message", "User registered successfully.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("Error during user signup for ID: {}", userDto.getUser_id(), e);
            response.put("error", "An error occurred during registration.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
        
        
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
    	System.out.println("로그아웃 요청");
        Cookie accessToken = new Cookie("Authorization", null);
        accessToken.setMaxAge(0);
        accessToken.setPath("/");
        accessToken.setHttpOnly(true); // 중요!
        
        Cookie refreshToken = new Cookie("Refresh-Token", null);
        refreshToken.setMaxAge(0);
        refreshToken.setPath("/");
        refreshToken.setHttpOnly(true); // 중요!

        response.addCookie(accessToken);
        response.addCookie(refreshToken);
        
        return ResponseEntity.ok("Logged out");
    }
    
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> listAllUsers() {
    	System.out.println("회원 리스트 호출");
        try {
            List<UserDto> list = userService.selectUserAll();
            System.out.println(list);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            log.error("Error fetching all users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /** 관리자가 특정 회원을 삭제 */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteAnyUser(@PathVariable String userId) {
        try {
            int rows = userService.deleteUser(userId);
            if (rows > 0) {
                return ResponseEntity.ok("User " + userId + " deleted.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        } catch (Exception e) {
            log.error("Error deleting user {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Deletion failed.");
        }
    }
    
    @GetMapping
    public Map<String, Object> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        boolean isAdmin = authentication.getAuthorities().stream()
            .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        return Map.of(
            "userId", authentication.getName(),
            "role", isAdmin ? "ADMIN" : "USER"
        );
    }


    
    
}
