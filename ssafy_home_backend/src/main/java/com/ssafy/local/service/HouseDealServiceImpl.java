package com.ssafy.local.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.local.dto.HouseDealDto;
import com.ssafy.local.dto.HouseInfoDto;
import com.ssafy.local.repository.HouseDealRepository;
import com.ssafy.local.repository.HouseInfoRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class HouseDealServiceImpl implements HouseDealService{
	private final HouseDealRepository repo;
	private final HouseInfoRepository repoinf;


    @Override
    public int updateHouseDeal(HouseDealDto dto) throws Exception {
    	return repo.updateHouseDeal(dto);
    }

    @Override
    public int insertHouseDeal(HouseDealDto dto) throws Exception {
    	return repo.insertHouseDeal(dto);
    }

    @Override
    public int deleteHouseDeal(String id) throws Exception {
    	return repo.deleteHouseDeal(id);
    }

	@Override
	public List<HouseDealDto> selectHouseDealbyAptSeq(String aptseq) throws Exception {

		return repo.selectHouseDealbyAptSeq(aptseq);
	}

	@Override
	public List<HouseDealDto> selectHouseDealbyDongcode(String dongcode) throws Exception {
		
		List<HouseDealDto> deals = new ArrayList<>();
		List<HouseInfoDto> ls = repoinf.selectHouseInfobyDongCode(dongcode);
		for (HouseInfoDto houseInfoDto : ls) {
			List<HouseDealDto> num = repo.selectHouseDealbyAptSeq(houseInfoDto.getApt_seq());
			deals.addAll(num);
		}
		return deals;
	}


}
