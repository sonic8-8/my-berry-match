package com.gongcha.berrymatch.post;

import com.gongcha.berrymatch.ApiResponse;
import com.gongcha.berrymatch.exception.BusinessException;
import com.gongcha.berrymatch.exception.ErrorCode;
import com.gongcha.berrymatch.post.requestDTO.MyPostRequest;
import com.gongcha.berrymatch.post.requestDTO.PostRequest;
import com.gongcha.berrymatch.post.requestDTO.PostSortRequest;
import com.gongcha.berrymatch.post.responseDTO.PostDataResponse;
import com.gongcha.berrymatch.post.responseDTO.PostResponse;
import com.gongcha.berrymatch.postFile.PostFile;
import com.gongcha.berrymatch.postFile.PostFileRepository;
import com.gongcha.berrymatch.user.User;
import com.gongcha.berrymatch.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.gongcha.berrymatch.exception.ErrorCode.MEMBER_NOT_FOUND;


@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostFileRepository postFileRepository;
    @Autowired
    private UserRepository userRepository;


    /**
     * 게시물 업로드
     */
    public PostResponse postSave(PostRequest request) {

        // User 타입이 필요하니깐, user_id값만 갖고 있고 나머지는 null인 User객체를 만들어준다.

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .build();

        System.out.println("아이디 까봄 : " + user.getId());
        System.out.println("유저의 아이디는 ? " + user.getIdentifier());

        postRepository.save(post);

        return PostResponse.of(post);

    }

    /**
     * 게시물 정렬값(최신순, 인기순)에 따라 게시물 가져오기 (페이지네이션)
     */
    public PostDataResponse getPosts(PostSortRequest request, int currentPage) {

        List<PostData> postDataList = new ArrayList<>();

        Page<Post> posts = null;

        System.out.println("조회 전 정렬 기준 확인");

        if(request.getSortByLikes()){
            System.out.println("인기순으로 조회");
            posts = postRepository.findPostsSortedByLikes(PageRequest.of(currentPage - 1, 6, Sort.Direction.DESC, "createdAt"));
        }else{
            System.out.println("최신순으로 조회");
            posts = postRepository.findPageBy(PageRequest.of(currentPage - 1, 6, Sort.Direction.DESC, "createdAt"));
        }

        for (Post post : posts.getContent()) {
            Long postId = post.getId();
            String thumbnailUrl = postFileRepository.findByPostId(post.getId()).
                    orElseThrow(() -> new BusinessException(ErrorCode.FILE_NOT_EXIST)).getThumbFileUrl();
            String fileUrl = postFileRepository.findByPostId(post.getId()).
                    orElseThrow(() -> new BusinessException(ErrorCode.FILE_NOT_EXIST)).getFileUrl();
            String nickname = post.getUser().getNickname();
            String title = post.getTitle();
            String content = post.getContent();
            String createAt = post.getCreatedAt().toString().substring(0, 10);

            postDataList.add(PostData.builder()
                    .postId(postId)
                    .thumbnailUrl(thumbnailUrl)
                    .fileUrl(fileUrl)
                    .title(title)
                    .content(content)
                    .nickname(nickname)
                    .createAt(createAt)
                    .build());
        }

        String totalPages = String.valueOf(posts.getTotalPages());

        System.out.println("postDataList의 값 : " + postDataList);

        return PostDataResponse.builder()
                .postDataList(postDataList)
                .totalPages(totalPages)
                .build();
    }

    /**
     * 나의 게시물 보기
     */
    public PostDataResponse getMyPosts(MyPostRequest request, int currentPage) {

        User user = User.builder()
                .id(request.getId())
                .build();

        // 나의 게시물 누른놈
        Page<Post> posts = postRepository.findPageByUser(user, PageRequest.of(currentPage - 1, 6, Sort.Direction.DESC, "createdAt"));

        List<PostData> postDataList = new ArrayList<>();

        for (Post post : posts.getContent()) {
            Long postId = post.getId();
            String thumbnailUrl = postFileRepository.findByPostId(post.getId()).
                    orElseThrow(() -> new BusinessException(ErrorCode.FILE_NOT_EXIST)).getThumbFileUrl();
            String fileUrl = postFileRepository.findByPostId(post.getId()).
                    orElseThrow(() -> new BusinessException(ErrorCode.FILE_NOT_EXIST)).getFileUrl();
            String nickname = post.getUser().getNickname();
            String title = post.getTitle();
            String content = post.getContent();
            String createAt = post.getCreatedAt().toString().substring(0, 10);



            postDataList.add(PostData.builder()
                    .postId(postId)
                    .thumbnailUrl(thumbnailUrl)
                    .fileUrl(fileUrl)
                    .title(title)
                    .content(content)
                    .nickname(nickname)
                    .createAt(createAt)
                    .build());
        }

        String totalPages = String.valueOf(posts.getTotalPages());

        System.out.println("postDataList의 값 : " + postDataList);

        return PostDataResponse.builder()
                .postDataList(postDataList)
                .totalPages(totalPages)
                .build();
    }

    /**
     * 게시글 권한 확인 로직
     */
    public Boolean postAuth(PostRequest request) {

        // User 타입인 user_id값을 가지고 있는 자바 객체 생성
        User user = User.builder()
                .id(request.getUserId())
                .build();

        System.out.println("조회 전 postId : " + request.getId());
        System.out.println("조회 전 userId : " + user.getId());

        Optional<Post> result = postRepository.findByIdAndUser(request.getId(), user);

        Boolean response = result.isPresent();

        System.out.println("DB 조회한 값 : " + result);
        System.out.println("반한된 불린값 : " + response);

        return response;
    }

    /**
     * 게시글 수정 로직
     */
    public PostResponse postUpdate(PostRequest request) {
        // User 타입이 필요하니깐, user_id값만 갖고 있고 나머지는 null인 User객체를 만들어준다.

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

//        Post post = Post.builder()
//                .user(user)
//                .title(request.getTitle())
//                .content(request.getContent())
//                .build();

        System.out.println("아이디 까봄 : " + user.getId());
        System.out.println("유저의 아이디는 ? " + user.getIdentifier());

        // post_id와 user_id값을 기준으로 찾은 튜플을 반환해줌. 해당 튜플은 post 엔티티의 튜플이니깐 post타입의 객체로 반환받음.
        Post updateResult = postRepository.findByIdAndUser(request.getId(), user).orElseThrow(() -> new BusinessException(ErrorCode.FILE_NOT_EXIST));

        // 그리고 반환받은 객체의 title과 content만 사용자가 입력한 내용으로 set해줌으로써 수정
        updateResult.setTitle(request.getTitle());
        updateResult.setContent(request.getContent());

        // 수정한 객체를 save (save는 이미 존재하는 데이터인 경우 update를 해줌.)
        postRepository.save(updateResult);

        return PostResponse.of(updateResult);
    }

    /**
     * 게시글 삭제 로직
     */
    @Transactional
    public Boolean postDelete(PostRequest request) {

        // User 타입인 user_id값을 가지고 있는 자바 객체 생성
        User user = User.builder()
                .id(request.getUserId())
                .build();

        Integer deleteResult = postRepository.removeByIdAndUser(request.getId(), user);

        System.out.println("게시글 삭제 결과값 : " + deleteResult);

        if (deleteResult > 0) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 게시글 조회수 로직
     */
    public Boolean postViews(PostRequest request) {

        Optional<Post> post = postRepository.findById(request.getId());

        System.out.println("현재 게시글 조회수 : " + post.get().getView());

        if (post.isPresent()) {
            post.get().setView(post.get().getView() + 1);
            postRepository.save(post.get());
            System.out.println("게시글 조회수 +1 : " + post.get().getView());
            return true; // 조회수 증가 성공
        } else {
            return false; // 게시글을 찾을 수 없음
        }

    }
}
