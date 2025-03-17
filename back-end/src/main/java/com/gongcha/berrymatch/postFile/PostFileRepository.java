package com.gongcha.berrymatch.postFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostFileRepository extends JpaRepository<PostFile, Long> {
    Optional<PostFile> findByPostId(Long post);
}
