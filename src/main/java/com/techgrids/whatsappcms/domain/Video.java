package com.techgrids.whatsappcms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import com.techgrids.whatsappcms.domain.enumeration.Tags;

/**
 * A Video.
 */
@Entity
@Table(name = "video")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "video")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3, max = 30)
    @Column(name = "title", length = 30, nullable = false)
    private String title;

    
    @Lob
    @Column(name = "video_url", nullable = false)
    private byte[] videoUrl;

    @Column(name = "video_url_content_type", nullable = false)
    private String videoUrlContentType;

    @Size(min = 5, max = 100)
    @Column(name = "description", length = 100)
    private String description;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @Enumerated(EnumType.STRING)
    @Column(name = "tags")
    private Tags tags;

    @DecimalMin(value = "0")
    @Column(name = "views", precision = 21, scale = 2)
    private BigDecimal views;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Video title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getVideoUrl() {
        return videoUrl;
    }

    public Video videoUrl(byte[] videoUrl) {
        this.videoUrl = videoUrl;
        return this;
    }

    public void setVideoUrl(byte[] videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrlContentType() {
        return videoUrlContentType;
    }

    public Video videoUrlContentType(String videoUrlContentType) {
        this.videoUrlContentType = videoUrlContentType;
        return this;
    }

    public void setVideoUrlContentType(String videoUrlContentType) {
        this.videoUrlContentType = videoUrlContentType;
    }

    public String getDescription() {
        return description;
    }

    public Video description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDate() {
        return date;
    }

    public Video date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Tags getTags() {
        return tags;
    }

    public Video tags(Tags tags) {
        this.tags = tags;
        return this;
    }

    public void setTags(Tags tags) {
        this.tags = tags;
    }

    public BigDecimal getViews() {
        return views;
    }

    public Video views(BigDecimal views) {
        this.views = views;
        return this;
    }

    public void setViews(BigDecimal views) {
        this.views = views;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Video)) {
            return false;
        }
        return id != null && id.equals(((Video) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Video{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", videoUrl='" + getVideoUrl() + "'" +
            ", videoUrlContentType='" + getVideoUrlContentType() + "'" +
            ", description='" + getDescription() + "'" +
            ", date='" + getDate() + "'" +
            ", tags='" + getTags() + "'" +
            ", views=" + getViews() +
            "}";
    }
}
