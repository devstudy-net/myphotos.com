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

import java.util.Collections;
import java.util.Map;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import static net.devstudy.myphotos.common.config.Constants.MAX_UPLOADED_PHOTO_SIZE_IN_BYTES;
import net.devstudy.myphotos.model.AsyncOperation;
import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.web.model.PartImageResource;

/**
 *
 * 
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@WebServlet(urlPatterns = "/upload-avatar", asyncSupported = true, loadOnStartup = 1)
@MultipartConfig(maxFileSize = MAX_UPLOADED_PHOTO_SIZE_IN_BYTES)
public class UploadAvatarController extends AbstractUploadController<Profile> {

    @Override
    protected void uploadImage(Profile profile, PartImageResource partImageResource, AsyncOperation<Profile> asyncOperation) {
        profileService.uploadNewAvatar(profile, partImageResource, asyncOperation);
    }

    @Override
    protected Map<String, String> getResultMap(Profile result, HttpServletRequest request) {
        return Collections.singletonMap("thumbnailUrl", result.getAvatarUrl());
    } 
}
