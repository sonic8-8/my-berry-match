package com.gongcha.berrymatch.postFile.responseDTO;

import com.gongcha.berrymatch.postFile.PostFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostFileUploadResponse {

    private String originalFileName;
    private String storedFileName;
    private String fileType;
    private Long size;
    private String fileKey;
    private String fileUrl;
    private String thumbFileUrl;

    @Builder
    public PostFileUploadResponse(String originalFileName, String storedFileName, String fileType, Long size, String fileKey, String fileUrl, String thumbFileUrl) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.fileType = fileType;
        this.size = size;
        this.fileKey = fileKey;
        this.fileUrl = fileUrl;
        this.thumbFileUrl = thumbFileUrl;
    }
    // 외부에서 빌더를 사용할 때 꼭 모든 필드값과 매개변수를 모두 채워서 생성하지 않아도 된다. 빌더의 유연성때문.

    public static PostFileUploadResponse of(PostFile postFile) {
        return PostFileUploadResponse.builder()
                .originalFileName(postFile.getOriginalFileName())
                .fileType(postFile.getFileType())
                .size(postFile.getSize())
                .fileKey(postFile.getFileKey())
                .fileUrl(postFile.getFileUrl())
                .thumbFileUrl(postFile.getThumbFileUrl())
                .build();
    }

}
