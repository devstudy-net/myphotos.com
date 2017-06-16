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
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.devstudy.myphotos.model.Pageable;
import net.devstudy.myphotos.model.domain.Photo;
import net.devstudy.myphotos.model.SortMode;
import net.devstudy.myphotos.service.PhotoService;
import static net.devstudy.myphotos.web.Constants.PHOTO_LIMIT;
import static net.devstudy.myphotos.web.util.RoutingUtils.forwardToFragment;

/**
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@WebServlet(urlPatterns = "/photos/popular/more", loadOnStartup = 1)
public class MorePopularPhotosController extends HttpServlet{

    @EJB
    private PhotoService photoService;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SortMode sortMode = SortMode.of(req.getParameter("sort"));
        int page = Integer.parseInt(req.getParameter("page"));
        List<Photo> photos = photoService.findPopularPhotos(sortMode, new Pageable(page, PHOTO_LIMIT));
        req.setAttribute("photos", photos);
        req.setAttribute("sortMode", sortMode.name().toLowerCase());
        forwardToFragment("more-photos", req, resp); 
    }
}
