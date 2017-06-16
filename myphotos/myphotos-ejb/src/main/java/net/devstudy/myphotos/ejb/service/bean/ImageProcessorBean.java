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

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import net.devstudy.myphotos.common.config.ImageCategory;
import static net.devstudy.myphotos.common.config.ImageCategory.LARGE_PHOTO;
import static net.devstudy.myphotos.common.config.ImageCategory.PROFILE_AVATAR;
import static net.devstudy.myphotos.common.config.ImageCategory.SMALL_PHOTO;
import net.devstudy.myphotos.common.model.TempImageResource;
import net.devstudy.myphotos.ejb.service.ImageResizerService;
import net.devstudy.myphotos.ejb.service.ImageStorageService;
import net.devstudy.myphotos.ejb.service.interceptor.ImageResourceInterceptor;
import net.devstudy.myphotos.model.ImageResource;
import net.devstudy.myphotos.model.domain.Photo;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ImageProcessorBean {

    @Inject
    private ImageResizerService imageResizerService;

    @Inject
    private ImageStorageService imageStorageService;

    @Interceptors({ImageResourceInterceptor.class})
    public String processProfileAvatar(ImageResource imageResource) {
        return createResizedImage(imageResource, PROFILE_AVATAR);
    }

    @Interceptors({ImageResourceInterceptor.class})
    public Photo processPhoto(ImageResource imageResource) {
        Photo photo = new Photo();
        photo.setLargeUrl(createResizedImage(imageResource, LARGE_PHOTO));
        photo.setSmallUrl(createResizedImage(imageResource, SMALL_PHOTO));
        photo.setOriginalUrl(imageStorageService.saveProtectedImage(imageResource.getTempPath()));
        return photo;
    }

    private String createResizedImage(ImageResource imageResource, ImageCategory imageCategory) {
        try (TempImageResource tempPath = new TempImageResource()) {
            imageResizerService.resize(imageResource.getTempPath(), tempPath.getTempPath(), imageCategory);
            return imageStorageService.savePublicImage(imageCategory, tempPath.getTempPath());
        }
    }
}
