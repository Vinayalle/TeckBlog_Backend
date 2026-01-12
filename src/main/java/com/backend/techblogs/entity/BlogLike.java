package com.backend.techblogs.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"blog_id", "clientIp"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long blogId;
    private String clientIp;

    private LocalDateTime createdAt = LocalDateTime.now();
}
