package com.gongcha.berrymatch.user;

import com.gongcha.berrymatch.exception.BusinessException;
import com.gongcha.berrymatch.notification.firebase.requestDTO.FcmTokenRegisterServiceRequest;
import com.gongcha.berrymatch.notification.firebase.responseDTO.FcmTokenRegisterResponse;
import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import com.gongcha.berrymatch.springSecurity.responseDTO.AuthResponse;
import com.gongcha.berrymatch.user.requestDTO.DummyAddServiceRequest;
import com.gongcha.berrymatch.user.requestDTO.DummyDeleteServiceRequest;
import com.gongcha.berrymatch.user.requestDTO.UserSignupServiceRequest;
import com.gongcha.berrymatch.user.responseDTO.UserInfoResponse;
import com.gongcha.berrymatch.user.responseDTO.UserProfileUpdateResponse;
import com.gongcha.berrymatch.user.responseDTO.UserSignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gongcha.berrymatch.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Value("${AMAZON_S3_BUCKET_NAME}")
    private String bucketName;
    @Value("${AMAZON_S3_BUCKET_REGION}")
    private String region;


//    private final UserDetailsServiceAutoConfiguration userDetailsServiceAutoConfiguration;

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
    }

    public User findUserByIdentifier(String identifier, ProviderInfo providerInfo) {
        return userRepository.findByOAuthInfo(identifier, providerInfo)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
    }

    public AuthResponse getUserAuthInfo(String identifier, ProviderInfo providerInfo) {
        User user = userRepository.findByOAuthInfo(identifier, providerInfo)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
        return new AuthResponse(user.getRole());
    }

    public User findUserByOAuthInfo(String identifier, ProviderInfo providerInfo) {
        return userRepository.findByOAuthInfo(identifier, providerInfo)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
    }

    public UserInfoResponse getUserInfo(String identifier, ProviderInfo providerInfo) {
        User user = userRepository.findByOAuthInfo(identifier, providerInfo)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        return UserInfoResponse.builder()
                .userMatchStatus(user.getUserMatchStatus())
                .city(user.getCity())
                .district(user.getDistrict())
                .identifier(user.getIdentifier())
                .nickname(user.getNickname())
                .introduction(user.getIntroduction())
                .profileImageUrl(user.getProfileImageUrl())
                .build();

    }

    /**
     * FE로 받은 사용자 입력으로 User 정보를 업데이트 해 회원가입 시켜주는 메서드
     */
    @Transactional
    public UserSignupResponse signup(UserSignupServiceRequest request) {

        System.out.println("identifier" + request.getIdentifier());
        System.out.println("providerInfo" + request.getProviderInfo());

        User user = userRepository.findByOAuthInfo(request.getIdentifier(), request.getProviderInfo())
                        .orElseThrow(() -> new BusinessException(NOT_AUTHENTICATED_USER));

        user.update(request);

        User savedUser = userRepository.save(user);

        if (user.getRole() == Role.USER) {
            return UserSignupResponse.builder()
                    .identifier(user.getIdentifier())
                    .role(user.getRole())
                    .providerInfo(user.getProviderInfo())
                    .userMatchStatus(user.getUserMatchStatus())
                    .build();
        } else {
            throw new BusinessException(MEMBER_NOT_UPDATED);
        }
    }

    /**
     * 프로필 정보를 수정하는 메서드
     */
    @Transactional
    public UserProfileUpdateResponse updateProfile(String identifier, ProviderInfo providerInfo, String key, String introduction) {

        User user = userRepository.findByOAuthInfo(identifier, providerInfo)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        String profileImageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);

        user.profileUpdate(profileImageUrl, introduction);

        userRepository.save(user);

        System.out.println(user.getProviderInfo());
        System.out.println(user.getIntroduction());
        System.out.println(user.getProfileImageUrl());
        System.out.println(user.getIdentifier());

        return UserProfileUpdateResponse.builder()
                .identifier(user.getIdentifier())
                .providerInfo(user.getProviderInfo())
                .profileImageUrl(user.getProfileImageUrl())
                .introduction(user.getIntroduction())
                .build();
    }

    /**
     * 로그인 시 사용자의 실시간 푸시알림을 위해 DB에 사용자의 Fcm 토큰을 업데이트해주는 메서드
     */
    @Transactional
    public FcmTokenRegisterResponse updateFcmToken(FcmTokenRegisterServiceRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        System.out.println("FCM 토큰 : " + request.getFcmToken());

        user.fcmTokenUpdate(request.getFcmToken());

        User savedUser = userRepository.save(user);

        System.out.println("FCM 토큰 등록 됐는지 확인 : " + savedUser.getFcmToken());

        return FcmTokenRegisterResponse.builder()
                .fcmToken(user.getFcmToken())
                .providerInfo(user.getProviderInfo().toString())
                .identifier(user.getIdentifier())
                .build();
    }

    /**
     * 더미 유저 데이터를 추가하는 메서드
     */
    @Transactional
    public String addUser(DummyAddServiceRequest request) {
        User user = User.builder()
                .id(request.getId())
                .userMatchStatus(request.getUserMatchStatus())
                .nickname(request.getNickname())
                .city(request.getCity())
                .district(request.getDistrict())
                .build();
        userRepository.save(user);

        return user.getNickname();
    }

    @Transactional
    public void deleteUser(DummyDeleteServiceRequest request) {
        userRepository.deleteByNickname(request.getNickname());
    }

    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));
    }

}
