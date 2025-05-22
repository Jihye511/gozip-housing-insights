package com.ssafy.local.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ssafy.local.dto.UserDto;
import com.ssafy.local.oauth.CustomOAuth2User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter{
	private final JWTUtil jwtUtil;
	//
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
	    String path = request.getRequestURI();
	    return path.startsWith("/api/recommend"); // 이 경로는 JWT 검증 생략
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		 //cookie들을 불러온 뒤 Authorization Key에 담긴 쿠키를 찾음
		String authorization = null;
	    Cookie[] cookies = request.getCookies();

	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if ("Authorization".equals(cookie.getName())) {
	                authorization = cookie.getValue();
	                break;
	            }
	        }
	    }

	    if (authorization == null) {
	        System.out.println("token null");
	        filterChain.doFilter(request, response);
	        return;
	    }

	    String token = authorization;

	    // 토큰 소멸 시간 검증
	    if (jwtUtil.isExpired(token)) {
	        System.out.println("token expired");
	        filterChain.doFilter(request, response);
	        return;
	    }

	    // 토큰에서 username과 role 획득
	    String username = jwtUtil.getUsername(token);
	    String role = jwtUtil.getRole(token);

	    UserDto userDTO = UserDto.builder()
	        .user_id(username) // user_id에 저장하는 게 맞습니다
	        .role(role)
	        .build();

	    CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

	    Authentication authToken = new UsernamePasswordAuthenticationToken(
	        customOAuth2User, null, customOAuth2User.getAuthorities()
	    );
	    SecurityContextHolder.getContext().setAuthentication(authToken);

	    System.out.println("🔑 JWT username = " + username);
	    System.out.println("🔑 JWT role = " + role);

	    filterChain.doFilter(request, response);
		
	}

}
