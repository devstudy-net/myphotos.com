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

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.devstudy.myphotos.common.annotation.cdi.Property;
import net.devstudy.myphotos.common.annotation.qualifier.GooglePlus;
import net.devstudy.myphotos.exception.RetrieveSocialDataFailedException;
import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.service.SocialService;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@GooglePlus
@ApplicationScoped
public class GooglePlusSocialService implements SocialService {

    @Inject
    @Property("myphotos.social.google-plus.client-id")
    private String clientId;

    private List<String> issuers;

    @Inject
    public void setIssuers(@Property("myphotos.social.google-plus.issuers") String issuers) {
        this.issuers = Collections.unmodifiableList(Arrays.asList(issuers.split(",")));
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public Profile fetchProfile(String code) throws RetrieveSocialDataFailedException {
        try {
            return createProfile(fetch(code));
        } catch (GeneralSecurityException | IOException | RuntimeException e) {
            if (e instanceof RetrieveSocialDataFailedException) {
                throw (RetrieveSocialDataFailedException) e;
            } else {
                throw new RetrieveSocialDataFailedException("Can't fetch user from google plus: " + e.getMessage(), e);
            }
        }
    }

    private GoogleIdToken.Payload fetch(String code) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singleton(clientId))
                .setIssuers(issuers).build();
        Optional<GoogleIdToken> idToken = Optional.ofNullable(verifier.verify(code));
        if (idToken.isPresent()) {
            return idToken.get().getPayload();
        } else {
            throw new RetrieveSocialDataFailedException("Can't get account by authToken: " + code);
        }
    }

    private Profile createProfile(GoogleIdToken.Payload user) {
        Profile profile = new Profile();
        profile.setEmail(user.getEmail());
        profile.setFirstName((String) user.get("given_name"));
        profile.setLastName((String) user.get("family_name"));
        profile.setAvatarUrl((String) user.get("picture"));
        return profile;
    }
}

