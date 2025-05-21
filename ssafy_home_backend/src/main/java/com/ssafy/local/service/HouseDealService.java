package com.ssafy.local.service;

import java.util.List;
import java.util.Map;

import com.ssafy.local.dto.AvgPriceDto;
import com.ssafy.local.dto.HouseDealDto;


public interface HouseDealService {
	
	List<HouseDealDto> selectHouseDealbyAptSeq(String aptseq) throws Exception;
    int updateHouseDeal(HouseDealDto dto)throws Exception;
    int insertHouseDeal(HouseDealDto dto)throws Exception;
    int deleteHouseDeal(String id)throws Exception;
    List<HouseDealDto> selectHouseDealbyDongcode(String dongcode) throws Exception;
	List<AvgPriceDto> getAvgPriceByArea(String aptSeq);
}
