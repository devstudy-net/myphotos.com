/* 
 * Copyright 2017 </>DevStudy.net.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.devstudy.myphotos.model.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@Entity
@Table(catalog = "myphotos", schema = "public")
public class Photo extends AbstractDomain {

    private Long id;

    private String smallUrl;

    private String largeUrl;

    private String originalUrl;

    private long views;

    private long downloads;

    private Profile profile;

    @Id
    @Basic(optional = false)
    @Column(unique = true, nullable = false, updatable = false)
    @SequenceGenerator(name = "photo_generator", sequenceName = "photo_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photo_generator")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @Size(max = 255)
    @Basic(optional = false)
    @Column(name = "small_url", nullable = false, length = 255, updatable = false)
    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    @NotNull
    @Size(max = 255)
    @Basic(optional = false)
    @Column(name = "large_url", nullable = false, length = 255, updatable = false)
    public String getLargeUrl() {
        return largeUrl;
    }

    public void setLargeUrl(String largeUrl) {
        this.largeUrl = largeUrl;
    }

    @NotNull
    @Size(max = 255)
    @Basic(optional = false)
    @Column(name = "original_url", nullable = false, length = 255, updatable = false)
    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    @Min(0)
    @Basic(optional = false)
    @Column(nullable = false)
    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    @Min(0)
    @Basic(optional = false)
    @Column(nullable = false)
    public long getDownloads() {
        return downloads;
    }

    public void setDownloads(long downloads) {
        this.downloads = downloads;
    }

    @NotNull
    @JoinColumn(name = "profile_id", referencedColumnName = "id", nullable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
