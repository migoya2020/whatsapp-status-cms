package com.techgrids.whatsappcms.repository.search;

import com.techgrids.whatsappcms.domain.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Video} entity.
 */
public interface VideoSearchRepository extends ElasticsearchRepository<Video, Long> {
}
