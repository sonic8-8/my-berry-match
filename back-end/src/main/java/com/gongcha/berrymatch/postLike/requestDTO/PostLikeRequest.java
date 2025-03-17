package com.gongcha.berrymatch.postLike.requestDTO;

import com.gongcha.berrymatch.post.Post;
import com.gongcha.berrymatch.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.NumberFormat;

@ToString
@Getter
@NoArgsConstructor
public class PostLikeRequest {

    private Long postId;
    private Long userId;

}
