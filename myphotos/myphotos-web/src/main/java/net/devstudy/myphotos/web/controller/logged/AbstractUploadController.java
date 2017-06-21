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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import static net.devstudy.myphotos.common.config.Constants.DEFAULT_ASYNC_OPERATION_TIMEOUT_IN_MILLIS;
import net.devstudy.myphotos.model.AsyncOperation;
import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.service.ProfileService;
import net.devstudy.myphotos.web.model.PartImageResource;
import net.devstudy.myphotos.web.security.SecurityUtils;
import static net.devstudy.myphotos.web.util.RoutingUtils.sendFileUploaderJson;

/**
 * https://docs.fineuploader.com
 *
 * @author devstudy
 * @see http://devstudy.net
 */
public abstract class AbstractUploadController<T> extends HttpServlet {

    @Inject
    protected Logger logger;
    
    @Inject
    protected ProfileService profileService;

    @Override
    protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part part = req.getPart("qqfile");
        Profile profile = getCurrentProfile();
        final AsyncContext asyncContext = req.startAsync(req, resp);
        asyncContext.setTimeout(DEFAULT_ASYNC_OPERATION_TIMEOUT_IN_MILLIS);
        uploadImage(profile, new PartImageResource(part), new AsyncOperation<T>() {
            @Override
            public void onSuccess(T result) {
                handleSuccess(asyncContext, result);
            }

            @Override
            public void onFailed(Throwable throwable) {
                handleFailed(throwable, asyncContext);
            }

            @Override
            public long getTimeOutInMillis() {
                return asyncContext.getTimeout();
            }
        });
    }
    
    protected Profile getCurrentProfile(){
        return profileService.findById(SecurityUtils.getCurrentProfileId().getId());
    }

    protected abstract void uploadImage(Profile profile, PartImageResource partImageResource, AsyncOperation<T> asyncOperation);

    protected void handleSuccess(AsyncContext asyncContext, T result) {
        try {
            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder().add("success", true);
            for (Map.Entry<String, String> entry : getResultMap(result, (HttpServletRequest) asyncContext.getRequest()).entrySet()) {
                jsonObjectBuilder.add(entry.getKey(), entry.getValue());
            }
            JsonObject json = jsonObjectBuilder.build();
            sendJsonResponse(json, asyncContext);
        } finally {
            asyncContext.complete();
        }
    }

    protected void handleFailed(Throwable throwable, AsyncContext asyncContext) {
        try {
            logger.log(Level.SEVERE, "Async operation failed: " + throwable.getMessage(), throwable);
            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder().add("success", false);
            JsonObject json = jsonObjectBuilder.build();
            sendJsonResponse(json, asyncContext);
        } finally {
            asyncContext.complete();
        }
    }

    protected void sendJsonResponse(JsonObject json, AsyncContext asyncContext) {
        try {
            HttpServletRequest request = (HttpServletRequest) asyncContext.getRequest();
            HttpServletResponse response = (HttpServletResponse) asyncContext.getResponse();
            // https://docs.fineuploader.com/endpoint_handlers/traditional.html#response
            sendFileUploaderJson(json, request, response);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "sendJsonResponse failed: " + e.getMessage(), e);
        }
    }

    protected abstract Map<String, String> getResultMap(T result, HttpServletRequest request);
}
