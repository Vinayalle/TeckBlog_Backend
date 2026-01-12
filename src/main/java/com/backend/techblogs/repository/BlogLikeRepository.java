package com.backend.techblogs.repository;

import com.backend.techblogs.entity.BlogLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogLikeRepository extends JpaRepository<BlogLike,Long> {
    boolean existsByBlogIdAndClientIp(Long blogId,String clientIp);
}
