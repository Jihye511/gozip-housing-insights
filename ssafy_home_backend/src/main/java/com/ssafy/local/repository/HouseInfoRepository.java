package com.ssafy.local.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.local.dto.HouseInfoDto;

@Mapper
public interface HouseInfoRepository {
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
    
	List<HouseInfoDto> selectHouseInfobyDongCode(String dongCode) throws Exception;
	List<HouseInfoDto> selectHouseInfoByAptName(String aptName)throws Exception;
	HouseInfoDto selectHouseInfoByAptSeq(String aptSeq);	
	List<String> findSeqByNames(@Param("aptNames") List<String> aptNames);
}
