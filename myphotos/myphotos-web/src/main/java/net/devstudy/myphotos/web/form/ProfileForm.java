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
public class ProfileForm extends Profile {

    private boolean agree;

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

    @AssertTrue(message = "{ProfileForm.agree.AssertTrue}", groups = SignUpGroup.class)
    public boolean isAgree() {
        return agree;
    }

    public void setAgree(boolean agree) {
        this.agree = agree;
    }

    @NotNull(message = "{Profile.firstName.NotNull}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @Size(min = 1, max = 60, message = "{Profile.firstName.Size}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @EnglishLanguage(withNumbers = false, withSpecialSymbols = false, groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    public String getFirstName() {
        return super.getFirstName();
    }

    @NotNull(message = "{Profile.lastName.NotNull}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @Size(min = 1, max = 60, message = "{Profile.lastName.Size}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @EnglishLanguage(withNumbers = false, withSpecialSymbols = false, groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    public String getLastName() {
        return super.getLastName();
    }

    @NotNull(message = "{Profile.jobTitle.NotNull}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @Size(min = 5, max = 100, message = "{Profile.jobTitle.Size}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @EnglishLanguage(withSpecialSymbols = false, groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    public String getJobTitle() {
        return super.getJobTitle();
    }

    @NotNull(message = "{Profile.location.NotNull}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @Size(min = 5, max = 100, message = "{Profile.location.Size}", groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    @EnglishLanguage(withSpecialSymbols = false, groups = {SignUpGroup.class, ProfileUpdateGroup.class})
    public String getLocation() {
        return super.getLocation();
    }

    public void copyToProfile(Profile profile) {
        profile.setFirstName(getFirstName());
        profile.setLastName(getLastName());
        profile.setJobTitle(getJobTitle());
        profile.setLocation(getLocation());
    }
}
