package com.ssafy.local.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ssafy.local.oauth.CustomOAuth2User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
	private final JWTUtil jwtUtil;
	//로그인 성공시 자동 호출
	 @Override
	    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

	        //OAuth2User 사용자 정보 추출
	        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();
	        //사용자 이름 추출
	        String username = customUserDetails.getName();

	        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
	        GrantedAuthority auth = iterator.next();
	        String role = auth.getAuthority();

	        String token = jwtUtil.createJwt(username, role, 60*60*60L);
	        //JWT를 쿠키에 담아서 클라이언트에 전달
	        response.addCookie(createCookie("Authorization", token));
	        response.sendRedirect("http://localhost:5173/");
	    }

	    private Cookie createCookie(String key, String value) {

	        Cookie cookie = new Cookie(key, value);
	        cookie.setMaxAge(60*60*60);
	        cookie.setSecure(false);
	        cookie.setPath("/");
	        cookie.setHttpOnly(true);

	        return cookie;
	    }
}
/*
OAuth2 로그인 성공
↓
CustomSuccessHandler 호출
↓
JWT 생성 (username + role)
↓
JWT를 쿠키에 저장 (Authorization)
↓
프론트엔드로 리다이렉트
*/