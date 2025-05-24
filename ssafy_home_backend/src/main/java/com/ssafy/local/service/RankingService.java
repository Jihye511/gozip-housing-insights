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

    public List<RankingDto> computeRectangleRanking(double swLat, double swLng, double neLat, double neLng) {
        // 1) 범위 내 아파트 목록
        List<HouseInfoDto> apartments = infoRepo.selectByBounds(swLat, swLng, neLat, neLng);

        // 2) 월별 증감률 계산
        YearMonth now  = YearMonth.now();
        YearMonth prev = now.minusMonths(1);

        return apartments.stream()
            .map(apt -> {
                Double thisAvg = dealRepo.selectMonthlyAvg(
                    apt.getApt_seq(), now.getYear(), now.getMonthValue());
                Double prevAvg = dealRepo.selectMonthlyAvg(
                    apt.getApt_seq(), prev.getYear(), prev.getMonthValue());

                double rateChange = 0;
                if (thisAvg != null && prevAvg != null && prevAvg > 0) {
                    rateChange = (thisAvg - prevAvg) / prevAvg * 100;
                }

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
            .sorted(Comparator.comparing(RankingDto::getRateChange).reversed())
            .collect(Collectors.toList());
    }
}
