package com.gongcha.berrymatch.postLike;

import com.gongcha.berrymatch.post.Post;
import com.gongcha.berrymatch.post.PostRepository;
import com.gongcha.berrymatch.postLike.requestDTO.PostLikeRequest;
import com.gongcha.berrymatch.postLike.requestDTO.PostLikeServiceRequest;
import com.gongcha.berrymatch.postLike.responseDTO.PostLikeResponse;
import com.gongcha.berrymatch.user.User;
import com.gongcha.berrymatch.user.UserRepository;
import com.gongcha.berrymatch.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PostLikeService {

    @Autowired
    private PostLikeRepository postLikeRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    public Boolean checkLike(PostLikeRequest request) {

        Post post = postRepository.findById(request.getPostId()).get();
        User user = userRepository.findById(request.getUserId()).get();

        System.out.println("여기 Post 값 : " + post.getId());
        System.out.println("여기 User 값 : " + user.getId());

        return postLikeRepository.existsByPostAndUser(post, user);
    }

    @Transactional
    public Boolean updateLike(PostLikeRequest request) {

        Post post = postRepository.findById(request.getPostId()).get();
        User user = userRepository.findById(request.getUserId()).get();

        Boolean result = postLikeRepository.existsByPostAndUser(post, user);

        System.out.println("DB에서 좋아요 여부 조회 : " + result);

        if(!result){
            PostLike postLike = PostLike.builder()
                    .post(post)
                    .user(user)
                    .build();

            System.out.println("PostLike 빌더의 게시글 id 값 : " + postLike.getPost().getId());
            System.out.println("PostLike 빌더의 유저 id 값 : " + postLike.getUser().getId());

            PostLike saveResult = postLikeRepository.save(postLike);

            System.out.println("저장된 후 결과값 : " + saveResult);
            return true;
        }else{
            Integer deleteResult = postLikeRepository.deleteByPostAndUser(post, user);

            System.out.println("삭제된 후 결과값 : " + deleteResult);
            return false;
        }
    }
}
