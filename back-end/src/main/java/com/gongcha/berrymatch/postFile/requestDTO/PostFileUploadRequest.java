package com.gongcha.berrymatch.postFile.requestDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Getter
@NoArgsConstructor
public class PostFileUploadRequest {

    private MultipartFile file;

    @Builder
    public PostFileUploadRequest(MultipartFile file) {
        this.file = file;
    }

    public PostFileUploadServiceRequest toServiceRequest () {
        return PostFileUploadServiceRequest.builder()
                .file(file)
                .build();
    }


    public static PostFileUploadRequest of(MultipartFile file) {
        return PostFileUploadRequest.builder()
                .file(file)
                .build();
    }

}
