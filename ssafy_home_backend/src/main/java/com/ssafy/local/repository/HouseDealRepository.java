package com.ssafy.local.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.local.dto.HouseDealDto;


@Mapper
public interface HouseDealRepository {
	List<HouseDealDto> selectHouseDealbyAptSeq(String aptseq) throws Exception;
    int updateHouseDeal(HouseDealDto dto)throws Exception;
    int insertHouseDeal(HouseDealDto dto)throws Exception;
    int deleteHouseDeal(String id)throws Exception;
}
