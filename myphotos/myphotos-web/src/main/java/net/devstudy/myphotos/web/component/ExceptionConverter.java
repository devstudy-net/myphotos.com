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
package net.devstudy.myphotos.web.component;

import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static net.devstudy.myphotos.web.Constants.DEFAULT_ERROR_MESSAGE;
import static net.devstudy.myphotos.web.Constants.EMPTY_MESSAGE;

import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.devstudy.myphotos.web.model.ErrorModel;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@ApplicationScoped
public class ExceptionConverter {

    @Inject
    private Map<Class, Integer> statusCodeMap;

    @Inject
    private Map<Integer, String> statusMessagesMap;

    public ErrorModel convertToHttpStatus(Throwable throwable) {
        if (throwable instanceof HttpStatusException) {
            HttpStatusException ex = (HttpStatusException) throwable;
            return createErrorModel(ex.getStatus(), ex.getMessage());
        } else {
            Integer status = statusCodeMap.getOrDefault(throwable.getClass(), SC_INTERNAL_SERVER_ERROR);
            return createErrorModel(status, throwable.getMessage());
        }
    }

    private ErrorModel createErrorModel(int status, String msg) {
        if (status == SC_INTERNAL_SERVER_ERROR) {
            return new ErrorModel(status, DEFAULT_ERROR_MESSAGE);
        } else if (EMPTY_MESSAGE.equals(msg)) {
            return new ErrorModel(status, statusMessagesMap.getOrDefault(status, DEFAULT_ERROR_MESSAGE));
        } else {
            return new ErrorModel(status, msg);
        }
    }
}
