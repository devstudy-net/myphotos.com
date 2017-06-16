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

import java.util.List;
import net.devstudy.myphotos.exception.ObjectNotFoundException;
import net.devstudy.myphotos.model.AsyncOperation;
import net.devstudy.myphotos.model.ImageResource;
import net.devstudy.myphotos.model.OriginalImage;
import net.devstudy.myphotos.model.Pageable;
import net.devstudy.myphotos.model.SortMode;
import net.devstudy.myphotos.model.domain.Photo;
import net.devstudy.myphotos.model.domain.Profile;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface PhotoService {

    List<Photo> findProfilePhotos(Long profileId, Pageable pageable);
    
    List<Photo> findPopularPhotos(SortMode sortMode, Pageable pageable);
    
    long countAllPhotos();
    
    String viewLargePhoto(Long photoId) throws ObjectNotFoundException; 
    
    OriginalImage downloadOriginalImage(Long photoId) throws ObjectNotFoundException; 
    
    void uploadNewPhoto(Profile currentProfile, ImageResource imageResource, AsyncOperation<Photo> asyncOperation);
}
