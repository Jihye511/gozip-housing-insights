package com.ssafy.local.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


import com.ssafy.local.dto.UserDto;
import com.ssafy.local.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		//네이버로부터 요청 받고 받아온 사용자 정보
		OAuth2User oAuth2User = super.loadUser(userRequest);

		System.out.println(oAuth2User);
		
		//어디서 받아오는지 확인
		String registrationId = userRequest.getClientRegistration().getRegistrationId();
		OAuth2Response oAuth2Response = null;
		if (registrationId.equals("naver")) {

			oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
		} else {

			return null;
		}

		// 리소스 서버에서 발급 받은 정보로 사용자를 특정할 아이디값을 만듬
		String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();
		UserDto existData = userRepository.selectUserByID(username);
		System.out.println("확인 : "+username);
		if (existData == null) {
		    UserDto userDto = UserDto.builder()
		        .user_id(username)
		        .email(oAuth2Response.getEmail())
		        .name(oAuth2Response.getName())
		        .role("ROLE_USER")
		        .build();

		    userRepository.insertUser(userDto);

		    return new CustomOAuth2User(userDto);
		} else {
		    existData.setEmail(oAuth2Response.getEmail());
		    existData.setName(oAuth2Response.getName());

		    userRepository.updateUser(existData);

		    return new CustomOAuth2User(existData);
		}

	}
}
