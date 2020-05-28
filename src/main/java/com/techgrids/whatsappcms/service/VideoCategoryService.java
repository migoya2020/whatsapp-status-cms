package com.techgrids.whatsappcms.service;

import com.techgrids.whatsappcms.domain.VideoCategory;
import com.techgrids.whatsappcms.repository.VideoCategoryRepository;
import com.techgrids.whatsappcms.repository.search.VideoCategorySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link VideoCategory}.
 */
@Service
@Transactional
public class VideoCategoryService {

    private final Logger log = LoggerFactory.getLogger(VideoCategoryService.class);

    private final VideoCategoryRepository videoCategoryRepository;

    private final VideoCategorySearchRepository videoCategorySearchRepository;

    public VideoCategoryService(VideoCategoryRepository videoCategoryRepository, VideoCategorySearchRepository videoCategorySearchRepository) {
        this.videoCategoryRepository = videoCategoryRepository;
        this.videoCategorySearchRepository = videoCategorySearchRepository;
    }

    /**
     * Save a videoCategory.
     *
     * @param videoCategory the entity to save.
     * @return the persisted entity.
     */
    public VideoCategory save(VideoCategory videoCategory) {
        log.debug("Request to save VideoCategory : {}", videoCategory);
        VideoCategory result = videoCategoryRepository.save(videoCategory);
        videoCategorySearchRepository.save(result);
        return result;
    }

    /**
     * Get all the videoCategories.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<VideoCategory> findAll() {
        log.debug("Request to get all VideoCategories");
        return videoCategoryRepository.findAll();
    }


    /**
     * Get one videoCategory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<VideoCategory> findOne(Long id) {
        log.debug("Request to get VideoCategory : {}", id);
        return videoCategoryRepository.findById(id);
    }

    /**
     * Delete the videoCategory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete VideoCategory : {}", id);

        videoCategoryRepository.deleteById(id);
        videoCategorySearchRepository.deleteById(id);
    }

    /**
     * Search for the videoCategory corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<VideoCategory> search(String query) {
        log.debug("Request to search VideoCategories for query {}", query);
        return StreamSupport
            .stream(videoCategorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
