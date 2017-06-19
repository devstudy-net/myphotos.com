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

package net.devstudy.myphotos.rest.controller;

import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import net.devstudy.myphotos.common.converter.ModelConverter;
import net.devstudy.myphotos.model.Pageable;
import net.devstudy.myphotos.model.domain.Photo;
import net.devstudy.myphotos.model.domain.Profile;
import static net.devstudy.myphotos.rest.Constants.PHOTO_LIMIT;
import net.devstudy.myphotos.service.PhotoService;
import net.devstudy.myphotos.service.ProfileService;
import net.devstudy.myphotos.rest.model.PhotosREST;
import net.devstudy.myphotos.rest.model.ProfilePhotoREST;
import net.devstudy.myphotos.rest.model.ProfileWithPhotosREST;

/**
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@Path("/profile")
@Produces({APPLICATION_JSON})
@RequestScoped
public class PublicProfileController {

    @EJB
    private ProfileService profileService;

    @EJB
    private PhotoService photoService;

    @Inject
    private ModelConverter converter;

    @GET
    @Path("/{id}")
    public ProfileWithPhotosREST findProfile(
            @PathParam("id") Long id,
            @DefaultValue("false") @QueryParam("withPhotos") boolean withPhotos,
            @DefaultValue(PHOTO_LIMIT) @QueryParam("limit") int limit) {
        Profile p = profileService.findById(id);
        ProfileWithPhotosREST profile = converter.convert(p, ProfileWithPhotosREST.class);
        if (withPhotos) {
            List<Photo> photos = photoService.findProfilePhotos(profile.getId(), new Pageable(1, limit));
            profile.setPhotos(converter.convertList(photos, ProfilePhotoREST.class));
        }
        return profile;
    }

    @GET
    @Path("/{profileId}/photos")
    public PhotosREST findProfilePhotos(
            @PathParam("profileId") Long profileId,
            @DefaultValue("1") @QueryParam("page") int page,
            @DefaultValue(PHOTO_LIMIT) @QueryParam("limit") int limit) {
        List<Photo> photos = photoService.findProfilePhotos(profileId, new Pageable(page, limit));
        return new PhotosREST(converter.convertList(photos, ProfilePhotoREST.class));
    }
}
