package com.backend.techblogs.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "seo_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeoMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String metaTitle;

    @Column(length = 300)
    private String metaDescription;

    private String keywords;

    @OneToOne
    @JoinColumn(name = "blog_id")
    @JsonBackReference
    private Blog blog;
}
