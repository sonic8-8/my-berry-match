package com.gongcha.berrymatch.post;

import com.gongcha.berrymatch.ApiResponse;
import com.gongcha.berrymatch.post.requestDTO.MyPostRequest;
import com.gongcha.berrymatch.post.requestDTO.PostRequest;
import com.gongcha.berrymatch.post.requestDTO.PostSortRequest;
import com.gongcha.berrymatch.post.responseDTO.PostDataResponse;
import com.gongcha.berrymatch.post.responseDTO.PostResponse;
import com.gongcha.berrymatch.user.User;
import com.gongcha.berrymatch.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;

    /**
     * 게시글 업로드
     */
    @PostMapping("/post/upload")
    public ApiResponse<PostResponse> post(@RequestBody PostRequest request) {

        System.out.println("PostRequest값 : " + request.toString());

        return ApiResponse.ok(postService.postSave(request));
    }

    /**
     * 현재 접속한 페이지의 게시물들이랑 총 페이지 수를 보내줌. (게시글 정렬 데이터도 포함)
     */
    @PostMapping("/postpage/{currentPage}")
    public ApiResponse<PostDataResponse> postList(@RequestBody PostSortRequest request, @PathVariable int currentPage) {

        System.out.println("정렬값들 : " + request);
        System.out.println("넘어온 페이지 값 : " + currentPage);

            return ApiResponse.ok(postService.getPosts(request, currentPage));
    }

    /**
     *  내가 작성한 게시글만 보기
     */
    @PostMapping("/mypostpage/{currentPage}")
    public ApiResponse<PostDataResponse> myPostList(@RequestBody MyPostRequest request, @PathVariable int currentPage) {
        System.out.println("마이포스트 누른놈 :" + request.getId());

        System.out.println("넘어온 페이지 값 : " + currentPage);
        return ApiResponse.ok(postService.getMyPosts(request, currentPage));
    }

    /**
     * 게시글 권한 확인
     */
    @PostMapping("/post/auth")
    public ApiResponse<Boolean> postAuth(@RequestBody PostRequest request) {

        System.out.println("게시글 권한 확인 : " + request);

        return ApiResponse.ok(postService.postAuth(request));
    }

    /**
     * 게시글 수정
     */
    @PostMapping("/post/update")
    public ApiResponse<PostResponse> postEdit(@RequestBody PostRequest request) {

        System.out.println("프론트에서 보낸 수정할 데이터 : " + request);

        return ApiResponse.ok(postService.postUpdate(request));
    }

    /**
     * 게시글 삭제
     */
    @PostMapping("/post/delete")
    public ApiResponse<Boolean> postDelete(@RequestBody PostRequest request) {
        return ApiResponse.ok(postService.postDelete(request));
    }

    /**
     * 게시글 조회수
     */
    @PostMapping("/post/views")
    public ApiResponse<Boolean> postViews(@RequestBody PostRequest request) {

        System.out.println("어떤 게시글의 조회수가 올라가나요 : " + request.getId());

        return ApiResponse.ok(postService.postViews(request));
    }

}