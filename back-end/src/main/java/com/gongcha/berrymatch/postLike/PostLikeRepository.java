package com.gongcha.berrymatch.postLike;

import com.gongcha.berrymatch.post.Post;
import com.gongcha.berrymatch.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Boolean existsByPostAndUser(Post post, User user);

    Integer deleteByPostAndUser(Post post, User user);

//    int deleteByUserIdAndPostId(Long user_id, Long post_id);
}
