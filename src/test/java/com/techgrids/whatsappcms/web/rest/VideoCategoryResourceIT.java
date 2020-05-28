package com.techgrids.whatsappcms.web.rest;

import com.techgrids.whatsappcms.WhatsappstatusApp;
import com.techgrids.whatsappcms.domain.VideoCategory;
import com.techgrids.whatsappcms.repository.VideoCategoryRepository;
import com.techgrids.whatsappcms.repository.search.VideoCategorySearchRepository;
import com.techgrids.whatsappcms.service.VideoCategoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link VideoCategoryResource} REST controller.
 */
@SpringBootTest(classes = WhatsappstatusApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class VideoCategoryResourceIT {

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private VideoCategoryRepository videoCategoryRepository;

    @Autowired
    private VideoCategoryService videoCategoryService;

    /**
     * This repository is mocked in the com.techgrids.whatsappcms.repository.search test package.
     *
     * @see com.techgrids.whatsappcms.repository.search.VideoCategorySearchRepositoryMockConfiguration
     */
    @Autowired
    private VideoCategorySearchRepository mockVideoCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVideoCategoryMockMvc;

    private VideoCategory videoCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VideoCategory createEntity(EntityManager em) {
        VideoCategory videoCategory = new VideoCategory()
            .category(DEFAULT_CATEGORY)
            .description(DEFAULT_DESCRIPTION);
        return videoCategory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VideoCategory createUpdatedEntity(EntityManager em) {
        VideoCategory videoCategory = new VideoCategory()
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION);
        return videoCategory;
    }

    @BeforeEach
    public void initTest() {
        videoCategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createVideoCategory() throws Exception {
        int databaseSizeBeforeCreate = videoCategoryRepository.findAll().size();
        // Create the VideoCategory
        restVideoCategoryMockMvc.perform(post("/api/video-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(videoCategory)))
            .andExpect(status().isCreated());

        // Validate the VideoCategory in the database
        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeCreate + 1);
        VideoCategory testVideoCategory = videoCategoryList.get(videoCategoryList.size() - 1);
        assertThat(testVideoCategory.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testVideoCategory.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the VideoCategory in Elasticsearch
        verify(mockVideoCategorySearchRepository, times(1)).save(testVideoCategory);
    }

    @Test
    @Transactional
    public void createVideoCategoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = videoCategoryRepository.findAll().size();

        // Create the VideoCategory with an existing ID
        videoCategory.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVideoCategoryMockMvc.perform(post("/api/video-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(videoCategory)))
            .andExpect(status().isBadRequest());

        // Validate the VideoCategory in the database
        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the VideoCategory in Elasticsearch
        verify(mockVideoCategorySearchRepository, times(0)).save(videoCategory);
    }


    @Test
    @Transactional
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = videoCategoryRepository.findAll().size();
        // set the field null
        videoCategory.setCategory(null);

        // Create the VideoCategory, which fails.


        restVideoCategoryMockMvc.perform(post("/api/video-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(videoCategory)))
            .andExpect(status().isBadRequest());

        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllVideoCategories() throws Exception {
        // Initialize the database
        videoCategoryRepository.saveAndFlush(videoCategory);

        // Get all the videoCategoryList
        restVideoCategoryMockMvc.perform(get("/api/video-categories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videoCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
    
    @Test
    @Transactional
    public void getVideoCategory() throws Exception {
        // Initialize the database
        videoCategoryRepository.saveAndFlush(videoCategory);

        // Get the videoCategory
        restVideoCategoryMockMvc.perform(get("/api/video-categories/{id}", videoCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(videoCategory.getId().intValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }
    @Test
    @Transactional
    public void getNonExistingVideoCategory() throws Exception {
        // Get the videoCategory
        restVideoCategoryMockMvc.perform(get("/api/video-categories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVideoCategory() throws Exception {
        // Initialize the database
        videoCategoryService.save(videoCategory);

        int databaseSizeBeforeUpdate = videoCategoryRepository.findAll().size();

        // Update the videoCategory
        VideoCategory updatedVideoCategory = videoCategoryRepository.findById(videoCategory.getId()).get();
        // Disconnect from session so that the updates on updatedVideoCategory are not directly saved in db
        em.detach(updatedVideoCategory);
        updatedVideoCategory
            .category(UPDATED_CATEGORY)
            .description(UPDATED_DESCRIPTION);

        restVideoCategoryMockMvc.perform(put("/api/video-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedVideoCategory)))
            .andExpect(status().isOk());

        // Validate the VideoCategory in the database
        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeUpdate);
        VideoCategory testVideoCategory = videoCategoryList.get(videoCategoryList.size() - 1);
        assertThat(testVideoCategory.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testVideoCategory.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the VideoCategory in Elasticsearch
        verify(mockVideoCategorySearchRepository, times(2)).save(testVideoCategory);
    }

    @Test
    @Transactional
    public void updateNonExistingVideoCategory() throws Exception {
        int databaseSizeBeforeUpdate = videoCategoryRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVideoCategoryMockMvc.perform(put("/api/video-categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(videoCategory)))
            .andExpect(status().isBadRequest());

        // Validate the VideoCategory in the database
        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the VideoCategory in Elasticsearch
        verify(mockVideoCategorySearchRepository, times(0)).save(videoCategory);
    }

    @Test
    @Transactional
    public void deleteVideoCategory() throws Exception {
        // Initialize the database
        videoCategoryService.save(videoCategory);

        int databaseSizeBeforeDelete = videoCategoryRepository.findAll().size();

        // Delete the videoCategory
        restVideoCategoryMockMvc.perform(delete("/api/video-categories/{id}", videoCategory.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VideoCategory> videoCategoryList = videoCategoryRepository.findAll();
        assertThat(videoCategoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the VideoCategory in Elasticsearch
        verify(mockVideoCategorySearchRepository, times(1)).deleteById(videoCategory.getId());
    }

    @Test
    @Transactional
    public void searchVideoCategory() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        videoCategoryService.save(videoCategory);
        when(mockVideoCategorySearchRepository.search(queryStringQuery("id:" + videoCategory.getId())))
            .thenReturn(Collections.singletonList(videoCategory));

        // Search the videoCategory
        restVideoCategoryMockMvc.perform(get("/api/_search/video-categories?query=id:" + videoCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(videoCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }
}
