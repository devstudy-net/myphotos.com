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
package net.devstudy.myphotos.ejb.service.bean;

import java.io.Serializable;
import static java.util.concurrent.TimeUnit.MINUTES;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;
import javax.inject.Inject;
import static net.devstudy.myphotos.common.config.Constants.DEFAULT_ASYNC_OPERATION_TIMEOUT_IN_MILLIS;
import net.devstudy.myphotos.ejb.model.URLImageResource;
import net.devstudy.myphotos.exception.ObjectNotFoundException;
import net.devstudy.myphotos.model.AsyncOperation;
import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.service.ProfileService;
import net.devstudy.myphotos.service.ProfileSignUpService;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@Stateful
@StatefulTimeout(value = 30, unit = MINUTES)
public class ProfileSignUpServiceBean implements ProfileSignUpService, Serializable {

    @Inject
    private transient Logger logger;

    @Inject
    private transient ProfileService profileService;

    private Profile profile;

    @Override
    public void createSignUpProfile(Profile profile) {
        this.profile = profile;
        profileService.translitSocialProfile(profile);
    }

    @Override
    public Profile getCurrentProfile() throws ObjectNotFoundException {
        if (profile == null) {
            throw new ObjectNotFoundException("Profile not found. Please create profile before use");
        }
        return profile;
    }

    @Override
    @Remove
    public void completeSignUp() {
        profileService.signUp(profile, false);
        if (profile.getAvatarUrl() != null) {
            profileService.uploadNewAvatar(profile, new URLImageResource(profile.getAvatarUrl()), new AsyncOperation<Profile>() {

                @Override
                public void onSuccess(Profile result) {
                    logger.log(Level.INFO, "Profile avatar successful saved to {0}", result.getAvatarUrl());
                }

                @Override
                public void onFailed(Throwable throwable) {
                    logger.log(Level.SEVERE, "Profile avatar can't saved: " + throwable.getMessage(), throwable);
                }

                @Override
                public long getTimeOutInMillis() {
                    return 0;//Infinity
                }
            });
        }
    }

    @Override
    @Remove
    public void cancel() {
        profile = null;
    }

    @PostConstruct
    private void postConstruct() {
        logger.log(Level.FINE, "Created {0} instance: {1}", new Object[]{getClass().getSimpleName(), System.identityHashCode(this)});
    }

    @PreDestroy
    private void preDestroy() {
        logger.log(Level.FINE, "Destroyed {0} instance: {1}", new Object[]{getClass().getSimpleName(), System.identityHashCode(this)});
    }

    @PostActivate
    private void postActivate() {
        logger.log(Level.FINE, "Activated {0} instance: {1}", new Object[]{getClass().getSimpleName(), System.identityHashCode(this)});
    }

    @PrePassivate
    private void prePassivate() {
        logger.log(Level.FINE, "Passivated {0} instance: {1}", new Object[]{getClass().getSimpleName(), System.identityHashCode(this)});
    }
}
