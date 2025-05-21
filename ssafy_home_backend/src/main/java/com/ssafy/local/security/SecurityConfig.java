package com.ssafy.local.security;

import java.util.Arrays;
import java.util.Collections;

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
				configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
				configuration.setAllowedMethods(Collections.singletonList("*"));
				configuration.setAllowCredentials(true);
				configuration.setAllowedHeaders(Collections.singletonList("*"));
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
			        "/api/reviews/**",      // 🔓 리뷰 조회는 로그인 없이 허용
			        "/oauth2/**", 
			        "/login/oauth2/**",
			        "/api/user/logout"
			    ).permitAll()
			    .anyRequest().authenticated()
			);
		// 세션 설정 : STATELESS
		http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
