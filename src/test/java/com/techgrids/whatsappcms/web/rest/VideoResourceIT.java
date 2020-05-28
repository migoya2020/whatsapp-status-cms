package com.techgrids.whatsappcms.web.rest;

import com.techgrids.whatsappcms.WhatsappstatusApp;
import com.techgrids.whatsappcms.domain.Video;
import com.techgrids.whatsappcms.repository.VideoRepository;
import com.techgrids.whatsappcms.repository.search.VideoSearchRepository;
import com.techgrids.whatsappcms.service.VideoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.techgrids.whatsappcms.domain.enumeration.Tags;
/**
 * Integration tests for the {@link VideoResource} REST controller.
 */
@SpringBootTest(classes = WhatsappstatusApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class VideoResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_VIDEO_URL = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_VIDEO_URL = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_VIDEO_URL_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_VIDEO_URL_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Tags DEFAULT_TAGS = Tags.COMIC;
    private static final Tags UPDATED_TAGS = Tags.COMMEDY;

    private static final BigDecimal DEFAULT_VIEWS = new BigDecimal(0);
    private static final BigDecimal UPDATED_VIEWS = new BigDecimal(1);

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoService videoService;

    /**
     * This repository is mocked in the com.techgrids.whatsappcms.repository.search test package.
     *
     * @see com.techgrids.whatsappcms.repository.search.VideoSearchRepositoryMockConfiguration
     */
    @Autowired
    private VideoSearchRepository mockVideoSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVideoMockMvc;

    private Video video;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Video createEntity(EntityManager em) {
        Video video = new Video()
            .title(DEFAULT_TITLE)
            .videoUrl(DEFAULT_VIDEO_URL)
            .videoUrlContentType(DEFAULT_VIDEO_URL_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .date(DEFAULT_DATE)
            .tags(DEFAULT_TAGS)
            .views(DEFAULT_VIEWS);
        return video;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Video createUpdatedEntity(EntityManager em) {
        Video video = new Video()
            .title(UPDATED_TITLE)
            .videoUrl(UPDATED_VIDEO_URL)
            .videoUrlContentType(UPDATED_VIDEO_URL_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .date(UPDATED_DATE)
            .tags(UPDATED_TAGS)
            .views(UPDATED_VIEWS);
        return video;
    }

    @BeforeEach
    public void initTest() {
        video = createEntity(em);
    }

    @Test
    @Transactional
    public void createVideo() throws Exception {
        int databaseSizeBeforeCreate = videoRepository.findAll().size();
        // Create the Video
        restVideoMockMvc.perform(post("/api/videos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isCreated());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeCreate + 1);
        Video testVideo = videoList.get(videoList.size() - 1);
        assertThat(testVideo.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testVideo.getVideoUrl()).isEqualTo(DEFAULT_VIDEO_URL);
        assertThat(testVideo.getVideoUrlContentType()).isEqualTo(DEFAULT_VIDEO_URL_CONTENT_TYPE);
        assertThat(testVideo.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVideo.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testVideo.getTags()).isEqualTo(DEFAULT_TAGS);
        assertThat(testVideo.getViews()).isEqualTo(DEFAULT_VIEWS);

        // Validate the Video in Elasticsearch
        verify(mockVideoSearchRepository, times(1)).save(testVideo);
    }

    @Test
    @Transactional
    public void createVideoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = videoRepository.findAll().size();

        // Create the Video with an existing ID
        video.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideoMockMvc.perform(post("/api/videos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isBadRequest());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeCreate);

        // Validate the Video in Elasticsearch
        verify(mockVideoSearchRepository, times(0)).save(video);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoRepository.findAll().size();
        // set the field null
        video.setTitle(null);

        // Create the Video, which fails.


        restVideoMockMvc.perform(post("/api/videos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isBadRequest());

        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoRepository.findAll().size();
        // set the field null
        video.setDate(null);

        // Create the Video, which fails.


        restVideoMockMvc.perform(post("/api/videos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isBadRequest());

        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVideos() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        // Get all the videoList
        restVideoMockMvc.perform(get("/api/videos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(video.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].videoUrlContentType").value(hasItem(DEFAULT_VIDEO_URL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].videoUrl").value(hasItem(Base64Utils.encodeToString(DEFAULT_VIDEO_URL))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS.toString())))
            .andExpect(jsonPath("$.[*].views").value(hasItem(DEFAULT_VIEWS.intValue())));
    }
    
    @Test
    @Transactional
    public void getVideo() throws Exception {
        // Initialize the database
        videoRepository.saveAndFlush(video);

        // Get the video
        restVideoMockMvc.perform(get("/api/videos/{id}", video.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(video.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.videoUrlContentType").value(DEFAULT_VIDEO_URL_CONTENT_TYPE))
            .andExpect(jsonPath("$.videoUrl").value(Base64Utils.encodeToString(DEFAULT_VIDEO_URL)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS.toString()))
            .andExpect(jsonPath("$.views").value(DEFAULT_VIEWS.intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingVideo() throws Exception {
        // Get the video
        restVideoMockMvc.perform(get("/api/videos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideo() throws Exception {
        // Initialize the database
        videoService.save(video);

        int databaseSizeBeforeUpdate = videoRepository.findAll().size();

        // Update the video
        Video updatedVideo = videoRepository.findById(video.getId()).get();
        // Disconnect from session so that the updates on updatedVideo are not directly saved in db
        em.detach(updatedVideo);
        updatedVideo
            .title(UPDATED_TITLE)
            .videoUrl(UPDATED_VIDEO_URL)
            .videoUrlContentType(UPDATED_VIDEO_URL_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .date(UPDATED_DATE)
            .tags(UPDATED_TAGS)
            .views(UPDATED_VIEWS);

        restVideoMockMvc.perform(put("/api/videos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVideo)))
            .andExpect(status().isOk());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeUpdate);
        Video testVideo = videoList.get(videoList.size() - 1);
        assertThat(testVideo.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testVideo.getVideoUrl()).isEqualTo(UPDATED_VIDEO_URL);
        assertThat(testVideo.getVideoUrlContentType()).isEqualTo(UPDATED_VIDEO_URL_CONTENT_TYPE);
        assertThat(testVideo.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVideo.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testVideo.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testVideo.getViews()).isEqualTo(UPDATED_VIEWS);

        // Validate the Video in Elasticsearch
        verify(mockVideoSearchRepository, times(2)).save(testVideo);
    }

    @Test
    @Transactional
    public void updateNonExistingVideo() throws Exception {
        int databaseSizeBeforeUpdate = videoRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideoMockMvc.perform(put("/api/videos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(video)))
            .andExpect(status().isBadRequest());

        // Validate the Video in the database
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Video in Elasticsearch
        verify(mockVideoSearchRepository, times(0)).save(video);
    }

    @Test
    @Transactional
    public void deleteVideo() throws Exception {
        // Initialize the database
        videoService.save(video);

        int databaseSizeBeforeDelete = videoRepository.findAll().size();

        // Delete the video
        restVideoMockMvc.perform(delete("/api/videos/{id}", video.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Video> videoList = videoRepository.findAll();
        assertThat(videoList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Video in Elasticsearch
        verify(mockVideoSearchRepository, times(1)).deleteById(video.getId());
    }

    @Test
    @Transactional
    public void searchVideo() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        videoService.save(video);
        when(mockVideoSearchRepository.search(queryStringQuery("id:" + video.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(video), PageRequest.of(0, 1), 1));

        // Search the video
        restVideoMockMvc.perform(get("/api/_search/videos?query=id:" + video.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(video.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].videoUrlContentType").value(hasItem(DEFAULT_VIDEO_URL_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].videoUrl").value(hasItem(Base64Utils.encodeToString(DEFAULT_VIDEO_URL))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS.toString())))
            .andExpect(jsonPath("$.[*].views").value(hasItem(DEFAULT_VIEWS.intValue())));
    }
}
