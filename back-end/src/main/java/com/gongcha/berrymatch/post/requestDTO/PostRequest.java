package com.gongcha.berrymatch.post.requestDTO;


import com.gongcha.berrymatch.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@ToString
public class PostRequest {

    private Long id;
    private Long userId;
    private String title;
    private String content;

    @Builder
    public PostRequest(Long id , Long userId, String title, String content) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
    }

}
