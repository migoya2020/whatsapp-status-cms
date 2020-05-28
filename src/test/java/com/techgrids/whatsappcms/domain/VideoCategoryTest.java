package com.techgrids.whatsappcms.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.techgrids.whatsappcms.web.rest.TestUtil;

public class VideoCategoryTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VideoCategory.class);
        VideoCategory videoCategory1 = new VideoCategory();
        videoCategory1.setId(1L);
        VideoCategory videoCategory2 = new VideoCategory();
        videoCategory2.setId(videoCategory1.getId());
        assertThat(videoCategory1).isEqualTo(videoCategory2);
        videoCategory2.setId(2L);
        assertThat(videoCategory1).isNotEqualTo(videoCategory2);
        videoCategory1.setId(null);
        assertThat(videoCategory1).isNotEqualTo(videoCategory2);
    }
}
