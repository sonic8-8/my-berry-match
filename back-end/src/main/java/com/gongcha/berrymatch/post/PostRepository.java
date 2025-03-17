package com.gongcha.berrymatch.post;

import com.gongcha.berrymatch.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findPageBy(Pageable pageable);
    Page<Post> findPageByUser(User user, Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN PostLike pl ON p.id = pl.post.id GROUP BY p.id ORDER BY COUNT(pl.id) DESC")
    Page<Post> findPostsSortedByLikes(Pageable pageable);

    Optional<Post> findByIdAndUser(Long id, User user);
    Integer removeByIdAndUser(Long id, User user);
}
