package com.ssafy.local.repository;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.local.dto.DongCodeDto;

@Mapper
public interface DongCodeRepository {
	DongCodeDto selectDongCodeByCode(String dongCode) throws Exception;
    DongCodeDto selectDongCodeByName(String name) throws Exception;
    String selectDongCodeByAddress(String sido_name, String gugun_name, String dong_name) throws Exception;
}
