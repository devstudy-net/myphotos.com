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
import java.io.InputStream;
import java.io.OutputStream;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.devstudy.myphotos.model.OriginalImage;
import net.devstudy.myphotos.service.PhotoService;
import net.devstudy.myphotos.web.util.UrlExtractorUtils;
import org.apache.commons.io.IOUtils;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@WebServlet(urlPatterns = "/download/*", loadOnStartup = 1)
public class DownloadPhotoController extends HttpServlet {

    @EJB
    private PhotoService photoService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long photoId = Long.parseLong(UrlExtractorUtils.getPathVariableValue(req.getRequestURI(), "/download/", ".jpg"));
        OriginalImage originalImage = photoService.downloadOriginalImage(photoId);
        
        resp.setHeader("Content-Disposition", "attachment; filename="+originalImage.getName());
        resp.setContentType(getServletContext().getMimeType(originalImage.getName()));
        resp.setContentLengthLong(originalImage.getSize());
        try(InputStream in = originalImage.getIn();
                OutputStream out = resp.getOutputStream()) {
            IOUtils.copyLarge(in, out);
        }
    }
}
