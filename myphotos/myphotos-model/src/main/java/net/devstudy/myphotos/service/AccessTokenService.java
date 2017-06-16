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
package net.devstudy.myphotos.service;

import net.devstudy.myphotos.exception.AccessForbiddenException;
import net.devstudy.myphotos.exception.InvalidAccessTokenException;
import net.devstudy.myphotos.model.domain.AccessToken;
import net.devstudy.myphotos.model.domain.Profile;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface AccessTokenService {

    AccessToken generateAccessToken(Profile profile);
    
    Profile findProfile(String token, Long profileId) throws AccessForbiddenException, InvalidAccessTokenException;
    
    void invalidateAccessToken(String token) throws InvalidAccessTokenException;
}
