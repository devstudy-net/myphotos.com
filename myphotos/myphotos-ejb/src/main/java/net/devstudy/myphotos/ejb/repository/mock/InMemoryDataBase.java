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

package net.devstudy.myphotos.ejb.repository.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import net.devstudy.myphotos.model.domain.Photo;
import net.devstudy.myphotos.model.domain.Profile;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public final class InMemoryDataBase {
    
    public static final Profile PROFILE;
    
    public static final List<Photo> PHOTOS;
    
    static {
        PROFILE = createProfile();
        PHOTOS = createPhotos(PROFILE);
    }
    
    private static Profile createProfile() {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setUid("richard-hendricks");
        profile.setCreated(new Date());
        profile.setFirstName("Richard");
        profile.setLastName("Hendricks");
        profile.setJobTitle("CEO of Pied Piper");
        profile.setLocation("Los Angeles, California");
        profile.setAvatarUrl("https://devstudy-net.github.io/myphotos-com-test-images/Richard-Hendricks.jpg");
        return profile;
    }
    
    private static List<Photo> createPhotos(Profile profile) {
        Random random = new Random();
        List<Photo> photos = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            Photo photo = new Photo();
            photo.setProfile(profile);
            profile.setPhotoCount(profile.getPhotoCount() + 1);
            String imageUrl = String.format("https://devstudy-net.github.io/myphotos-com-test-images/%s.jpg", i % 6 + 1);
            photo.setSmallUrl(imageUrl);
            photo.setLargeUrl("https://devstudy-net.github.io/myphotos-com-test-images/large.jpg");
            photo.setOriginalUrl(imageUrl);
            photo.setViews(random.nextInt(100) * 10 + 1);
            photo.setDownloads(random.nextInt(20) * 10 + 1);
            photo.setCreated(new Date());
            photos.add(photo);
        }
        return Collections.unmodifiableList(photos);
    }
    
    private InMemoryDataBase() {
    }
}
