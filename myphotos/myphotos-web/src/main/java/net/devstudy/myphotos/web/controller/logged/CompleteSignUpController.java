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

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;

import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.web.component.ProfileSignUpServiceProxy;
import net.devstudy.myphotos.common.annotation.group.SignUpGroup;
import net.devstudy.myphotos.web.security.SecurityUtils;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@WebServlet(urlPatterns = "/sign-up/complete", loadOnStartup = 1)
public class CompleteSignUpController extends AbstractProfileSaveController {

    @Inject
    private ProfileSignUpServiceProxy profileSignUpService;

    @Override
    protected Class<?>[] getValidationGroups() {
        return new Class<?>[] {SignUpGroup.class};
    }

    @Override
    protected String getBackToEditView() {
        return "sign-up";
    }

    @Override
    protected Profile getCurrentProfile() {
        return profileSignUpService.getCurrentProfile();
    }

    @Override
    protected void saveProfile(Profile profile) {
        profileSignUpService.completeSignUp();
        reloginWithUserRole(profile);
    }

    private void reloginWithUserRole(Profile profile) {
        SecurityUtils.logout();
        SecurityUtils.authentificate(profile);
    }
}
