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

package net.devstudy.myphotos.ejb.repository;

import java.util.List;
import net.devstudy.myphotos.model.domain.Photo;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public interface PhotoRepository extends EntityRepository<Photo, Long>{

    List<Photo> findProfilePhotosLatestFirst(Long profileId, int offset, int limit);
    
    int countProfilePhotos(Long profileId);
    
    List<Photo> findAllOrderByViewsDesc(int offset, int limit);
    
    List<Photo> findAllOrderByAuthorRatingDesc(int offset, int limit);
    
    long countAll();
}
