package com.ssafy.local.service;

import org.springframework.stereotype.Service;

import com.ssafy.local.dto.DongCodeDto;
import com.ssafy.local.repository.DongCodeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DongCodeServiceImpl implements DongCodeService{
	
	private final DongCodeRepository repo;

	@Override
	public DongCodeDto selectDongCodeByCode(String dongCode) throws Exception {
		return repo.selectDongCodeByCode(dongCode);
	}

	@Override
	public DongCodeDto selectDongCodeByName(String name) throws Exception {
		return repo.selectDongCodeByName(name);
	}


}
