package com.ssafy.local.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.local.dto.AvgPriceDto;
import com.ssafy.local.dto.HouseDealDto;


@Mapper
public interface HouseDealRepository {
	List<HouseDealDto> selectHouseDealbyAptSeq(String aptseq) throws Exception;
    int updateHouseDeal(HouseDealDto dto)throws Exception;
    int insertHouseDeal(HouseDealDto dto)throws Exception;
    int deleteHouseDeal(String id)throws Exception;
    List<AvgPriceDto> getAvgPriceByArea(String aptSeq);
    List<Map<String, Object>> getYearlyAvgPriceByArea(@Param("aptSeq") String aptSeq,
            @Param("area") String area);

    Double selectMonthlyAvg(
            @Param("aptSeq") String aptSeq,
            @Param("year")   int year,
            @Param("month")  int month
        );
}
