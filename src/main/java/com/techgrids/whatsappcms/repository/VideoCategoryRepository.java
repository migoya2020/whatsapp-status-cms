package com.techgrids.whatsappcms.repository;

import com.techgrids.whatsappcms.domain.VideoCategory;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the VideoCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoCategoryRepository extends JpaRepository<VideoCategory, Long> {
}
