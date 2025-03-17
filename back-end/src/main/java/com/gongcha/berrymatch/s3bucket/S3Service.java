package com.gongcha.berrymatch.s3bucket;

import com.gongcha.berrymatch.ApiResponse;
import com.gongcha.berrymatch.postFile.PostFile;
import com.gongcha.berrymatch.postFile.requestDTO.PostFileUploadServiceRequest;
import com.gongcha.berrymatch.postFile.responseDTO.PostFileUploadResponse;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${AMAZON_S3_BUCKET_NAME}")
    private String bucketName;

    private final S3Template s3Template;

    /**
     * S3 버킷에 하이라이트 영상을 업로드해주는 메서드
     */
    public String uploadVideo(PostFileUploadServiceRequest request) throws IOException {

        // 파일 검증 메소드
        ValidateVideoFilter(request);

        MultipartFile file = request.getFile();

        String directory = "highlight_video/";
        String storedFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String key = directory + storedFilename;

        s3Template.upload(bucketName, key, file.getInputStream());

        return key;

    }

    /**
     * S3 버킷에 하이라이트 사진을 업로드해주는 메서드
     */
    public String uploadImage(PostFileUploadServiceRequest request) throws IOException {

        ValidateVideoFilter(request);

        MultipartFile file = request.getFile();

        String directory = "highlight_video/";
        String storedFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String key = directory + storedFilename;

        s3Template.upload(bucketName, key, file.getInputStream());

        return key;

    }

    /**
     * S3 버킷에 썸네일 사진을 업로드해주는 메서드
     */
    public String uploadThumbnail(PostFileUploadServiceRequest request) throws IOException {

        ValidateImageFilter(request);

        MultipartFile file = request.getFile();

        String directory = "highlight_thumbnail/";
        String storedFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String key = directory + storedFilename;

        s3Template.upload(bucketName, key, file.getInputStream());

        return key;

    }

    /**
     * 영상 파일 크기와 타입을 검증해주는 메서드
     */
    private static void ValidateVideoFilter(PostFileUploadServiceRequest request) {
        MultipartFile file = request.getFile();

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("video/")) {
            System.out.println(file.getContentType());
            throw new IllegalArgumentException("영상 파일만 업로드할 수 있습니다.");
        }

        Long maxFileSize = 500 * 1024 * 1024L;
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("영상 파일 크기는 500MB를 초과할 수 없습니다.");
        }

    }

    /**
     * 사진 파일 크기와 타입을 검증해주는 메서드
     */
    private static void ValidateImageFilter(PostFileUploadServiceRequest request) {
        MultipartFile file = request.getFile();

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            System.out.println(file.getContentType());
            throw new IllegalArgumentException("사진 파일만 업로드할 수 있습니다.");
        }

        Long maxFileSize = 500 * 1024 * 1024L;
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("사진 파일 크기는 500MB를 초과할 수 없습니다.");
        }

    }

    /**
     * S3 버킷에 하이라이트 사진을 업로드해주는 메서드
     */
    public String uploadProfileImage(PostFileUploadServiceRequest request) throws IOException {

        ValidateImageFilter(request);

        MultipartFile file = request.getFile();

        String directory = "profile_image/";
        String storedFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String key = directory + storedFilename;

        s3Template.upload(bucketName, key, file.getInputStream());

        return key;

    }

}

