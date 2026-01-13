package com.backend.techblogs.service.Impl;

import com.backend.techblogs.dto.request.CreateBlogRequest;
import com.backend.techblogs.dto.response.BlogListResponse;
import com.backend.techblogs.dto.response.BlogResponse;
import com.backend.techblogs.dto.response.SeoResponse;
import com.backend.techblogs.entity.*;
import com.backend.techblogs.repository.BlogLikeRepository;
import com.backend.techblogs.repository.BlogRepository;
import com.backend.techblogs.repository.CategoryRepository;
import com.backend.techblogs.repository.TagRepository;
import com.backend.techblogs.service.BlogService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class BlogServiceImpl implements BlogService {

    @Autowired
    private  BlogRepository blogRepository;

    @Autowired
    private BlogLikeRepository blogLikeRepository;
    @Autowired
    private  CategoryRepository categoryRepository;
    @Autowired
    private  TagRepository tagRepository;


    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Blog createBlog(CreateBlogRequest request) {

        System.out.println(request);
        if (blogRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Blog with slug already exists");
        }

        // 1️⃣ Category
        Category category = categoryRepository
                .findByName(request.getCategory())
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(request.getCategory());
                    return categoryRepository.save(newCategory);
                });

        Set<Tag> tags = request.getTags()
                .stream()
                .map(tagName ->
                        tagRepository.findByName(tagName)
                                .orElseGet(() -> {
                                    Tag tag = new Tag();
                                    tag.setName(tagName);
                                    return tagRepository.save(tag);
                                })
                )
                .collect(Collectors.toSet());

        // 3️⃣ Blog
        Blog blog = new Blog();
        blog.setSlug(request.getSlug());
        blog.setTitle(request.getTitle());
        blog.setExcerpt(request.getExcerpt());
        blog.setContent(request.getContent());
        blog.setImage(request.getImage());
        blog.setAuthor(request.getAuthor());
        blog.setPublishedDate(request.getDate());
        blog.setReadTime(request.getReadTime());
        blog.setCategory(category);
        blog.setTags(tags);

        // 4️⃣ SEO
        SeoMetadata seo = new SeoMetadata();
        seo.setMetaTitle(request.getSeo().getMetaTitle());
        seo.setMetaDescription(request.getSeo().getMetaDescription());
        seo.setKeywords(request.getSeo().getKeywords());
        seo.setBlog(blog);
        blog.setSeo(seo);



        return blogRepository.save(blog);


    }

    @Override
    public Page<BlogListResponse> getAllBlogs(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Blog> blogPage=blogRepository.findAll(pageable);

        return blogPage.map(blog ->
                modelMapper.map(blog, BlogListResponse.class)
        );
    }

    @Override
    @Transactional
    public BlogResponse updateBlog(Long blogId, CreateBlogRequest request) {

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        // 1️⃣ Slug uniqueness check (only if changed)
        if (request.getSlug() != null &&
                !request.getSlug().equals(blog.getSlug()) &&
                blogRepository.existsBySlug(request.getSlug())) {
            throw new RuntimeException("Blog with slug already exists");
        }

        // 2️⃣ Category (reuse or create)
        if (request.getCategory() != null) {
            Category category = categoryRepository
                    .findByName(request.getCategory())
                    .orElseGet(() -> {
                        Category newCategory = new Category();
                        newCategory.setName(request.getCategory());
                        return categoryRepository.save(newCategory);
                    });
            blog.setCategory(category);
        }

        // 3️⃣ Tags (reuse or create)
        if (request.getTags() != null) {
            Set<Tag> tags = request.getTags()
                    .stream()
                    .map(tagName ->
                            tagRepository.findByName(tagName)
                                    .orElseGet(() -> {
                                        Tag tag = new Tag();
                                        tag.setName(tagName);
                                        return tagRepository.save(tag);
                                    })
                    )
                    .collect(Collectors.toSet());
            blog.setTags(tags);
        }

        // 4️⃣ Blog fields (partial update)
        if (request.getSlug() != null) blog.setSlug(request.getSlug());
        if (request.getTitle() != null) blog.setTitle(request.getTitle());
        if (request.getExcerpt() != null) blog.setExcerpt(request.getExcerpt());
        if (request.getContent() != null) blog.setContent(request.getContent());
        if (request.getImage() != null) blog.setImage(request.getImage());
        if (request.getAuthor() != null) blog.setAuthor(request.getAuthor());
        if (request.getDate() != null) blog.setPublishedDate(request.getDate());
        if (request.getReadTime() != null) blog.setReadTime(request.getReadTime());

        // 5️⃣ SEO (update or create)
        if (request.getSeo() != null) {
            SeoMetadata seo = blog.getSeo();
            if (seo == null) {
                seo = new SeoMetadata();
                seo.setBlog(blog);
            }
            seo.setMetaTitle(request.getSeo().getMetaTitle());
            seo.setMetaDescription(request.getSeo().getMetaDescription());
            seo.setKeywords(request.getSeo().getKeywords());
            blog.setSeo(seo);
        }

        // 6️⃣ Save updated blog
        Blog savedBlog = blogRepository.save(blog);

        // 7️⃣ Map to BlogResponse using ModelMapper
        BlogResponse response = modelMapper.map(savedBlog, BlogResponse.class);

        // 8️⃣ Manual mapping for complex fields
        response.setCategory(
                savedBlog.getCategory() != null
                        ? savedBlog.getCategory().getName()
                        : null
        );

        response.setTags(
                savedBlog.getTags()
                        .stream()
                        .map(Tag::getName)
                        .toList()
        );

        if (savedBlog.getSeo() != null) {
            SeoResponse seoResponse =
                    modelMapper.map(savedBlog.getSeo(), SeoResponse.class);
            response.setSeo(seoResponse);
        }

        return response;
    }

    @Override
    public BlogResponse getBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        BlogResponse response = modelMapper.map(blog, BlogResponse.class);

        // Manual mapping for relations
        response.setCategory(blog.getCategory().getName());

        response.setTags(
                blog.getTags()
                        .stream()
                        .map(Tag::getName)
                        .collect(Collectors.toList())
        );

        return response;
    }

    @Override
    public String deleteById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with id: " + id));

       blogRepository.delete(blog);

        return "Blog Deleted Successfully";


    }

    @Override
    public int likeBlog(Long blogId, String clientIp) {
        if (blogLikeRepository.existsByBlogIdAndClientIp(blogId, clientIp)) {
            throw new RuntimeException("Already liked");
        }

        System.out.println(clientIp);

        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        blog.setLikeCount(blog.getLikeCount() + 1);
        blogRepository.save(blog);

        BlogLike like = new BlogLike();
        like.setBlogId(blogId);
        like.setClientIp(clientIp);
        blogLikeRepository.save(like);

        return blog.getLikeCount();
    }

    @Override
    public BlogResponse getBlogBySlug(String slug) {
        Blog blog = blogRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        BlogResponse response = modelMapper.map(blog, BlogResponse.class);

        // Manual mapping for relations
        response.setCategory(blog.getCategory().getName());

        response.setTags(
                blog.getTags()
                        .stream()
                        .map(Tag::getName)
                        .collect(Collectors.toList())
        );

        return response;
    }

}
