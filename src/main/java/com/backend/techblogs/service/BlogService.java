package com.backend.techblogs.service;

import com.backend.techblogs.dto.request.CreateBlogRequest;
import com.backend.techblogs.dto.response.BlogListResponse;
import com.backend.techblogs.dto.response.BlogResponse;
import com.backend.techblogs.entity.Blog;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;

public interface BlogService {
    Blog createBlog(CreateBlogRequest request);

     Page<BlogListResponse> getAllBlogs(int page, int size, String sortBy, String direction);

     BlogResponse updateBlog(Long id, CreateBlogRequest request);

     BlogResponse getBlogById(Long id);

    String deleteById(Long id);
}
