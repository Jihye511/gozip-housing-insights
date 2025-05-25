package com.ssafy.local.service;

import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ssafy.local.dto.HouseInfoDto;
import com.ssafy.local.dto.RankingDto;
import com.ssafy.local.repository.HouseDealRepository;
import com.ssafy.local.repository.HouseInfoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final HouseInfoRepository infoRepo;
    private final HouseDealRepository dealRepo;

    /**
     * swLat, swLng, neLat, neLng: 지도 바운드
     * fromYear, toYear: 비교할 시작 연도와 종료 연도
     */
    public List<RankingDto> computeRectangleRanking(
            double swLat, double swLng,
            double neLat, double neLng,
            int fromYear, int toYear) {

        // 1) 범위 내 아파트
        List<HouseInfoDto> apartments =
            infoRepo.selectByBounds(swLat, swLng, neLat, neLng);

        return apartments.stream()
            // 2) 각 아파트에 대해 연평균을 가져와 증감률 계산
            .map(apt -> {
                Double avgFrom = dealRepo.selectYearlyAvg(apt.getApt_seq(), fromYear);
                Double avgTo   = dealRepo.selectYearlyAvg(apt.getApt_seq(), toYear);

                // 데이터 없거나 0 이면 랭킹에서 제외
                if (avgFrom == null || avgTo == null || avgFrom <= 0) {
                    return null;
                }

                double rateChange = (avgTo - avgFrom) / avgFrom * 100.0;

                // 기존대로 inline 주소 조합
                String address = apt.getRoad_nm()
                    + (apt.getRoad_nm_bonbun() != null ? " " + apt.getRoad_nm_bonbun() : "")
                    + (apt.getRoad_nm_bubun() != null && !"0".equals(apt.getRoad_nm_bubun())
                       ? "-" + apt.getRoad_nm_bubun() : "");

                return new RankingDto(
                    apt.getApt_seq(),
                    apt.getApt_nm(),
                    address,
                    rateChange,
                    apt.getLatitude(),
                    apt.getLongitude()
                );
            })
            // 3) null 제거(filter)
            .filter(dto -> dto != null)
            // 4) 증감률 내림차순 정렬
            .sorted(Comparator.comparingDouble(RankingDto::getRateChange).reversed())
            .collect(Collectors.toList());
    }
}
