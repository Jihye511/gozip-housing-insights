package com.ssafy.local.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.local.dto.AiRequestDto;
import com.ssafy.local.service.GptPromptService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendationController {

    private final GptPromptService gptPromptService;

    @PostMapping
    public String getRecommendation(@RequestBody AiRequestDto request) {
    	System.out.println("🔥 POST /api/recommend 호출됨");
        StringBuilder sb = new StringBuilder();
        sb.append("지역: ")
          .append(request.getSido()).append(" ")
          .append(request.getGugun()).append(" ")
          .append(request.getDong()).append("\n");

        sb.append("주거 환경: ")
          .append(request.getEnv() != null ? String.join(", ", request.getEnv()) : "정보 없음").append("\n");

        sb.append("목적: ")
          .append(request.getPurpose() != null ? String.join(", ", request.getPurpose()) : "정보 없음").append("\n");

        sb.append("예산: ").append(request.getBudget() != null ? request.getBudget() : "정보 없음").append("\n");
        sb.append("평수: ").append(request.getArea() != null ? request.getArea() : "정보 없음").append("\n");

        return gptPromptService.getGptResponse(request);
    }

}
