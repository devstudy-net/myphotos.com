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

import static java.lang.String.format;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import net.devstudy.myphotos.ejb.repository.PhotoRepository;
import net.devstudy.myphotos.ejb.repository.ProfileRepository;
import net.devstudy.myphotos.ejb.service.ImageStorageService;
import net.devstudy.myphotos.ejb.service.bean.scalable.AsyncUploadImageManager;
import net.devstudy.myphotos.ejb.service.interceptor.AsyncOperationInterceptor;
import net.devstudy.myphotos.exception.ObjectNotFoundException;
import net.devstudy.myphotos.exception.ValidationException;
import net.devstudy.myphotos.model.AsyncOperation;
import net.devstudy.myphotos.model.ImageResource;
import net.devstudy.myphotos.model.OriginalImage;
import net.devstudy.myphotos.model.Pageable;
import net.devstudy.myphotos.model.SortMode;
import net.devstudy.myphotos.model.domain.Photo;
import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.service.PhotoService;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Stateless
@LocalBean
@Local(PhotoService.class)
public class PhotoServiceBean implements PhotoService {
    
    @Inject
    private PhotoRepository photoRepository;

    @Inject
    private ProfileRepository profileRepository;
    
    @Inject
    private ImageStorageService imageStorageService;
    
    @EJB
    private ImageProcessorBean imageProcessorBean;
    
    @Resource
    private SessionContext sessionContext;
    
    @EJB
    private AsyncUploadImageManager asyncUploadImageManager;

    @Override
    public List<Photo> findProfilePhotos(Long profileId, Pageable pageable) {
        return photoRepository.findProfilePhotosLatestFirst(profileId, pageable.getOffset(), pageable.getLimit());
    }

    @Override
    public List<Photo> findPopularPhotos(SortMode sortMode, Pageable pageable) {
        switch (sortMode) {
            case POPULAR_AUTHOR:
                return photoRepository.findAllOrderByAuthorRatingDesc(pageable.getOffset(), pageable.getLimit());
            case POPULAR_PHOTO:
                return photoRepository.findAllOrderByViewsDesc(pageable.getOffset(), pageable.getLimit());
            default:
                throw new ValidationException("Unsupported sort mode: " + sortMode);
        }
    }

    @Override
    public long countAllPhotos() {
        return photoRepository.countAll();
    }

    @Override
    public String viewLargePhoto(Long photoId) throws ObjectNotFoundException {
        Photo photo = getPhoto(photoId);
        photo.setViews(photo.getViews() + 1);
        photoRepository.update(photo);
        return photo.getLargeUrl();
    }
    
    public Photo getPhoto(Long photoId) throws ObjectNotFoundException {
        Optional<Photo> photo = photoRepository.findById(photoId);
        if (!photo.isPresent()) {
            throw new ObjectNotFoundException(format("Photo not found by id: %s", photoId));
        }
        return photo.get();
    }

    @Override
    public OriginalImage downloadOriginalImage(Long photoId) throws ObjectNotFoundException {
        Photo photo = getPhoto(photoId);
        photo.setDownloads(photo.getDownloads() + 1);
        photoRepository.update(photo);
        
        return imageStorageService.getOriginalImage(photo.getOriginalUrl());
    }

    /*@Override
    @Asynchronous
    @Interceptors(AsyncOperationInterceptor.class)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void uploadNewPhoto(Profile currentProfile, ImageResource imageResource, AsyncOperation<Photo> asyncOperation) {
        try {
            Photo photo = uploadNewPhoto(currentProfile, imageResource);
            asyncOperation.onSuccess(photo);
        } catch (Throwable throwable) {
            sessionContext.setRollbackOnly();
            asyncOperation.onFailed(throwable);
        }
    }*/
    
    public void uploadNewPhoto(Profile currentProfile, ImageResource imageResource, AsyncOperation<Photo> asyncOperation) {
        asyncUploadImageManager.uploadNewPhoto(currentProfile, imageResource, asyncOperation);
    }

    public Photo uploadNewPhoto(Profile currentProfile, ImageResource imageResource){
        Photo photo = imageProcessorBean.processPhoto(imageResource);
        createNewPhoto(currentProfile, photo);
        return photo;
    }
    
    public void createNewPhoto(Long profileId, Photo photo){
        createNewPhoto(profileRepository.findById(profileId).get(), photo);
    }
    
    private void createNewPhoto(Profile currentProfile, Photo photo){
        photo.setProfile(currentProfile);
        photoRepository.create(photo);
        photoRepository.flush();
        currentProfile.setPhotoCount(photoRepository.countProfilePhotos(currentProfile.getId()));
        profileRepository.update(currentProfile);
    }
}
