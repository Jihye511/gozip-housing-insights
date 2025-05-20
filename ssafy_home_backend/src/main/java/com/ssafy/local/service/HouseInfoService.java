package com.ssafy.local.service;

import java.util.List;

import com.ssafy.local.dto.HouseInfoDto;

// 주택정보 houseInfo
public interface HouseInfoService {
    // 주택 select (아이디)
    HouseInfoDto selectHouseInfoByID(String id)throws Exception;
    // 주택 select (이름)
    HouseInfoDto selectHouseInfoByName(String name)throws Exception;
    // 주택 insert (dto)
    int insertHouseInfo(HouseInfoDto dto) throws Exception;
    // 주택 update (Dto)
	int updateHouseInfo(HouseInfoDto dto) throws Exception;
    // 주택 delete (id)
	int deleteHouseInfo(String id) throws Exception;
	public List<HouseInfoDto> selectHouseInfobyDongCode(String dongCode) throws Exception;
    // 시군구로 주택 정보찾기
	List<HouseInfoDto> selectHouseInfoByAddress(String sido_name, String gugun_name, String dong_name) throws Exception;
	List<HouseInfoDto> selectHouseInfoByAptName(String aptName) throws Exception;
}

