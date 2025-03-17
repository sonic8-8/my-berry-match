package com.gongcha.berrymatch.post.responseDTO;

import com.gongcha.berrymatch.post.Post;
import com.gongcha.berrymatch.postFile.PostFile;
import lombok.Builder;

import java.time.LocalDateTime;

public class PostListResponse {

    private Long post_id;
    private String nickname;
    private String title;
    private String thumbnailUrl;
    private LocalDateTime createdAt;

    @Builder
    public PostListResponse(String nickname, String title, String thumbnailUrl, LocalDateTime createdAt) {
        this.nickname = nickname;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.createdAt = createdAt;
    }

    public PostListResponse of(Post post, PostFile postFile, String nickname) {
        return PostListResponse.builder()
                .nickname(nickname)
                .title(post.getTitle())
                .thumbnailUrl(postFile.getThumbFileUrl())
                .createdAt(post.getCreatedAt())
                .build();
    }

}
