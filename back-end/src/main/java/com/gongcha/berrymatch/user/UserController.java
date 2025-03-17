package com.gongcha.berrymatch.user;

import com.gongcha.berrymatch.ApiResponse;
import com.gongcha.berrymatch.match.Service.MatchingQueueCleanupService;
import com.gongcha.berrymatch.postFile.requestDTO.PostFileUploadRequest;
import com.gongcha.berrymatch.s3bucket.S3Service;
import com.gongcha.berrymatch.springSecurity.constants.ProviderInfo;
import com.gongcha.berrymatch.user.requestDTO.DummyAddRequest;
import com.gongcha.berrymatch.user.requestDTO.DummyDeleteRequest;
import com.gongcha.berrymatch.user.requestDTO.UserSignupRequest;
import com.gongcha.berrymatch.user.responseDTO.UserInfoResponse;
import com.gongcha.berrymatch.user.responseDTO.UserProfileUpdateResponse;
import com.gongcha.berrymatch.user.responseDTO.UserSignupResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final MatchingQueueCleanupService matchingQueueCleanupService;

    /**
     * 소셜 로그인으로 받아온 identifier + 사용자 입력으로 requestDTO를 만들고 회원가입 시켜주는 메서드
     */
    @PostMapping("/auth/signup")
    public ApiResponse<UserSignupResponse> signup(@RequestBody UserSignupRequest userSignupRequest) {

        System.out.println("회원가입 요청 들어옴");

        System.out.println(userSignupRequest.getProviderInfo());
        System.out.println("여기는 providerInfo 정상적으로 들어옴");

        return ApiResponse.ok(userService.signup(userSignupRequest.toService()));
    }

    @GetMapping("/user-info")
    public ApiResponse<UserInfoResponse> getUserInfo(@RequestParam("identifier") String identifier, @RequestParam("providerInfo") String providerInfo) {
        System.out.println("user-info 요청들어옴");
        return ApiResponse.ok(userService.getUserInfo(identifier, ProviderInfo.from(providerInfo)));
    }

    /**
     * 마이 페이지에서 프로필 수정 요청 시 프로필을 수정해주는 메서드
     */
    @PostMapping("/profile/update")
    public ApiResponse<UserProfileUpdateResponse> updateUserInfo(@RequestParam("file") MultipartFile file,
                                                                 @RequestParam("identifier") String identifier,
                                                                 @RequestParam("providerInfo") String providerInfo,
                                                                 @RequestParam("introduction") String introduction
                                                ) throws IOException {


            PostFileUploadRequest request = PostFileUploadRequest.of(file);
            String key = s3Service.uploadProfileImage(request.toServiceRequest());

        return ApiResponse.ok(userService.updateProfile(identifier,
                ProviderInfo.valueOf(providerInfo.toUpperCase()),
                key, introduction));
    }

    @PostMapping("/add-dummy")
    public ApiResponse<String> addDummyUser(@RequestBody DummyAddRequest request) {
        userService.addUser(request.toServiceRequest());
        return ApiResponse.ok("추가함요");
    }

    @PostMapping("/delete-dummy")
    public ApiResponse<String> deleteDummyUser(@RequestBody DummyDeleteRequest request) {
        matchingQueueCleanupService.cleanUpMatchingQueue();
        userService.deleteUser(request.toServiceRequest());
        return ApiResponse.ok("삭제함요");
    }


}
