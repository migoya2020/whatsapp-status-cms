package com.techgrids.whatsappcms.web.rest;

import com.techgrids.whatsappcms.domain.VideoCategory;
import com.techgrids.whatsappcms.service.VideoCategoryService;
import com.techgrids.whatsappcms.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.techgrids.whatsappcms.domain.VideoCategory}.
 */
@RestController
@RequestMapping("/api")
public class VideoCategoryResource {

    private final Logger log = LoggerFactory.getLogger(VideoCategoryResource.class);

    private static final String ENTITY_NAME = "videoCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VideoCategoryService videoCategoryService;

    public VideoCategoryResource(VideoCategoryService videoCategoryService) {
        this.videoCategoryService = videoCategoryService;
    }

    /**
     * {@code POST  /video-categories} : Create a new videoCategory.
     *
     * @param videoCategory the videoCategory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new videoCategory, or with status {@code 400 (Bad Request)} if the videoCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/video-categories")
    public ResponseEntity<VideoCategory> createVideoCategory(@Valid @RequestBody VideoCategory videoCategory) throws URISyntaxException {
        log.debug("REST request to save VideoCategory : {}", videoCategory);
        if (videoCategory.getId() != null) {
            throw new BadRequestAlertException("A new videoCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VideoCategory result = videoCategoryService.save(videoCategory);
        return ResponseEntity.created(new URI("/api/video-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /video-categories} : Updates an existing videoCategory.
     *
     * @param videoCategory the videoCategory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated videoCategory,
     * or with status {@code 400 (Bad Request)} if the videoCategory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the videoCategory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/video-categories")
    public ResponseEntity<VideoCategory> updateVideoCategory(@Valid @RequestBody VideoCategory videoCategory) throws URISyntaxException {
        log.debug("REST request to update VideoCategory : {}", videoCategory);
        if (videoCategory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VideoCategory result = videoCategoryService.save(videoCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, videoCategory.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /video-categories} : get all the videoCategories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of videoCategories in body.
     */
    @GetMapping("/video-categories")
    public List<VideoCategory> getAllVideoCategories() {
        log.debug("REST request to get all VideoCategories");
        return videoCategoryService.findAll();
    }

    /**
     * {@code GET  /video-categories/:id} : get the "id" videoCategory.
     *
     * @param id the id of the videoCategory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the videoCategory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/video-categories/{id}")
    public ResponseEntity<VideoCategory> getVideoCategory(@PathVariable Long id) {
        log.debug("REST request to get VideoCategory : {}", id);
        Optional<VideoCategory> videoCategory = videoCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(videoCategory);
    }

    /**
     * {@code DELETE  /video-categories/:id} : delete the "id" videoCategory.
     *
     * @param id the id of the videoCategory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/video-categories/{id}")
    public ResponseEntity<Void> deleteVideoCategory(@PathVariable Long id) {
        log.debug("REST request to delete VideoCategory : {}", id);

        videoCategoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/video-categories?query=:query} : search for the videoCategory corresponding
     * to the query.
     *
     * @param query the query of the videoCategory search.
     * @return the result of the search.
     */
    @GetMapping("/_search/video-categories")
    public List<VideoCategory> searchVideoCategories(@RequestParam String query) {
        log.debug("REST request to search VideoCategories for query {}", query);
        return videoCategoryService.search(query);
    }
}
