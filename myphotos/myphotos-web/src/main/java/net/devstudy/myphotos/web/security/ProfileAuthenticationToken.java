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
package net.devstudy.myphotos.web.security;

import net.devstudy.myphotos.model.domain.Profile;
import static net.devstudy.myphotos.web.security.SecurityUtils.TEMP_PASS;
import org.apache.shiro.authc.RememberMeAuthenticationToken;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
public class ProfileAuthenticationToken implements RememberMeAuthenticationToken {

    private static final long serialVersionUID = 4351226293244178431L;
    private final ProfileId profileId;

    public ProfileAuthenticationToken(Profile profile) {
        this.profileId = new ProfileId(profile);
    }

    @Override
    public boolean isRememberMe() {
        return false;
    }

    @Override
    public ProfileId getPrincipal() {
        return profileId;
    }

    @Override
    public String getCredentials() {
        return TEMP_PASS;
    }
}
