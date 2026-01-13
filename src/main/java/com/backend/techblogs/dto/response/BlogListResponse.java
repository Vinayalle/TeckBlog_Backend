package com.backend.techblogs.dto.response;

import com.backend.techblogs.entity.Category;
import lombok.Data;

import java.time.LocalDate;


@Data
public class BlogListResponse {

    private Long id;
    private String title;
    private String image;
    private String slug;
    private String excerpt;
    private String author;
    private LocalDate publishedDate;
    private Integer readTime;
    private Category category;
}
