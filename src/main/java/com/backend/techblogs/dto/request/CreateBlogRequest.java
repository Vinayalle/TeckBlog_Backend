package com.backend.techblogs.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Data
public class CreateBlogRequest {


    private String title;
    private String slug;

    private SeoRequest seo;

    private String excerpt;
    private String content;
    private String image;

    private String category;
    private List<String> tags;

    private String author;
    private LocalDate date;
    private Integer readTime;
}

