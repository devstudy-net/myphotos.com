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

import java.io.IOException;
import java.util.Set;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.web.component.ConstraintViolationConverter;
import net.devstudy.myphotos.web.component.FormReader;
import net.devstudy.myphotos.web.form.ProfileForm;
import net.devstudy.myphotos.web.util.RoutingUtils;
import static net.devstudy.myphotos.web.util.RoutingUtils.forwardToPage;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public abstract class AbstractProfileSaveController extends HttpServlet{

    @Resource(lookup = "java:comp/Validator")
    private Validator validator;
    
    @Inject
    private ConstraintViolationConverter constraintViolationConvertor;
    
    @Inject
    private FormReader formReader;

    @Override
    protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp); 
    }
    
    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProfileForm profileForm = formReader.readForm(req, ProfileForm.class);
        Set<ConstraintViolation<ProfileForm>> violations = validator.validate(profileForm, getValidationGroups());
        if(violations.isEmpty()) {
            saveChanges(profileForm, req, resp);
        } else {
            backToEditPage(req, profileForm, violations, resp);
        }
    }
    
    protected abstract Class<?>[] getValidationGroups();
    
    private void backToEditPage(HttpServletRequest req, ProfileForm profileForm, 
            Set<ConstraintViolation<ProfileForm>> violations, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("profile", profileForm);
        req.setAttribute("violations", constraintViolationConvertor.convert(violations));
        forwardToPage(getBackToEditView(), req, resp);
    }

    protected abstract String getBackToEditView();
    
    private void saveChanges(ProfileForm profileForm, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Profile profile = getCurrentProfile();
        profileForm.copyToProfile(profile);
        saveProfile(profile);
        RoutingUtils.redirectToUrl("/"+profile.getUid(), req, resp);
    }
    
    protected abstract Profile getCurrentProfile();
    
    protected abstract void saveProfile(Profile profile);
}

