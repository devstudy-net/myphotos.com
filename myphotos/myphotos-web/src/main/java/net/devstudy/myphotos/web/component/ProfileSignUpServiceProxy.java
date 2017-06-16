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

package net.devstudy.myphotos.web.component;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import net.devstudy.myphotos.exception.InvalidWorkFlowException;
import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.service.ProfileSignUpService;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@SessionScoped
public class ProfileSignUpServiceProxy implements ProfileSignUpService, Serializable {

    @EJB
    private ProfileSignUpService profileSignUpService;

    @Inject
    private transient Logger logger;

    @Override
    public void createSignUpProfile(Profile profile) {
        validate();
        profileSignUpService.createSignUpProfile(profile);
    }

    @Override
    public Profile getCurrentProfile() {
        validate();
        return profileSignUpService.getCurrentProfile();
    }

    @Override
    public void completeSignUp() {
        validate();
        profileSignUpService.completeSignUp();
        profileSignUpService = null;
    }

    private void validate() {
        if (profileSignUpService == null) {
            throw new InvalidWorkFlowException("Can't use ProfileSignUpService after completeSignUp() or cancel() invokation");
        }
    }

    @PostConstruct
    private void postConstruct() {
        logger.log(Level.FINE, "Created {0} instance: {1}", new Object[]{getClass().getSimpleName(), System.identityHashCode(this)});
    }

    @PreDestroy
    private void preDestroy() {
        logger.log(Level.FINE, "Destroyed {0} instance: {1}", new Object[]{getClass().getSimpleName(), System.identityHashCode(this)});
        // Remove stateful bean if current session destroyed
        if (profileSignUpService != null) {
            profileSignUpService.cancel();
            profileSignUpService = null;
        }
    }

    @Override
    public void cancel() {
        profileSignUpService.cancel();
        profileSignUpService = null;
    }
}

