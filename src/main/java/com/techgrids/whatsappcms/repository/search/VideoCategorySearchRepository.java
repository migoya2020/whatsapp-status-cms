package com.techgrids.whatsappcms.repository.search;

import com.techgrids.whatsappcms.domain.VideoCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link VideoCategory} entity.
 */
public interface VideoCategorySearchRepository extends ElasticsearchRepository<VideoCategory, Long> {
}
