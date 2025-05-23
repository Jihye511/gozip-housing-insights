package com.ssafy.local.service;

import com.ssafy.local.dto.CertificationDto;
import java.util.List;

public interface CertificationService {
	// ─── 관리자용 메서드 ───────────────────

	// 모든 인증 요청을 조회
	List<CertificationDto> getAllRequests();

	// 인증 요청 승인
	void approveRequest(String certificationId);

	// 인증 요청 거부
	void rejectRequest(String certificationId);

	// ─── 사용자용 메서드 ───────────────────

	// 새로운 인증 요청
	void createRequest(CertificationDto dto);

	// 인증 사용자 조회
	CertificationDto getByUserAndApt(String userId, String aptSeq);

}