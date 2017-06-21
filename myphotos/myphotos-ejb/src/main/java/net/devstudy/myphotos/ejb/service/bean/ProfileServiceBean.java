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

import java.util.List;
import java.util.Optional;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import net.devstudy.myphotos.common.annotation.cdi.Property;
import net.devstudy.myphotos.common.config.ImageCategory;
import net.devstudy.myphotos.common.converter.ModelConverter;
import net.devstudy.myphotos.ejb.model.URLImageResource;
import net.devstudy.myphotos.ejb.repository.ProfileRepository;
import net.devstudy.myphotos.ejb.service.ImageStorageService;
import net.devstudy.myphotos.ejb.service.TranslitConverter;
import net.devstudy.myphotos.ejb.service.bean.scalable.AsyncUploadImageManager;
import net.devstudy.myphotos.ejb.service.impl.ProfileUidServiceManager;
import net.devstudy.myphotos.ejb.service.interceptor.AsyncOperationInterceptor;
import net.devstudy.myphotos.exception.ObjectNotFoundException;
import net.devstudy.myphotos.model.AsyncOperation;
import net.devstudy.myphotos.model.ImageResource;
import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.rmi.model.RemoteProfile;
import net.devstudy.myphotos.rmi.service.ProfileRemoteService;
import net.devstudy.myphotos.service.ProfileService;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Stateless
@LocalBean
@Local(ProfileService.class)
public class ProfileServiceBean implements ProfileService, ProfileRemoteService{
    
    @Inject
    @Property("myphotos.profile.avatar.placeholder.url")
    private String avatarPlaceHolderUrl;
    
    @Inject
    private ProfileRepository profileRepository;
    
    @EJB
    private ImageProcessorBean imageProcessorBean;
    
    @Inject
    private ImageStorageService imageStorageService;
    
    @Inject
    private ProfileUidServiceManager profileUidServiceManager;
    
    @Inject
    private TranslitConverter translitConverter;
    
    @Inject
    private ModelConverter modelConverter;
    
    @EJB
    private AsyncUploadImageManager asyncUploadImageManager;

    @Override
    public Profile findById(Long id) throws ObjectNotFoundException {
        Optional<Profile> profile = profileRepository.findById(id);
        if (!profile.isPresent()) {
            throw new ObjectNotFoundException(String.format("Profile not found by id: %s", id));
        }
        return profile.get();
    }

    @Override
    public RemoteProfile findRemoteById(Long id) throws ObjectNotFoundException {
        Profile profile = findById(id);
        return modelConverter.convert(profile, RemoteProfile.class);
    }

    @Override
    public Profile findByUid(String uid) throws ObjectNotFoundException {
        Optional<Profile> profile = profileRepository.findByUid(uid);
        if (!profile.isPresent()) {
            throw new ObjectNotFoundException(String.format("Profile not found by uid: %s", uid));
        }
        return profile.get();
    }

    @Override
    public Optional<Profile> findByEmail(String email) {
        return profileRepository.findByEmail(email);
    }

    @Override
    public void signUp(Profile profile, boolean uploadProfileAvatar) {
        if(profile.getUid() == null) {
            setProfileUid(profile);
        }
        profileRepository.create(profile);
        if (uploadProfileAvatar && profile.getAvatarUrl() != null) {
            uploadNewAvatar(profile, new URLImageResource(profile.getAvatarUrl()));
        }
    }
    
    private void setProfileUid(Profile profile) {
        List<String> uids = profileUidServiceManager.getProfileUidCandidates(profile.getFirstName(), profile.getLastName());
        List<String> existUids = profileRepository.findUids(uids);
        for (String uid : uids) {
            if (!existUids.contains(uid)) {
                profile.setUid(uid);
                return;
            }
        }

        profile.setUid(profileUidServiceManager.getDefaultUid());
    }

    @Override
    public void translitSocialProfile(Profile profile) {
        profile.setFirstName(profile.getFirstName() != null ? translitConverter.translit(profile.getFirstName()) : null);
        profile.setLastName(profile.getLastName() != null ? translitConverter.translit(profile.getLastName()) : null);
        profile.setJobTitle(profile.getJobTitle() != null ? translitConverter.translit(profile.getJobTitle()) : null);
        profile.setLocation(profile.getLocation() != null ? translitConverter.translit(profile.getLocation()) : null);
    }

    @Override
    public void update(Profile profile) {
        profileRepository.update(profile);
    }

    /*@Override
    @Asynchronous
    @Interceptors(AsyncOperationInterceptor.class)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void uploadNewAvatar(Profile currentProfile, ImageResource imageResource, AsyncOperation<Profile> asyncOperation) {
        try {
            uploadNewAvatar(currentProfile, imageResource);
            asyncOperation.onSuccess(currentProfile);
        } catch (Throwable throwable) {
            setAvatarPlaceHolder(currentProfile);
            asyncOperation.onFailed(throwable);
        }
    }*/
    
    public void uploadNewAvatar(Profile currentProfile, ImageResource imageResource, AsyncOperation<Profile> asyncOperation) {
        asyncUploadImageManager.uploadNewAvatar(currentProfile, imageResource, asyncOperation);
    }   
    
    public void uploadNewAvatar(Profile currentProfile, ImageResource imageResource) {
        String avatarUrl = imageProcessorBean.processProfileAvatar(imageResource);
        ProfileServiceBean.this.saveNewAvatar(currentProfile, avatarUrl);
    }
    
    private void saveNewAvatar(Profile currentProfile, String avatarUrl) {
        currentProfile.setAvatarUrl(avatarUrl);
        profileRepository.update(currentProfile);
    }
    
    public void saveNewAvatar(Long profileId, String avatarUrl) {
        saveNewAvatar(profileRepository.findById(profileId).get(), avatarUrl);
    }
    
    public void setAvatarPlaceHolder(Long profileId) {
        setAvatarPlaceHolder(profileRepository.findById(profileId).get());
    }

    private void setAvatarPlaceHolder(Profile currentProfile) {
        if (currentProfile.getAvatarUrl() == null) {
            currentProfile.setAvatarUrl(avatarPlaceHolderUrl);
            profileRepository.update(currentProfile);
        }
    }
}
