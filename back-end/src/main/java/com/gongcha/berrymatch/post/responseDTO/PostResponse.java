package com.gongcha.berrymatch.post.responseDTO;

import com.gongcha.berrymatch.post.Post;
import com.gongcha.berrymatch.postFile.PostFile;
import com.gongcha.berrymatch.postFile.responseDTO.PostFileUploadResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class PostResponse {

    private Long id; // 이거 Post 식별자 아이디임
    private String title;
    private String content;
    private String fileUrl;
    private String thumbnailUrl;

    // 빌더로 유연한 생성자의 틀을 만듦
    // 빌더 생성자가 받는 매개변수는 나중에 빌더로 생성자를 만들 때 각 속성들이 넘겨줘야할 매개변수의 타입임
    @Builder
    public PostResponse(Long id,String title, String content, String fileUrl, String thumbnailUrl) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.fileUrl = fileUrl;
        this.thumbnailUrl = thumbnailUrl;
    }


    public static PostResponse of(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
//                .fileUrl(postFile.getFileUrl())
//                .thumbnailUrl(postFile.getThumbFileUrl())
                .build();
    }
}
