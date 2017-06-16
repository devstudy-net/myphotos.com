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

package net.devstudy.myphotos.generator.component;

import java.util.List;
import java.util.Random;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import net.devstudy.myphotos.ejb.repository.PhotoRepository;
import net.devstudy.myphotos.model.domain.Photo;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@ApplicationScoped
public class UpdatePhotoService {

    @Inject
    private PhotoRepository photoRepository;
    
    private Random random = new Random();
    
    @Transactional
    public void updatePhotos(List<Photo> photos) {
        for(Photo photo : photos) {
            photo.setDownloads(random.nextInt(100));
            photo.setViews(random.nextInt(1000) * 5 + 100);
            photoRepository.update(photo);
        }
    }
}
