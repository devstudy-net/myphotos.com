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
package net.devstudy.myphotos.ws.bean;

import static javax.ejb.ConcurrencyManagementType.BEAN;

import java.util.List;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.jws.WebService;

import net.devstudy.myphotos.common.converter.ModelConverter;
import net.devstudy.myphotos.model.Pageable;
import net.devstudy.myphotos.model.domain.Photo;
import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.service.PhotoService;
import net.devstudy.myphotos.service.ProfileService;
import net.devstudy.myphotos.ws.ProfileWebService;
import net.devstudy.myphotos.ws.error.ExceptionMapperInterceptor;
import net.devstudy.myphotos.ws.model.ProfilePhotoSOAP;
import net.devstudy.myphotos.ws.model.ProfilePhotosSOAP;
import net.devstudy.myphotos.ws.model.ProfileSOAP;

/**
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@Singleton
@ConcurrencyManagement(BEAN)
@WebService(portName = "ProfileServicePort",
        serviceName = "ProfileService",
        targetNamespace = "http://soap.myphotos.com/ws/ProfileService?wsdl",
        endpointInterface = "net.devstudy.myphotos.ws.ProfileWebService")
@Interceptors(ExceptionMapperInterceptor.class)
public class ProfileWebServiceBean implements ProfileWebService {

    @EJB
    private ProfileService profileService;

    @EJB
    private PhotoService photoService;

    @Inject
    private ModelConverter modelConverter;

    @Override
    public ProfileSOAP findById(Long id, boolean withPhotos, int limit) {
        Profile profile = profileService.findById(id);
        ProfileSOAP result = modelConverter.convert(profile, ProfileSOAP.class);
        if (withPhotos) {
            List<Photo> photos = photoService.findProfilePhotos(profile.getId(), new Pageable(limit));
            result.setPhotos(modelConverter.convertList(photos, ProfilePhotoSOAP.class));
        }
        return result;
    }

    @Override
    public ProfilePhotosSOAP findProfilePhotos(Long profileId, int page, int limit) {
        List<Photo> photos = photoService.findProfilePhotos(profileId, new Pageable(page, limit));
        ProfilePhotosSOAP result = new ProfilePhotosSOAP();
        result.setPhotos(modelConverter.convertList(photos, ProfilePhotoSOAP.class));
        return result;
    }
}
