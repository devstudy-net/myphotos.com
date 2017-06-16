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
package net.devstudy.myphotos.web.controller.social;

import java.io.IOException;
import java.util.Optional;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.service.ProfileService;
import net.devstudy.myphotos.service.SocialService;
import net.devstudy.myphotos.web.component.ProfileSignUpServiceProxy;
import net.devstudy.myphotos.web.security.SecurityUtils;
import static net.devstudy.myphotos.web.util.RoutingUtils.redirectToUrl;
import static net.devstudy.myphotos.web.util.RoutingUtils.redirectToValidAuthUrl;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
public abstract class AbstractSignUpController extends HttpServlet {

    @EJB
    protected ProfileService profileService;

    @Inject
    protected ProfileSignUpServiceProxy profileSignUpService;

    protected SocialService socialService;

    protected abstract void setSocialService(SocialService socialService);

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SecurityUtils.isAuthenticated()) {
            redirectToValidAuthUrl(req, resp);
        } else {
            Optional<String> code = Optional.ofNullable(req.getParameter("code"));
            if (code.isPresent()) {
                processSignUp(code.get(), req, resp);
            } else {
                redirectToUrl("/", req, resp);
            }
        }
    }

    private void processSignUp(String code, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Profile signUpProfile = socialService.fetchProfile(code);
        Optional<Profile> profileOptional = profileService.findByEmail(signUpProfile.getEmail());
        if (profileOptional.isPresent()) {
            Profile profile = profileOptional.get();
            SecurityUtils.authentificate(profile);
            redirectToUrl("/" + profile.getUid(), req, resp);
        } else {
            SecurityUtils.authentificate();
            profileSignUpService.createSignUpProfile(signUpProfile);
            redirectToUrl("/sign-up", req, resp);
        }
    }
}
