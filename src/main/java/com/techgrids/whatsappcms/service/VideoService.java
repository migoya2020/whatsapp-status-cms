package com.techgrids.whatsappcms.service;

import com.techgrids.whatsappcms.domain.Video;
import com.techgrids.whatsappcms.repository.VideoRepository;
import com.techgrids.whatsappcms.repository.search.VideoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Video}.
 */
@Service
@Transactional
public class VideoService {

    private final Logger log = LoggerFactory.getLogger(VideoService.class);

    private final VideoRepository videoRepository;

    private final VideoSearchRepository videoSearchRepository;

    public VideoService(VideoRepository videoRepository, VideoSearchRepository videoSearchRepository) {
        this.videoRepository = videoRepository;
        this.videoSearchRepository = videoSearchRepository;
    }

    /**
     * Save a video.
     *
     * @param video the entity to save.
     * @return the persisted entity.
     */
    public Video save(Video video) {
        log.debug("Request to save Video : {}", video);
        Video result = videoRepository.save(video);
        videoSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the videos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Video> findAll(Pageable pageable) {
        log.debug("Request to get all Videos");
        return videoRepository.findAll(pageable);
    }


    /**
     * Get one video by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Video> findOne(Long id) {
        log.debug("Request to get Video : {}", id);
        return videoRepository.findById(id);
    }

    /**
     * Delete the video by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Video : {}", id);

        videoRepository.deleteById(id);
        videoSearchRepository.deleteById(id);
    }

    /**
     * Search for the video corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Video> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Videos for query {}", query);
        return videoSearchRepository.search(queryStringQuery(query), pageable);    }
}
