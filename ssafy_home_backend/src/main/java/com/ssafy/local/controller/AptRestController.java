package com.ssafy.local.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.local.dto.AvgPriceDto;
import com.ssafy.local.dto.HouseDealDto;
import com.ssafy.local.dto.HouseInfoDto;
import com.ssafy.local.service.HouseDealService;
import com.ssafy.local.service.HouseInfoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/apt")
@RequiredArgsConstructor
@Slf4j
public class AptRestController {

    private final HouseDealService dealService;
    private final HouseInfoService infoService;

    @GetMapping("/{aptSeq}/deals")
    private ResponseEntity<?> getDealsByAptSeq(@PathVariable String aptSeq) {
        try {
            List<HouseDealDto> deals = dealService.selectHouseDealbyAptSeq(aptSeq);
            if (deals == null || deals.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "No deals found for this apartment sequence."));
            }
            return ResponseEntity.ok(deals); // Return list of deals
        } catch (Exception e) {
            log.error("Error fetching deals for aptSeq: {}", aptSeq, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching apartment deals."));
        }
    }
    
    @GetMapping("/{aptSeq}/avg-prices")
    private ResponseEntity<?> getAvgPriceByArea(@PathVariable String aptSeq) {
        try {
            List<AvgPriceDto> avgPrices = dealService.getAvgPriceByArea(aptSeq);
            if (avgPrices == null || avgPrices.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "No average price data found for this apartment."));
            }
            return ResponseEntity.ok(avgPrices);
        } catch (Exception e) {
            log.error("Error fetching average prices for aptSeq: {}", aptSeq, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching average prices."));
        }
    }
    

    @GetMapping("/search")
    private ResponseEntity<?> searchApt(@RequestParam(required = false) String aptName,
            @RequestParam(required = false) String dongCode) { // Changed 'dong' to 'dongCode' for clarity
        try {
            List<HouseInfoDto> results;

            // 현재는 apt 이름, dongcode 순으로 우선순위를 가지고 있지만, 두 값 모두 동시에 사용해 검색으로 확장 가능?
            if (aptName != null && !aptName.trim().isEmpty()) {
                log.info("Searching apartments by name: {}", aptName);
                results = infoService.selectHouseInfoByAptName(aptName);
            } else if (dongCode != null && !dongCode.trim().isEmpty()) {
                log.info("Searching apartments by dong code: {}", dongCode);
                results = infoService.selectHouseInfobyDongCode(dongCode);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Please provide aptName or dongCode for search."));
            }

            return ResponseEntity.ok(results);

        } catch (Exception e) {
            log.error("Error searching apartments (aptName: {}, dongCode: {})", aptName, dongCode, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error searching apartments."));
        }
    }
}