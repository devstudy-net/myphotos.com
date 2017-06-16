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

import java.util.Optional;
import net.devstudy.myphotos.exception.ObjectNotFoundException;
import net.devstudy.myphotos.model.AsyncOperation;
import net.devstudy.myphotos.model.ImageResource;
import net.devstudy.myphotos.model.domain.Profile;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface ProfileService {

    Profile findById(Long id) throws ObjectNotFoundException;
    
    Profile findByUid(String uid) throws ObjectNotFoundException;
    
    Optional<Profile> findByEmail(String email);
    
    void signUp(Profile profile, boolean uploadProfileAvatar);
    
    void translitSocialProfile(Profile profile);
    
    void update(Profile profile);
    
    void uploadNewAvatar(Profile currentProfile, ImageResource imageResource, AsyncOperation<Profile> asyncOperation);
}
