package com.ssafy.local.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.ssafy.local.jwt.JWTFilter;
import com.ssafy.local.jwt.JWTUtil;
import com.ssafy.local.jwt.CustomSuccessHandler;
import com.ssafy.local.oauth.CustomOAuth2UserService;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)  // ← 이 줄
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomSuccessHandler customSuccessHandler;
    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    /**
     * 암호화를 위한 PasswordEncoder 빈 등록
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     * AuthenticationManager 빈 등록 (DAO 기반 인증)
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoProvider = new DaoAuthenticationProvider();
        daoProvider.setUserDetailsService(userDetailsService);
        daoProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(daoProvider);
    }

    /**
     * Spring Security HTTP 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CORS 설정
            .cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Arrays.asList("http://localhost:5173","http://localhost:8080"));
                    config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS"));
                    config.setAllowCredentials(true);
                    config.setAllowedHeaders(Arrays.asList("Content-Type","Authorization","X-Requested-With"));
                    config.setExposedHeaders(Arrays.asList("Set-Cookie","Authorization"));
                    config.setMaxAge(3600L);
                    return config;
                }
            }))
            // CSRF 비활성화
            .csrf(csrf -> csrf.disable())
            // 세션 상태 없이 JWT로 인증 처리
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // 기본 로그인/폼 로그인, HTTP Basic 비활성화
            .formLogin(login -> login.disable())
            .httpBasic(basic -> basic.disable())

		
            // JWT 필터를 UsernamePasswordAuthenticationFilter 이전에 추가
            .addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)

            // 예외 처리: 인증 실패 시 401 응답
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, authEx) -> {
                    res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    res.setContentType("application/json;charset=UTF-8");
                    res.getWriter().write("{\"error\": \"Unauthorized\"}");
                })
            )

            // OAuth2 로그인 설정
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
                .successHandler(customSuccessHandler)
            )

            // URL 인증/인가 설정
            .authorizeHttpRequests(auth -> auth
                // 퍼미션 설정: 누구나 접근 허용
                .requestMatchers(
                		"/**",
                    "/api/auth/**",        // 일반 로그인/회원가입
                    "/oauth2/**",
                    "/login/oauth2/**",
                    "/api/apt/**",         // 아파트 조회
                    "/api/reviews/**",
                    "/api/user/**"// 리뷰 조회
                ).permitAll()
                // 나머지 요청은 인증 필요
                .anyRequest().authenticated()
            );

        return http.build();
    }
}
