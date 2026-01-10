package com.backend.techblogs.controller;


import com.backend.techblogs.dto.request.CreateBlogRequest;
import com.backend.techblogs.dto.response.BlogListResponse;
import com.backend.techblogs.dto.response.BlogResponse;
import com.backend.techblogs.entity.Blog;
import com.backend.techblogs.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @PostMapping
    public ResponseEntity<Blog> createBlog(
            @RequestBody @Validated CreateBlogRequest request) {
        Blog blog = blogService.createBlog(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(blog);
    }


    @GetMapping
    public ResponseEntity<Page<BlogListResponse>> getAllBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "publishedDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        return ResponseEntity.ok(
                blogService.getAllBlogs(page, size, sortBy, direction)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogResponse> updateBlog(
            @PathVariable Long id,
            @RequestBody CreateBlogRequest request) {

        return ResponseEntity.ok(blogService.updateBlog(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogResponse> getBlogById(
            @PathVariable Long id) {

        return ResponseEntity.ok(blogService.getBlogById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
       String isDeleted = blogService.deleteById(id);
        return  new ResponseEntity<>(isDeleted,HttpStatus.OK);

    }




}
