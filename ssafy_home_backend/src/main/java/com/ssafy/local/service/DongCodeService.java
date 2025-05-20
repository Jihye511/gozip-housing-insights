package com.ssafy.local.service;

import com.ssafy.local.dto.DongCodeDto;

public interface DongCodeService {
	
	DongCodeDto selectDongCodeByCode(String dongCode) throws Exception;
    DongCodeDto selectDongCodeByName(String name) throws Exception;
	
}
