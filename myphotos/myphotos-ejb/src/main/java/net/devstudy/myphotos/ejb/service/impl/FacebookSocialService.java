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

package net.devstudy.myphotos.ejb.service.impl;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.scope.ExtendedPermissions;
import com.restfb.scope.ScopeBuilder;
import com.restfb.types.User;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.devstudy.myphotos.common.annotation.cdi.Property;
import net.devstudy.myphotos.common.annotation.qualifier.Facebook;
import net.devstudy.myphotos.exception.RetrieveSocialDataFailedException;
import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.service.SocialService;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Facebook
@ApplicationScoped
public class FacebookSocialService implements SocialService{
    
    @Inject
    @Property("myphotos.social.facebook.client-id")
    private String clientId;
    
    @Inject
    @Property("myphotos.social.facebook.client-secret")
    private String secret;
    
    private String redirectUrl;
    
    @Inject
    public void setHost(@Property("myphotos.host") String host) {
        this.redirectUrl = host + "/from/facebook";
    }
    
    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public Profile fetchProfile(String code) throws RetrieveSocialDataFailedException {
        try {
            return createProfile(fetch(code));
        } catch(RuntimeException e) {
            throw new RetrieveSocialDataFailedException("Can't fetch user from facebook: "+e.getMessage(), e);
        }
    }
    
    private User fetch(String code) {
        FacebookClient client = new DefaultFacebookClient(Version.LATEST);
        FacebookClient.AccessToken accessToken = client.obtainUserAccessToken(clientId, secret, redirectUrl, code);
        client = new DefaultFacebookClient(accessToken.getAccessToken(), Version.LATEST);
        return client.fetchObject("me", User.class, Parameter.with("fields", "id,email,first_name,last_name"));
    }

    private Profile createProfile(User user) {
        Profile profile = new Profile();
        profile.setEmail(user.getEmail());
        profile.setFirstName(user.getFirstName());
        profile.setLastName(user.getLastName());
        profile.setAvatarUrl(String.format("https://graph.facebook.com/v2.7/%s/picture?type=large", user.getId()));
        return profile;
    }

    @Override
    public String getAuthorizeUrl() {
        ScopeBuilder scopeBuilder = new ScopeBuilder();
        scopeBuilder.addPermission(ExtendedPermissions.EMAIL);
        FacebookClient client = new DefaultFacebookClient(Version.LATEST);
        return client.getLoginDialogUrl(clientId, redirectUrl, scopeBuilder);
    }
}

