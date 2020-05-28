package com.techgrids.whatsappcms.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link VideoCategorySearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class VideoCategorySearchRepositoryMockConfiguration {

    @MockBean
    private VideoCategorySearchRepository mockVideoCategorySearchRepository;

}
