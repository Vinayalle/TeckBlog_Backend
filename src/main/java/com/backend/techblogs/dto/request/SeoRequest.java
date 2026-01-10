package com.backend.techblogs.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class SeoRequest {

    private String metaTitle;
    private String metaDescription;
    private String keywords;
}

