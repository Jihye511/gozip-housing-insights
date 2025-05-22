package com.ssafy.local.service;

import com.ssafy.local.dto.AiRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptPromptService {

    private final ChatClient.Builder chatClientBuilder;

    public String getGptResponse(AiRequestDto request) {
        try {
            ChatClient chatClient = chatClientBuilder.build();

            // 프롬프트 구성
            StringBuilder userMessage = new StringBuilder();
            userMessage.append("지역: ")
                .append(request.getSido()).append(" ")
                .append(request.getGugun()).append(" ")
                .append(request.getDong()).append("\n");

            userMessage.append("주거 환경: ")
                .append(request.getEnv() != null ? String.join(", ", request.getEnv()) : "정보 없음").append("\n");

            userMessage.append("목적: ")
                .append(request.getPurpose() != null ? String.join(", ", request.getPurpose()) : "정보 없음").append("\n");

            userMessage.append("예산: ")
                .append(request.getBudget() != null ? request.getBudget() : "정보 없음").append("\n");

            userMessage.append("평수: ")
                .append(request.getArea() != null ? request.getArea() : "정보 없음").append("\n");

            String prompt = """
            		당신은 부동산 추천 전문가입니다.
            		아래 조건에 맞는 아파트를 **정확히 4개** 추천해 주세요.

            		각 아파트는 다음 형식으로 출력해 주세요:
            		1. 아파트 이름
            		2. 추천 사유

            		결과는 다음과 같은 형식을 따라 주세요:

            		1. [아파트명1] - 추천 사유1
            		2. [아파트명2] - 추천 사유2
            		3. [아파트명3] - 추천 사유3
            		4. [아파트명4] - 추천 사유4

            		추가 설명 없이 위 형식으로만 출력해 주세요.
            		""";

           System.out.println(prompt);
            var response = chatClient.prompt()
                .system(prompt)
                .user(userMessage.toString() + "\n위 조건을 반영하여 아파트를 추천해 주세요. 추천 사유도 함께 작성해 주세요.")
                .call();

            return response.content();

        } catch (Exception ex) {
            log.error("GPT 추천 요청 중 오류 발생", ex);
            return "GPT 추천 중 오류가 발생했습니다.";
        }
    }
}
