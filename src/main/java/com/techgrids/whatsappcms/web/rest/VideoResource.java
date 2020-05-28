package com.techgrids.whatsappcms.web.rest;

import com.techgrids.whatsappcms.domain.Video;
import com.techgrids.whatsappcms.service.VideoService;
import com.techgrids.whatsappcms.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
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
 * REST controller for managing {@link com.techgrids.whatsappcms.domain.Video}.
 */
@RestController
@RequestMapping("/api")
public class VideoResource {

    private final Logger log = LoggerFactory.getLogger(VideoResource.class);

    private static final String ENTITY_NAME = "video";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VideoService videoService;

    public VideoResource(VideoService videoService) {
        this.videoService = videoService;
    }

    /**
     * {@code POST  /videos} : Create a new video.
     *
     * @param video the video to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new video, or with status {@code 400 (Bad Request)} if the video has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/videos")
    public ResponseEntity<Video> createVideo(@Valid @RequestBody Video video) throws URISyntaxException {
        log.debug("REST request to save Video : {}", video);
        if (video.getId() != null) {
            throw new BadRequestAlertException("A new video cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Video result = videoService.save(video);
        return ResponseEntity.created(new URI("/api/videos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /videos} : Updates an existing video.
     *
     * @param video the video to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated video,
     * or with status {@code 400 (Bad Request)} if the video is not valid,
     * or with status {@code 500 (Internal Server Error)} if the video couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/videos")
    public ResponseEntity<Video> updateVideo(@Valid @RequestBody Video video) throws URISyntaxException {
        log.debug("REST request to update Video : {}", video);
        if (video.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Video result = videoService.save(video);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, video.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /videos} : get all the videos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of videos in body.
     */
    @GetMapping("/videos")
    public ResponseEntity<List<Video>> getAllVideos(Pageable pageable) {
        log.debug("REST request to get a page of Videos");
        Page<Video> page = videoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /videos/:id} : get the "id" video.
     *
     * @param id the id of the video to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the video, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/videos/{id}")
    public ResponseEntity<Video> getVideo(@PathVariable Long id) {
        log.debug("REST request to get Video : {}", id);
        Optional<Video> video = videoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(video);
    }

    /**
     * {@code DELETE  /videos/:id} : delete the "id" video.
     *
     * @param id the id of the video to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/videos/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        log.debug("REST request to delete Video : {}", id);

        videoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/videos?query=:query} : search for the video corresponding
     * to the query.
     *
     * @param query the query of the video search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/videos")
    public ResponseEntity<List<Video>> searchVideos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Videos for query {}", query);
        Page<Video> page = videoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
