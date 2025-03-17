package com.gongcha.berrymatch.post.responseDTO;

import com.gongcha.berrymatch.post.PostData;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostDataResponse {

    private List<PostData> postDataList;
    private String totalPages;

    @Builder
    public PostDataResponse(List<PostData> postDataList, String totalPages) {
        this.postDataList = postDataList;
        this.totalPages = totalPages;
    }
}
