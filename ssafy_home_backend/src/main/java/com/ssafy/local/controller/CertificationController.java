package com.ssafy.local.controller;

import com.ssafy.local.dto.CertificationDto;
import com.ssafy.local.service.CertificationService;
import com.ssafy.local.service.FileStorageService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CertificationController {

	private final CertificationService certificationService;
	private final FileStorageService fileStorageService;

	//
	// ─── 사용자용 API ────────────────────────────────────────────────────────────
	//

	/**
	 * 실거주 인증 요청 생성
	 * 
	 * @throws IOException
	 */
	@PostMapping(value = "/api/certifications", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> requestCertification(Authentication authentication,
			@RequestPart("apt_seq") String aptSeq, @RequestPart("file") MultipartFile file) throws IOException {

		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		String userId = authentication.getName();
		String fileUrl = fileStorageService.store(file);

		CertificationDto dto = CertificationDto.builder().certification_id(null) // DB에서 자동 생성
				.user_id(userId).pdf_file(fileUrl).apt_seq(aptSeq).approval("PENDING").build();

		certificationService.createRequest(dto);
		return ResponseEntity.ok("인증 요청이 정상적으로 전송되었습니다.");
	}

	/** 내 인증 상태 조회 */
	@GetMapping("/api/certifications/status")
	public ResponseEntity<CertificationDto> getStatus(Authentication authentication, // ← java.security.Principal 대신
			@RequestParam String aptSeq) {

		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		// authentication.getName() 에는 JWTUtil 에서 setAuthentication 할 때
		// 넣은 username(user_id)이 들어옵니다.
		String userId = authentication.getName();
		CertificationDto dto = certificationService.getByUserAndApt(userId, aptSeq);
		if (dto == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto);
	}

	//
	// ─── 관리자용 API ───────────────────────────────────────────────────────────
	//

	/** 전체 인증 요청 조회 */
	@GetMapping("/api/admin/certifications")
	public ResponseEntity<List<CertificationDto>> listAll() {
		List<CertificationDto> list = certificationService.getAllRequests();
		return ResponseEntity.ok(list);
	}

	/** 특정 요청 승인 */
	@PostMapping("/api/admin/certifications/{id}/approve")
	public ResponseEntity<Void> approve(@PathVariable String id) {
		certificationService.approveRequest(id);
		return ResponseEntity.ok().build();
	}

	/** 특정 요청 거부 */
	@PostMapping("/api/admin/certifications/{id}/reject")
	public ResponseEntity<Void> reject(@PathVariable String id) {
		certificationService.rejectRequest(id);
		return ResponseEntity.ok().build();
	}
}
