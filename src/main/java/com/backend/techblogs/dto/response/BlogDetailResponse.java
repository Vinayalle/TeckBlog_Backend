package com.backend.techblogs.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
public class BlogDetailResponse {

    private Long id;
    private String title;
    private String slug;
    private String excerpt;
    private String content;
    private String image;
    private String author;
    private LocalDate publishedDate;
    private Integer readTime;

    private String category;
    private List<String> tags;

    private SeoResponse seo;
}
