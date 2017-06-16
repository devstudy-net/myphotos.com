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

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.inject.Inject;
import net.devstudy.myphotos.ejb.repository.AccessTokenRepository;
import net.devstudy.myphotos.exception.AccessForbiddenException;
import net.devstudy.myphotos.exception.InvalidAccessTokenException;
import net.devstudy.myphotos.model.domain.AccessToken;
import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.service.AccessTokenService;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Stateless
@Local(AccessTokenService.class)
public class AccessTokenServiceBean implements AccessTokenService{

    @Inject
    private Logger logger;
    
    @Inject
    private AccessTokenRepository accessTokenRepository;

    @Override
    public AccessToken generateAccessToken(Profile profile) {
        AccessToken accessToken = new AccessToken();
        accessToken.setProfile(profile);
        accessTokenRepository.create(accessToken);
        return accessToken;
    }

    @Override
    public Profile findProfile(String token, Long profileId){
        Optional<AccessToken> accessTokenOptional = accessTokenRepository.findByToken(token);
        if(!accessTokenOptional.isPresent()) {
            throw new InvalidAccessTokenException(String.format("Access token %s invalid", token));
        }
        Profile profile = accessTokenOptional.get().getProfile();
        if(!profile.getId().equals(profileId)) {
            throw new AccessForbiddenException(String.format("Access forbidden for token=%s and profileId=%s", token, profileId));
        }
        return profile;
    }

    @Override
    public void invalidateAccessToken(String token) {
        boolean removed = accessTokenRepository.removeAccessToken(token);
        if(!removed) {
            logger.log(Level.WARNING, "Access token {0} not found", token);
            throw new InvalidAccessTokenException("Access token not found");
        }
    }

}
