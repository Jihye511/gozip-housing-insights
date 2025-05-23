package com.ssafy.local.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.ssafy.local.jwt.CustomSuccessHandler;
import com.ssafy.local.jwt.JWTFilter;
import com.ssafy.local.jwt.JWTUtil;
import com.ssafy.local.oauth.CustomOAuth2UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomSuccessHandler customSuccessHandler;
	private final JWTUtil jwtUtil;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration configuration = new CorsConfiguration();
				configuration.setAllowedOrigins(List.of("http://localhost:5173","http://localhost:8080"));
				configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
				configuration.setAllowCredentials(true);
				configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization", "X-Requested-With"));
				configuration.setMaxAge(3600L);

				configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));

				return configuration;
			}
		}));

		// csrf disable
		http.csrf((auth) -> auth.disable());
		// From 로그인 방식 disable
		http.formLogin((auth) -> auth.disable());
		// HTTP Basic 인증 방식 disable
		http.httpBasic((auth) -> auth.disable());
		// JWTFilter 추가
		http.addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
		http.exceptionHandling(exception -> exception
	            .authenticationEntryPoint((request, response, authException) -> {
	                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	                response.setContentType("application/json;charset=UTF-8");
	                response.getWriter().write("{\"error\": \"Unauthorized\"}");
	            }));
		// oauth2
		http.oauth2Login((oauth2) -> oauth2
				.userInfoEndpoint(
						(userInfoEndpointConfig) -> userInfoEndpointConfig.userService(customOAuth2UserService))
				.successHandler(customSuccessHandler));

		// 경로별 인가 작업
		http.authorizeHttpRequests(auth -> auth
			    .requestMatchers(
			        "/", 
			        "/api/apt/**", 
			        "/api/apt/aptseqList",
			        "/api/reviews/**",      // 🔓 리뷰 조회는 로그인 없이 허용
			        "/oauth2/**", 
			        "/login/oauth2/**",
			        "/api/user/logout",
			        "/login/oauth2/code/naver",
			        "/api/**"
			    ).permitAll()
			    .anyRequest().authenticated()
			);
		// 세션 설정 : STATELESS
		http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
