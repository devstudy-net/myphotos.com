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
package net.devstudy.myphotos.web.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.devstudy.myphotos.model.Pageable;
import net.devstudy.myphotos.model.SortMode;
import net.devstudy.myphotos.model.domain.Photo;
import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.service.PhotoService;
import net.devstudy.myphotos.service.ProfileService;
import static net.devstudy.myphotos.web.Constants.PHOTO_LIMIT;
import net.devstudy.myphotos.web.security.SecurityUtils;
import static net.devstudy.myphotos.web.util.RoutingUtils.forwardToPage;
import static net.devstudy.myphotos.web.util.RoutingUtils.redirectToUrl;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@WebServlet(urlPatterns = "/", loadOnStartup = 1)
public class ProfileController extends HttpServlet {

    private final List<String> homeUrls;

    @EJB
    private ProfileService profileService;

    @EJB
    private PhotoService photoService;

    public ProfileController() {
        this.homeUrls = Collections.unmodifiableList(Arrays.asList("/"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURI();
        if (isHomeUrl(url)) {
            if (SecurityUtils.isTempAuthenticated()) {
                redirectToUrl("/sign-up", req, resp);
            } else {
                handleHomeRequest(req, resp);
            }
        } else {
            handleProfileRequest(req, resp);
        }
    }

    private boolean isHomeUrl(String url) {
        return homeUrls.contains(url);
    }

    private void handleHomeRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        SortMode sortMode = getSortMode(req);
        List<Photo> photos = photoService.findPopularPhotos(sortMode, new Pageable(1, PHOTO_LIMIT));
        req.setAttribute("photos", photos);
        long totalCount = photoService.countAllPhotos();
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("sortMode", sortMode.name().toLowerCase());
        if (SecurityUtils.isAuthenticated()) {
            req.setAttribute("profile", profileService.findByUid(SecurityUtils.getCurrentProfileId().getUid()));
        }
        forwardToPage("home", req, resp);
    }

    private SortMode getSortMode(HttpServletRequest req) {
        Optional<String> sortMode = Optional.ofNullable(req.getParameter("sort"));
        return sortMode.isPresent() ? SortMode.of(sortMode.get()) : SortMode.POPULAR_PHOTO;
    }

    private void handleProfileRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uid = req.getRequestURI().substring(1);
        Profile profile = profileService.findByUid(uid);
        req.setAttribute("profile", profile);
        req.setAttribute("profilePhotos", Boolean.TRUE);
        List<Photo> photos = photoService.findProfilePhotos(profile.getId(), new Pageable(1, PHOTO_LIMIT));
        req.setAttribute("photos", photos);
        forwardToPage("profile", req, resp);
    }
}
