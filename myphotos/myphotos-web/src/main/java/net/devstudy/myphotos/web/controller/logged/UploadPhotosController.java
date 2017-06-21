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

package net.devstudy.myphotos.web.controller.logged;

import static net.devstudy.myphotos.common.config.Constants.MAX_UPLOADED_PHOTO_SIZE_IN_BYTES;
import static net.devstudy.myphotos.web.Constants.PHOTO_LIMIT;
import static net.devstudy.myphotos.web.util.RoutingUtils.forwardToPage;

import java.io.IOException;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.devstudy.myphotos.model.AsyncOperation;
import net.devstudy.myphotos.model.Pageable;
import net.devstudy.myphotos.model.domain.Photo;
import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.service.PhotoService;
import net.devstudy.myphotos.web.model.PartImageResource;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@WebServlet(urlPatterns = "/upload-photos", asyncSupported = true, loadOnStartup = 1)
@MultipartConfig(maxFileSize = MAX_UPLOADED_PHOTO_SIZE_IN_BYTES)
public class UploadPhotosController extends AbstractUploadController<Photo> {

    @Inject
    private PhotoService photoService;	

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Profile profile = getCurrentProfile();
        List<Photo> photos = photoService.findProfilePhotos(profile.getId(), new Pageable(1, PHOTO_LIMIT - 1));
        req.setAttribute("profile", profile);
        req.setAttribute("photos", photos);
        req.setAttribute("profilePhotos", Boolean.TRUE);
        forwardToPage("upload-photos", req, resp);
    }

    @Override
    protected void uploadImage(Profile profile, PartImageResource partImageResource, AsyncOperation<Photo> asyncOperation) {
        photoService.uploadNewPhoto(profile, partImageResource, asyncOperation);
    }

    @Override
    protected Map<String, String> getResultMap(Photo photo, HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        map.put("smallUrl", photo.getSmallUrl());
        map.put("largeUrl", String.format("/preview/%s.jpg", photo.getId()));
        map.put("originalUrl", String.format("/download/%s.jpg", photo.getId()));
        map.put("views", String.valueOf(photo.getViews()));
        map.put("downloads", String.valueOf(photo.getDownloads()));
        map.put("created", DateFormat.getDateInstance(DateFormat.SHORT, request.getLocale()).format(photo.getCreated()));
        return map;
    }
}
