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

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;

import net.devstudy.myphotos.common.annotation.group.ProfileUpdateGroup;
import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.service.ProfileService;
import net.devstudy.myphotos.web.security.SecurityUtils;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@WebServlet(urlPatterns = "/save", loadOnStartup = 1)
public class SaveProfileController extends AbstractProfileSaveController {

    @EJB
    private ProfileService profileService;

    @Override
    protected Class<?>[] getValidationGroups() {
        return new Class<?>[] {ProfileUpdateGroup.class};
    }

    @Override
    protected String getBackToEditView() {
        return "edit";
    }

    @Override
    protected Profile getCurrentProfile() {
        return profileService.findById(SecurityUtils.getCurrentProfileId().getId());
    }

    @Override
    protected void saveProfile(Profile profile) {
        profileService.update(profile);
    }
}
