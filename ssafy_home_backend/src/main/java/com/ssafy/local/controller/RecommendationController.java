package com.ssafy.local.controller;

import com.ssafy.local.dto.AiRequestDto;
import com.ssafy.local.dto.HouseInfoDto;
import com.ssafy.local.service.GptPromptService;
import com.ssafy.local.service.HouseInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendationController {

    private final HouseInfoService infoService;
    private final GptPromptService gptPromptService;

    @PostMapping()
    public ResponseEntity<?> recommendUsingDbList(@RequestBody AiRequestDto request) {
        try {
            String dongCode = request.getDongCode();
            log.info("🔥 받은 dongCode: {}", request.getDongCode());

            if (dongCode == null || dongCode.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "dongCode는 필수입니다."));
            }

            // InfoService의 기존 로직 재사용
            List<HouseInfoDto> apartmentList = infoService.selectHouseInfobyDongCode(dongCode);

            if (apartmentList == null || apartmentList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "해당 동의 아파트 정보가 없습니다."));
            }

            // GPT에 전달하여 추천 받기
            String result = gptPromptService.recommendFromList(request, apartmentList);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("AI 추천 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "AI 추천 중 오류가 발생했습니다."));
        }
    }
}
