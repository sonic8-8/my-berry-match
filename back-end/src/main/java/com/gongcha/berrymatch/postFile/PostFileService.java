package com.gongcha.berrymatch.postFile;

import com.gongcha.berrymatch.post.Post;
import com.gongcha.berrymatch.postFile.requestDTO.PostFileUploadServiceRequest;
import com.gongcha.berrymatch.postFile.responseDTO.PostFileUploadResponse;
import com.gongcha.berrymatch.s3bucket.S3Service;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostFileService {

    private final S3Service s3Service;

    @Value("${AMAZON_S3_BUCKET_NAME}")
    private String bucketName;
    @Value("${AMAZON_S3_BUCKET_REGION}")
    private String region;

    private final PostFileRepository postFileRepository;

    /**
     * 게시물 업로드 요청(DTO)와 S3 버킷 업로드 후 생성된 key를 매개변수로 받아 생성된 PostFile 데이터를 DB에 저장하는 메서드
     */
    public PostFileUploadResponse savePostFile(PostFileUploadServiceRequest request, String fileKey, String thumbnailKey, Post post_id) {

        MultipartFile file = request.getFile();

        // 하이라이트 파일 URL
        String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileKey);

        // 썸네일 파일 URL
        String thumbnailUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, thumbnailKey);



        PostFile postFile = PostFile.builder()
                .post(post_id)
                .originalFileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .size(file.getSize())
                .fileKey(fileKey)
                .fileUrl(fileUrl)
                .thumbFileUrl(thumbnailUrl)
                .build();

        postFileRepository.save(postFile);

        // PostFileUploadResponse에도 썸네일url담아주는 데이터 추가완료
        return PostFileUploadResponse.of(postFile);
    }

    /**
     * 게시글 파일 수정 로직
     */
    public Boolean updatePostFile(PostFileUploadServiceRequest request, String fileKey, String thumbnailKey, Post post_id) {
        MultipartFile file = request.getFile();

        // 하이라이트 파일 URL
        String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileKey);

        // 썸네일 파일 URL
        String thumbnailUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, thumbnailKey);

        Optional<PostFile> updateResult = postFileRepository.findByPostId(post_id.getId());

        System.out.println("업데이트할 post_id 조회값 : " + updateResult.isPresent());

        if (updateResult.isPresent()) {
            PostFile result = updateResult.get();
            result.setFileKey(fileKey);
            result.setFileUrl(fileUrl);
            result.setThumbFileUrl(thumbnailUrl);
            result.setSize(file.getSize());
            result.setOriginalFileName(file.getOriginalFilename());

            System.out.println("파일 업데이트 후 객체 : " + result);

            PostFile saveResult = postFileRepository.save(result);

            System.out.println("파일 업데이트 후 객체 : " + saveResult);

            return true;
        }else{
            return false;
        }
    }
}
