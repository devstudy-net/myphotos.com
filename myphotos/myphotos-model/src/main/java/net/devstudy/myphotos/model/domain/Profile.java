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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import net.devstudy.myphotos.model.validation.Email;
import net.devstudy.myphotos.model.validation.EnglishLanguage;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@Entity
@Table(catalog = "myphotos", schema = "public", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"email"}), 
    @UniqueConstraint(columnNames = {"uid"})
})
public class Profile extends AbstractDomain {

    private Long id;

    private String uid;

    private String email;

    private String firstName;

    private String lastName;

    private String avatarUrl;

    private String jobTitle;

    private String location;

    private int photoCount;

    private int rating;

    public Profile() {
    }

    @Id
    @Basic(optional = false)
    @Column(unique = true, nullable = false, updatable = false)
    @SequenceGenerator(name = "profile_generator", sequenceName = "profile_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profile_generator")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    @Size(max = 255)
    @Basic(optional = false)
    @Column(unique = true, nullable = false, length = 255, updatable = false)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Email
    @NotNull
    @Size(max = 100)
    @Basic(optional = false)
    @Column(unique = true, nullable = false, length = 100, updatable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull
    @Size(min = 1, max = 60)
    @EnglishLanguage(withNumbers = false, withSpecialSymbols = false)
    @Basic(optional = false)
    @Column(name = "first_name", nullable = false, length = 60)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotNull
    @Size(min = 1, max = 60)
    @EnglishLanguage(withNumbers = false, withSpecialSymbols = false)
    @Basic(optional = false)
    @Column(name = "last_name", nullable = false, length = 60)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @NotNull
    @Size(max = 255)
    @Basic(optional = false)
    @Column(name = "avatar_url", nullable = false, length = 255)
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    @NotNull
    @Size(min = 5, max = 100)
    @EnglishLanguage(withSpecialSymbols = false)
    @Basic(optional = false)
    @Column(name = "job_title", nullable = false, length = 100)
    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    @NotNull
    @Size(min = 5, max = 100)
    @EnglishLanguage(withSpecialSymbols = false)
    @Basic(optional = false)
    @Column(nullable = false, length = 100)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Min(0)
    @Basic(optional = false)
    @Column(name = "photo_count", nullable = false)
    public int getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }

    @Min(0)
    @Basic(optional = false)
    @Column(nullable = false)
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Transient
    public String getFullName() {
        return String.format("%s %s", getFirstName(), getLastName());
    }
}
