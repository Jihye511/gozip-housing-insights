package com.ssafy.local.service;

import com.ssafy.local.dto.AiRequestDto;
import com.ssafy.local.dto.HouseInfoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptPromptService {

    private final ChatClient.Builder chatClientBuilder;

    public String recommendFromList(AiRequestDto request, List<HouseInfoDto> candidates) {
        ChatClient chatClient = chatClientBuilder.build();

        StringBuilder userMessage = new StringBuilder();

        userMessage.append("다음은 ")
                .append(request.getSido()).append(" ")
                .append(request.getGugun()).append(" ")
                .append(request.getDong()).append(" 지역의 아파트 목록입니다.\n")
                .append("이 목록에 있는 아파트 중에서 조건에 맞는 아파트를 4개 추천해주세요.\n")
                .append("[조건]\n")
                .append("- 목적: ").append(request.getPurpose()).append("\n")
                .append("- 예산: ").append(request.getBudget()).append("\n")
                .append("- 평수: ").append(request.getArea()).append("\n")
                .append("- 주거 환경: ")
                .append(request.getEnv() != null ? String.join(", ", request.getEnv()) : "정보 없음").append("\n\n")
                .append("[아파트 목록]\n");

        int idx = 1;
        for (HouseInfoDto apt : candidates) {
            userMessage.append(idx++).append(". ")
                    .append(apt.getApt_nm()).append(" - ")
                    .append(apt.getBuild_year()).append("년 준공, 주소: ")
                    .append(apt.getRoad_nm()).append(" ")
                    .append(apt.getRoad_nm_bonbun());
            if (apt.getRoad_nm_bubun() != null && !apt.getRoad_nm_bubun().isEmpty()) {
                userMessage.append("-").append(apt.getRoad_nm_bubun());
            }
            userMessage.append("\n");
        }

        userMessage.append("\n위 목록 중 조건을 만족하는 4개의 아파트를 추천 사유와 함께 아래 형식으로 작성해주세요.\n")
                    .append("1. [아파트명] - 추천 사유\n")
                    .append("2. ...\n");

        String prompt = "당신은 부동산 추천 전문가입니다. 아래 사용자의 조건에 따라 제공된 아파트 목록 중에서 정확히 4개를 선택해 추천하고 그 이유를 작성하세요. 제공된 목록에 없는 아파트는 절대 추천하지 마세요.";

        log.info("\uD83D\uDCAC GPT에 보낼 프롬프트:\n{}", userMessage);

        var response = chatClient.prompt()
                .system(prompt)
                .user(userMessage.toString())
                .call();

        return response.content();
    }
} 
	