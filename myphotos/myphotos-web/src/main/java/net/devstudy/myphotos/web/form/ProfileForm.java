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
package net.devstudy.myphotos.web.form;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import net.devstudy.myphotos.common.annotation.group.ProfileUpdateGroup;

import net.devstudy.myphotos.common.annotation.group.SignUpGroup;
import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.model.validation.EnglishLanguage;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
public class ProfileForm {
    
    private String uid;
    
    private String avatarUrl;

    @AssertTrue(message = "{ProfileForm.agree.AssertTrue}", groups = SignUpGroup.class)
    private boolean agree;
    
    @NotNull(message = "{Profile.firstName.NotNull}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @Size(min = 1, max = 60, message = "{Profile.firstName.Size}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @EnglishLanguage(withNumbers = false, withSpecialSymbols = false, groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    private String firstName;
    
    @NotNull(message = "{Profile.lastName.NotNull}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @Size(min = 1, max = 60, message = "{Profile.lastName.Size}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @EnglishLanguage(withNumbers = false, withSpecialSymbols = false, groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    private String lastName;
    
    @NotNull(message = "{Profile.jobTitle.NotNull}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @Size(min = 5, max = 100, message = "{Profile.jobTitle.Size}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @EnglishLanguage(withSpecialSymbols = false, groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    private String jobTitle;
    
    @NotNull(message = "{Profile.location.NotNull}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @Size(min = 5, max = 100, message = "{Profile.location.Size}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @EnglishLanguage(withSpecialSymbols = false, groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    private String location;

    public ProfileForm() {
    }

    public ProfileForm(Profile profile) {
        setUid(profile.getUid());
        setFirstName(profile.getFirstName());
        setLastName(profile.getLastName());
        setJobTitle(profile.getJobTitle());
        setLocation(profile.getLocation());
        setAvatarUrl(profile.getAvatarUrl());
    }

    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    public String getFullName(){
        return String.format("%s %s", getFirstName(), getLastName());
    }
    
    public void copyToProfile(Profile profile) {
        profile.setFirstName(getFirstName());
        profile.setLastName(getLastName());
        profile.setJobTitle(getJobTitle());
        profile.setLocation(getLocation());
    }
}
