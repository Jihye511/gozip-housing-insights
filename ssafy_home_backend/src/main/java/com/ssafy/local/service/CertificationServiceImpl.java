package com.ssafy.local.service;

import com.ssafy.local.dto.CertificationDto;
import com.ssafy.local.repository.CertificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CertificationServiceImpl implements CertificationService {
	private final CertificationRepository repository;

	@Override
	public List<CertificationDto> getAllRequests() {
		return repository.selectAllRequests();
	}

	@Override
	public void approveRequest(String certificationId) {
		repository.updateApproval(certificationId, "APPROVED");
	}

	@Override
	public void rejectRequest(String certificationId) {
		repository.updateApproval(certificationId, "REJECTED");
	}

	@Override
	public void createRequest(CertificationDto dto) {
		repository.insertRequest(dto);
	}

	@Override
	public CertificationDto getByUserAndApt(String userId, String aptSeq) {
		return repository.selectByUserAndApt(userId, aptSeq);
	}
}