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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import static net.devstudy.myphotos.rest.StatusMessages.INTERNAL_ERROR;
import static net.devstudy.myphotos.rest.Constants.PHOTO_LIMIT;
import net.devstudy.myphotos.service.PhotoService;
import net.devstudy.myphotos.service.ProfileService;
import net.devstudy.myphotos.rest.model.ErrorMessageREST;
import net.devstudy.myphotos.rest.model.PhotosREST;
import net.devstudy.myphotos.rest.model.ProfilePhotoREST;
import net.devstudy.myphotos.rest.model.ProfileWithPhotosREST;
import static net.devstudy.myphotos.rest.StatusMessages.SERVICE_UNAVAILABLE;

/**
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@Api("profile")
@Path("/profile")
@Produces({APPLICATION_JSON})
@RequestScoped
@ApiResponses({
    @ApiResponse(code = 500, message = INTERNAL_ERROR, response = ErrorMessageREST.class),
    @ApiResponse(code = 502, message = SERVICE_UNAVAILABLE),
    @ApiResponse(code = 503, message = SERVICE_UNAVAILABLE),
    @ApiResponse(code = 504, message = SERVICE_UNAVAILABLE)
})
public class PublicProfileController {

    @EJB
    private ProfileService profileService;

    @EJB
    private PhotoService photoService;

    @Inject
    private ModelConverter converter;

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Finds profile by id",
            notes = "withPhotos and limit are optional. If withPhotos=true, this method returns profile with his photos, limited by limit parameter")
    @ApiResponses({
        @ApiResponse(code = 404, message = "Profile not found by id", response = ErrorMessageREST.class)
    })
    public ProfileWithPhotosREST findProfile(
            @ApiParam(value = "Profile number id", required = true)
            @PathParam("id") Long id,
            @ApiParam(value = "Flag, which adds or not profile photos to result")
            @DefaultValue("false") @QueryParam("withPhotos") boolean withPhotos,
            @ApiParam(value = "Photos limit")
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
    @ApiOperation(value = "Finds profile photos",
            notes = "If profile not found by profileId, this method return empty list with status code 200")
    public PhotosREST findProfilePhotos(
            @ApiParam(value = "Profile number id", required = true)
            @PathParam("profileId") Long profileId,
            @ApiParam(value = "Pagination page (this parameter should start with 1)")
            @DefaultValue("1") @QueryParam("page") int page,
            @ApiParam(value = "Photos limit")
            @DefaultValue(PHOTO_LIMIT) @QueryParam("limit") int limit) {
        List<Photo> photos = photoService.findProfilePhotos(profileId, new Pageable(page, limit));
        return new PhotosREST(converter.convertList(photos, ProfilePhotoREST.class));
    }
}
