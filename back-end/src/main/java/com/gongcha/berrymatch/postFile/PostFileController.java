package com.gongcha.berrymatch.postFile;

import com.gongcha.berrymatch.ApiResponse;
import com.gongcha.berrymatch.post.Post;
import com.gongcha.berrymatch.postFile.requestDTO.PostFileUploadRequest;
import com.gongcha.berrymatch.postFile.responseDTO.PostFileUploadResponse;
import com.gongcha.berrymatch.s3bucket.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController("/api/s3")
@CrossOrigin(origins = "http://localhost:3000")
public class PostFileController {

    private final S3Service s3Service;
    private final PostFileService postFileService;

    // 바이트 -> 메가바이트 변환기
    public static double bytesToMegabytes(long bytes) {
        return (double) bytes / (1024 * 1024);
    }


    @PostMapping("/api/s3/test")
    public String s3(@RequestPart List<MultipartFile> files){

        if(files.isEmpty()){
            System.out.println("파일이 비어있습니다.");
            return "fail" ;
        }else{
            System.out.println("프론트에서 넘어온 파일 데이터 : " + files);

            // 파일명 추출
            String highlightFileName = files.get(0).getOriginalFilename();
            String thumbnailFileName = files.get(1).getOriginalFilename();
            System.out.println("하이라이트 파일명 : " + highlightFileName);
            System.out.println("썸네일 파일명 : " + thumbnailFileName);

            // 파일 MIME 타입 추출 (동영상인지 이미지인지 확인)
            String highlightFileType = files.get(0).getContentType();
            String thumbnailFileType = files.get(1).getContentType();
            System.out.println("하이라이트 파일 타입 : " + highlightFileType);
            System.out.println("썸네일 파일 타입 : " + thumbnailFileType);


            // 파일 크기 추출(byte 단위)
            Long highlightFileSize = files.get(0).getSize();
            Long thumbnailFileSize = files.get(1).getSize();

            System.out.println("하이라이트 파일 크기 : " + highlightFileSize + " byte");
            System.out.println("썸네일 파일 크기 : " + thumbnailFileSize + " byte");
            System.out.println("하이라이트 파일 크기 : " + bytesToMegabytes(highlightFileSize) + " MB");
            System.out.println("썸네일 파일 크기 : " + bytesToMegabytes(thumbnailFileSize) + " MB");


            // s3 버킷에 업로도된 url 가지고 오기

            // key?

            return "success";
        }

    }


    /**
     * 게시글 파일 업로드
     */
    @PostMapping("/api/s3/upload")
    public ApiResponse<PostFileUploadResponse> videoUpload(@RequestParam("file") MultipartFile file,
                                                           @RequestParam("thumbnail") MultipartFile thumbnail,
                                                           @RequestParam("post_id") Post post_id) throws IOException {

            System.out.println("넘어온 post_id를 가지고 있는 참조값 : " + post_id);

        System.out.println("넘어온 하이라이트 : " + file.getOriginalFilename());
        System.out.println("넘어온 썸네일 : " + thumbnail.getOriginalFilename());

            // FE에서 받아온 key가 file인 데이터를 controller용 requestDTO로 만듦
            // 1. 지금 현재 file은 MultipartFile 타입인 상태임. 이 타입을 가진 file을 매개변수로 설정.
            // 현재 얘는 빌더 패턴의 객체를 가지고 있는 놈임
            PostFileUploadRequest request = PostFileUploadRequest.of(file);

            // FE에서 받아온 key가 thumbnail인 데이터를 controller용 requestDTO로 만듦
            PostFileUploadRequest thumbnailRequest = PostFileUploadRequest.of(thumbnail);

            // 1. request.toServiceRequest() : controller용 requestDTO를 service용 requestDTO로 만듦
            // 2. service에서 비즈니스 로직 처리 uploadVideo
            // 3. key를 반환받음 (key는 url의 일부분이다.)
            String fileKey = s3Service.uploadVideo(request.toServiceRequest());



            String thumbnailKey = s3Service.uploadThumbnail(thumbnailRequest.toServiceRequest());

            // 서비스 DTO로 바꾸고 s3 업로드 로직을 처리한다. 업로드를 하면서 해당 파일의 키를 반환해줌. 그걸 key1 변수에 담은거고.
            // 그 다음으로는. 또 request를 서비스 DTO로 바꿔줘야 한다. 왜냐하면 post_file 테이블에 데이터를 삽입해야하기 때문에.
            // 데이터를 삽입해주는 서비스에 있는 비즈니스 로직을 실행해준다.
            // 근데 서비스 로직에서는 자바 객체를 return 해줌.
            // 그 return해준 값과, 전에 발급해준 key를 FE로 반환한다!!!!


            // post_id를 가지고 와야되는데 ->
            // 사용자가 게시글을 등록하는 동시에 post테이블에 데이터가 삽입되고. post_id를 꺼내서 여기에 보내줘야한다.


            // savePostFile() 매개변수에다가 post_id와 썸네일 key를 추가로 보내줘야 한다.
        return ApiResponse.ok(postFileService.savePostFile(request.toServiceRequest(), fileKey, thumbnailKey, post_id));
            // 이 리턴문에서 Service로직을 처리한다~
    }

    /**
     * 게시글 파일 수정
     */
    @PostMapping("/api/s3/update")
    public ApiResponse<Boolean> videoUpdate(@RequestParam("file") MultipartFile file,
                                                           @RequestParam("thumbnail") MultipartFile thumbnail,
                                                           @RequestParam("post_id") Post post_id) throws IOException{
        System.out.println("넘어온 post_id를 가지고 있는 Post 자료형의 객체 : " + post_id.getId());

        PostFileUploadRequest request = PostFileUploadRequest.of(file);

        PostFileUploadRequest thumbnailRequest = PostFileUploadRequest.of(thumbnail);

        String fileKey = s3Service.uploadVideo(request.toServiceRequest());

        String thumbnailKey = s3Service.uploadThumbnail(thumbnailRequest.toServiceRequest());

        return ApiResponse.ok(postFileService.updatePostFile(request.toServiceRequest(), fileKey, thumbnailKey, post_id));
    }


}
