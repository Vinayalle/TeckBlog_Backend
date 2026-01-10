package com.backend.techblogs.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "blogs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String slug;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String excerpt;

    @Lob
    @Column(nullable = false)
    private String content;

    private String image;

    private Integer readTime;

    private LocalDate publishedDate;

    private String author;

    @OneToOne(mappedBy = "blog", cascade = CascadeType.ALL)
    @JsonManagedReference
    private SeoMetadata seo;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "blog_tags",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();
}
