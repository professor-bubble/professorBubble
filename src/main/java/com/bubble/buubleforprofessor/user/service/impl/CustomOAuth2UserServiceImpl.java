package com.bubble.buubleforprofessor.user.service.impl;

import com.bubble.buubleforprofessor.user.dto.CustomOAuth2User;
import com.bubble.buubleforprofessor.user.dto.OAuth2ResponseDto;
import com.bubble.buubleforprofessor.user.dto.impl.GoogleResponseImplDto;
import com.bubble.buubleforprofessor.user.dto.impl.NaverResponseImplDto;
import com.bubble.buubleforprofessor.user.entity.Role;
import com.bubble.buubleforprofessor.user.entity.User;
import com.bubble.buubleforprofessor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // SecurityContext에 OAuth2User 객체 담기
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("oAuth2Uesr : " + oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        System.out.println("registerationId : " + registrationId);

        // OAuth2ResponseDto 인터페이스로 모든 OAuth2 로그인을 관리
        OAuth2ResponseDto oAuth2ResponseDto = null;
        if (registrationId.equals("naver")) {
            oAuth2ResponseDto = new NaverResponseImplDto(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2ResponseDto = new GoogleResponseImplDto(oAuth2User.getAttributes());
        } else {
            return null;
        }

        // DB 저장
        // 리소스 서버에서 발급받은 정보로 사용자를 특정할 ID값을 만듦
        String username = oAuth2ResponseDto.getProvider() + " " + oAuth2ResponseDto.getProviderId();

        User existData = userRepository.findByLoginId(username);
        System.out.println("user : " + existData);

        if (Objects.isNull(existData)) {
            User newUser = User.builder()
                    .loginId(username)
                    .name(oAuth2ResponseDto.getName())
//                    .password(bCryptPasswordEncoder.encode(UUID.randomUUID().toString()))
                    .password(username)
                    .phoneNumber("000-0000-0000")
                    .email(oAuth2ResponseDto.getEmail())
                    .role(new Role("ROLE_USER"))
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .build();

            userRepository.save(newUser);

            CustomOAuth2User customOAuth2User = new CustomOAuth2User(newUser);

            return customOAuth2User;
        } else {
            // Todo setter 처리

            CustomOAuth2User customOAuth2User = new CustomOAuth2User(existData);

            return customOAuth2User;
        }
    }
}
