package com.backend.techblogs.repository;

import com.backend.techblogs.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    boolean existsBySlug(String slug);


    Optional<Blog> findBySlug(String slug);
}
