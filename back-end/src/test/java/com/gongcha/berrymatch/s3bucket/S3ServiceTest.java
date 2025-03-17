package com.gongcha.berrymatch.s3bucket;

import com.gongcha.berrymatch.postFile.requestDTO.PostFileUploadServiceRequest;
import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
public class S3ServiceTest {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private S3Template s3Template;

    @Container
    private static final LocalStackContainer localStackContainer = new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
            .withServices(LocalStackContainer.Service.S3);

    private String bucketName = "localstack-berry-match-bucket";

    @BeforeEach
    void setUp() {
        openMocks(this);
        s3Template.createBucket(bucketName);
    }

    @AfterEach
    void tearDown() {
        localStackContainer.stop();
    }

    @DisplayName("하이라이트 영상을 S3에 업로드할 수 있다.")
    @Test
    void uploadVideo() throws IOException {
        // given
        MockMultipartFile videoFile = new MockMultipartFile("file", "test-video.mp4", "video/mp4", new ByteArrayInputStream("This is a test video".getBytes()));
        PostFileUploadServiceRequest request = new PostFileUploadServiceRequest(videoFile);

        // when
        String key = s3Service.uploadVideo(request);

        // then
        assertThat(key).startsWith("highlight_video/");
        Resource resource = s3Template.download(bucketName, key);
        String content = new String(resource.getInputStream().readAllBytes());
        assertThat(content).isEqualTo("This is a test video");
    }

    @DisplayName("영상이 아닌 파일을 업로드하려고 할 때 예외가 발생한다.")
    @Test
    void uploadNonVideoFileThrowsException() throws IOException {
        // given
        MockMultipartFile nonVideoFile = new MockMultipartFile("file", "test-image.jpg", "image/jpeg", new ByteArrayInputStream("This is a test image".getBytes()));
        PostFileUploadServiceRequest request = new PostFileUploadServiceRequest(nonVideoFile);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> s3Service.uploadVideo(request), "영상 파일만 업로드할 수 있습니다.");
    }

    @DisplayName("하이라이트 이미지를 S3에 업로드할 수 있다.")
    @Test
    void uploadImage() throws IOException {
        // given
        MockMultipartFile imageFile = new MockMultipartFile("file", "test-image.jpg", "image/jpeg", new ByteArrayInputStream("This is a test image".getBytes()));
        PostFileUploadServiceRequest request = new PostFileUploadServiceRequest(imageFile);

        // when
        String key = s3Service.uploadImage(request);

        // then
        assertThat(key).startsWith("highlight_video/");
        Resource resource = s3Template.download(bucketName, key);
        String content = new String(resource.getInputStream().readAllBytes());
        assertThat(content).isEqualTo("This is a test image");
    }

    @DisplayName("이미지가 아닌 파일을 업로드하려고 할 때 예외가 발생한다.")
    @Test
    void uploadNonImageFileThrowsException() throws IOException {
        // given
        MockMultipartFile nonImageFile = new MockMultipartFile("file", "test-video.mp4", "video/mp4", new ByteArrayInputStream("This is a test video".getBytes()));
        PostFileUploadServiceRequest request = new PostFileUploadServiceRequest(nonImageFile);

        // when & then
        assertThrows(IllegalArgumentException.class, () -> s3Service.uploadImage(request), "사진 파일만 업로드할 수 있습니다.");
    }
}
