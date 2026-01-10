package com.backend.techblogs.dto.response;


import lombok.Builder;
import lombok.Data;

@Data
public class SeoResponse {
    private String metaTitle;
    private String metaDescription;
    private String keywords;
}
