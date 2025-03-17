package com.gongcha.berrymatch.postFile.requestDTO;

import com.gongcha.berrymatch.postFile.PostFile;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class PostFileUploadServiceRequest {

    private MultipartFile file;

    @Builder
    public PostFileUploadServiceRequest(MultipartFile file) {
        this.file = file;
    }

    // 매개변수로 들어온 file을 ServiceRequest의 file값에 할당한다.

}
