package com.gongcha.berrymatch.postLike.requestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostLikeServiceRequest {

    private Long postId;
    private Long userId;

    @Builder
    public PostLikeServiceRequest(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }





}
