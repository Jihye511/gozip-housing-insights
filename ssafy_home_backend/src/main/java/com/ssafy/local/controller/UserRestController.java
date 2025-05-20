package com.ssafy.local.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ssafy.local.dto.UserDto;
import com.ssafy.local.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserRestController {

	private final UserService userService;

	// Read (My Page Info)
	@GetMapping("/mypage")
	private ResponseEntity<?> mypage(@SessionAttribute(name = "loginUser", required = false) UserDto sessionUser) {
		// 세션에 로그인 정보가 없으면 401 Unauthorized 반환
		if (sessionUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
		}
		try {
			// 세션의 사용자 ID로 DB에서 최신 사용자 정보 조회
			UserDto userFromDb = userService.selectUserByID(sessionUser.getUser_id());
			if (userFromDb != null) {
				// 사용자 정보 반환 (비밀번호 등 민감 정보 제외 필요 시 DTO 분리 고려)
				return ResponseEntity.ok(userFromDb);
			} else {
				// DB에 해당 사용자가 없으면 404 Not Found 반환
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
			}
		} catch (Exception e) {
			log.error("Error fetching user data for ID: {}", sessionUser.getUser_id(), e);
			// 서버 내부 오류 발생 시 500 Internal Server Error 반환
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching user data.");
		}
	}

	// Update (User Info)
	@PutMapping("/update")
	private ResponseEntity<?> update(@SessionAttribute(name = "loginUser", required = false) UserDto sessionUser,
			@RequestBody UserDto updateRequest) { // 요청 본문에서 수정할 사용자 정보 받음
		// 세션 확인
		if (sessionUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
		}
		// 요청된 사용자 ID와 세션의 사용자 ID가 다르면 수정 권한 없음 (403 Forbidden)
		// URL 경로 변수로 ID를 받는 대신, 세션 사용자의 정보만 수정 가능하도록 강제
		if (!sessionUser.getUser_id().equals(updateRequest.getUser_id())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot update another user's profile.");
		}

		Map<String, String> response = new HashMap<>();
		try {
			// 이메일 중복 확인 (요청된 이메일이 있고, 기존 이메일과 다르며, 이미 존재하는 이메일인 경우)
			if (updateRequest.getEmail() != null && !updateRequest.getEmail().equals(sessionUser.getEmail())
					&& userService.isEmailExists(updateRequest.getEmail())) {
				response.put("error", "Email already in use.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			// DB에서 현재 사용자 정보 조회
			UserDto userToUpdate = userService.selectUserByID(sessionUser.getUser_id());
			if (userToUpdate == null) {
				// 이론상 세션이 있으면 사용자는 존재해야 하지만, 방어 로직 추가
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
			}

			// 요청된 필드만 업데이트 (null이 아닌 경우)
			if (updateRequest.getName() != null)
				userToUpdate.setName(updateRequest.getName());
			if (updateRequest.getPhone() != null)
				userToUpdate.setPhone(updateRequest.getPhone());
			if (updateRequest.getEmail() != null)
				userToUpdate.setEmail(updateRequest.getEmail());
			// 비밀번호 필드가 요청에 포함되고 비어있지 않으면 업데이트, 아니면 null 처리 (업데이트 안 함)
			if (updateRequest.getPw() != null && !updateRequest.getPw().trim().isEmpty()) {
				userToUpdate.setPw(updateRequest.getPw()); // 서비스 레이어에서 암호화 필요
			} else {
				// 비밀번호 필드가 없거나 비어있으면 기존 비밀번호 유지 (null 설정 시 기존 비밀번호 유지 로직 필요)
				// 여기서는 DTO의 pw를 null로 설정하여 update 쿼리에서 제외되도록 함 (MyBatis if test 등 활용)
				userToUpdate.setPw(null);
			}

			// 사용자 정보 업데이트 실행
			int result = userService.updateUser(userToUpdate);
			if (result > 0) {
				// 성공 응답
				response.put("message", "User information updated successfully.");
				// REST 이므로 session update code 적용 안함
				// session.setAttribute("loginUser", userToUpdate); // 상태 유지 시 필요
				return ResponseEntity.ok(response);
			} else {
				// 업데이트 실패 응답
				response.put("error", "Failed to update user information.");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}

		} catch (Exception e) {
			log.error("Error updating user ID: {}", sessionUser.getUser_id(), e);
			response.put("error", "An error occurred during update.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	// Delete (User Account)
	@DeleteMapping("/delete")
	public ResponseEntity<?> delete(@SessionAttribute(name = "loginUser", required = false) UserDto sessionUser,
			HttpSession session) { // TODO: Remove HttpSession
		// 세션 확인
		if (sessionUser == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
		}

		Map<String, String> response = new HashMap<>();
		try {
			// 사용자 삭제 실행
			int result = userService.deleteUser(sessionUser.getUser_id());
			if (result > 0) {
				// TODO: session 방식을 사용하지 않는다면 삭제 필요
				session.invalidate();
				response.put("message", "User deleted successfully.");
				return ResponseEntity.ok(response);
			} else {
				// 삭제 실패 응답
				response.put("error", "Failed to delete user.");
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
			}
		} catch (Exception e) {
			log.error("Error deleting user ID: {}", sessionUser.getUser_id(), e);
			response.put("error", "An error occurred during deletion.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	// Create (Sign Up)
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody UserDto userDto) { // 요청 본문에서 사용자 정보 받음
		Map<String, String> response = new HashMap<>();
		try {
			// 아이디 중복 확인
			if (userService.isUserIdExists(userDto.getUser_id())) {
				response.put("error", "User ID already exists.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			// 이메일 중복 확인
			if (userService.isEmailExists(userDto.getEmail())) {
				response.put("error", "Email already exists.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}

			// 사용자 등록 실행 (서비스 레이어에서 비밀번호 암호화 필요)
			userService.insertUser(userDto);
			// 회원가입 성공 시 자동 로그인 처리 안 함. 생성 확인 메시지만 반환.
			response.put("message", "User registered successfully.");
			return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created 상태 코드 반환

		} catch (Exception e) {
			log.error("Error during user signup for ID: {}", userDto.getUser_id(), e);
			response.put("error", "An error occurred during registration.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
}
