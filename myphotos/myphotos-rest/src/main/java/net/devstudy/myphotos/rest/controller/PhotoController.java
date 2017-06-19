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
import static javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import javax.ws.rs.core.Response;
import net.devstudy.myphotos.common.converter.ModelConverter;
import net.devstudy.myphotos.common.converter.UrlConveter;
import net.devstudy.myphotos.model.OriginalImage;
import net.devstudy.myphotos.model.Pageable;
import net.devstudy.myphotos.model.SortMode;
import net.devstudy.myphotos.model.domain.Photo;
import static net.devstudy.myphotos.rest.StatusMessages.INTERNAL_ERROR;
import static net.devstudy.myphotos.rest.Constants.PHOTO_LIMIT;
import net.devstudy.myphotos.rest.model.ErrorMessageREST;
import net.devstudy.myphotos.rest.model.ImageLinkREST;
import net.devstudy.myphotos.rest.model.PhotoREST;
import net.devstudy.myphotos.rest.model.PhotosREST;
import net.devstudy.myphotos.service.PhotoService;
import static net.devstudy.myphotos.rest.StatusMessages.SERVICE_UNAVAILABLE;

/**
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@Api("photo")
@Path("/photo")
@Produces({APPLICATION_JSON})
@RequestScoped
@ApiResponses({
    @ApiResponse(code = 500, message = INTERNAL_ERROR, response = ErrorMessageREST.class),
    @ApiResponse(code = 502, message = SERVICE_UNAVAILABLE),
    @ApiResponse(code = 503, message = SERVICE_UNAVAILABLE),
    @ApiResponse(code = 504, message = SERVICE_UNAVAILABLE)
})
public class PhotoController {

    @EJB
    private PhotoService photoService;

    @Inject
    private UrlConveter urlConveter;

    @Inject
    private ModelConverter converter;

    @GET
    @Path("/all")
    @ApiOperation(value = "Gets popular photos according to given sortMode",
            notes = "If you need total photo count set withTotal=true")
    public PhotosREST findAllPhotos(
            @ApiParam(value = "Flag, which indicates to calculate or not total photo count")
            @DefaultValue("false") @QueryParam("withTotal") boolean withTotal,
            @ApiParam(value = "Photos sort mode", allowableValues = "popular_photo,popular_author")
            @DefaultValue("popular_photo") @QueryParam("sortMode") String sortMode,
            @ApiParam(value = "Pagination page (this parameter should start with 1)")
            @DefaultValue("1") @QueryParam("page") int page,
            @ApiParam(value = "Photos limit")
            @DefaultValue(PHOTO_LIMIT) @QueryParam("limit") int limit) {
        List<Photo> photos = photoService.findPopularPhotos(SortMode.of(sortMode), new Pageable(page, limit));
        PhotosREST result = new PhotosREST();
        result.setPhotos(converter.convertList(photos, PhotoREST.class));
        if (withTotal) {
            result.setTotal(photoService.countAllPhotos());
        }
        return result;
    }

    @GET
    @Path("/preview/{id}")
    @ApiOperation(value = "Gets large photo url by id",
            notes = "FYI: This method increments view count for given photo")
    @ApiResponses({
        @ApiResponse(code = 404, message = "Photo not found by id", response = ErrorMessageREST.class)
    })
    public ImageLinkREST viewLargePhoto(
            @ApiParam(value = "Photo number id", required = true)
            @PathParam("id") Long photoId) {
        String relativeUrl = photoService.viewLargePhoto(photoId);
        String absoluteUrl = urlConveter.convert(relativeUrl);
        return new ImageLinkREST(absoluteUrl);
    }

    @GET
    @Path("/download/{id}")
    @Produces(APPLICATION_OCTET_STREAM)
    @ApiOperation(value = "Downloads original photo by id",
            notes = "FYI: This method increments download count for given photo")
    @ApiResponses({
        @ApiResponse(code = 404, message = "Photo not found by id", response = ErrorMessageREST.class)
    })
    public Response downloadOriginalImage(
            @ApiParam(value = "Photo number id", required = true)
            @PathParam("id") Long photoId) {
        OriginalImage originalImage = photoService.downloadOriginalImage(photoId);
        Response.ResponseBuilder builder = Response.ok(originalImage.getIn(), APPLICATION_OCTET_STREAM);
        builder.header("Content-Disposition", "attachment; filename=" + originalImage.getName());
        return builder.build();
    }
}
