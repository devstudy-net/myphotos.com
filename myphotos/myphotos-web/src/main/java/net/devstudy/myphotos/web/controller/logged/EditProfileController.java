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

import static net.devstudy.myphotos.web.util.RoutingUtils.forwardToPage;

import java.io.IOException;
import javax.ejb.EJB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.service.ProfileService;
import net.devstudy.myphotos.web.form.ProfileForm;
import net.devstudy.myphotos.web.security.SecurityUtils;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@WebServlet(urlPatterns = "/edit", loadOnStartup = 1)
public class EditProfileController extends HttpServlet {	

    @EJB
    private ProfileService profileService;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Profile profile = profileService.findById(SecurityUtils.getCurrentProfileId().getId());
        req.setAttribute("profile", new ProfileForm(profile));
        forwardToPage("edit", req, resp);
    }
}
