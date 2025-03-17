package com.gongcha.berrymatch.postLike;

import com.gongcha.berrymatch.ApiResponse;
import com.gongcha.berrymatch.post.Post;
import com.gongcha.berrymatch.postLike.requestDTO.PostLikeRequest;
import com.gongcha.berrymatch.postLike.responseDTO.PostLikeResponse;
import com.gongcha.berrymatch.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/postlike")
@CrossOrigin(origins = "http://localhost:3000")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;


    @PostMapping("/check")
    public ApiResponse<Boolean> checkPostLike(@RequestBody PostLikeRequest request) {

        System.out.println("게시글에 접속한 사용자는 누구니 ? " + request.getUserId());
        System.out.println("사용자가 접속한 게시글의 인덱스는 ? " + request.getPostId());

        return ApiResponse.ok(postLikeService.checkLike(request));
    }


    @PostMapping("/update")
    public ApiResponse<Boolean> postLike(@RequestBody PostLikeRequest request) {

        // 좋아요 누른 놈의 정체
        System.out.println("좋아요 누가 누름 ? " + request.getUserId());
        System.out.println("어느 게시물에 좋아요 누름 ? " + request.getPostId());

        return ApiResponse.ok(postLikeService.updateLike(request));
    }

}
