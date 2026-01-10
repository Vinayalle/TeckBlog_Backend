package com.backend.techblogs.repository;

import com.backend.techblogs.entity.SeoMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SeoRepository extends JpaRepository<SeoMetadata,Long> {
}
