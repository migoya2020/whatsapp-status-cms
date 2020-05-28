package com.techgrids.whatsappcms.repository;

import com.techgrids.whatsappcms.domain.Video;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Video entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
}
