package com.ssafy.local.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.local.dto.HouseInfoDto;
import com.ssafy.local.repository.DongCodeRepository;
import com.ssafy.local.repository.HouseInfoRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class HouseInfoServiceImpl implements HouseInfoService {
	private final HouseInfoRepository repo;
	private final DongCodeRepository repo_dong;

    @Override
    public HouseInfoDto selectHouseInfoByID(String id) throws Exception {
        // TODO Auto-generated method stub
    	return repo.selectHouseInfoByID(id);
    }

    @Override
    public HouseInfoDto selectHouseInfoByName(String name) throws Exception {
        // TODO Auto-generated method stub
        return repo.selectHouseInfoByName(name);
    }

    @Override
    public int insertHouseInfo(HouseInfoDto dto) throws Exception {
        // TODO Auto-generated method stub
    	return repo.insertHouseInfo(dto);
    }

    @Override
    public int updateHouseInfo(HouseInfoDto dto) throws Exception {
        // TODO Auto-generated method stub
    	return repo.updateHouseInfo(dto);
    }

    @Override
    public int deleteHouseInfo(String id) throws Exception {
        // TODO Auto-generated method stub
    	return repo.deleteHouseInfo(id);
    }
	@Override
	public List<HouseInfoDto> selectHouseInfoByAddress(String sido_name, String gugun_name, String dong_name)throws Exception {
		String dongcode = repo_dong.selectDongCodeByAddress(sido_name, gugun_name, dong_name);
		return repo.selectHouseInfobyDongCode(dongcode);
	}
	@Override
	public List<HouseInfoDto> selectHouseInfobyDongCode(String dongCode) throws Exception {
		// TODO Auto-generated method stub
		return repo.selectHouseInfobyDongCode(dongCode);
	}
	@Override
	public List<HouseInfoDto> selectHouseInfoByAptName(String aptName) throws Exception {
		// TODO Auto-generated method stub
		return repo.selectHouseInfoByAptName(aptName);
	}
}
