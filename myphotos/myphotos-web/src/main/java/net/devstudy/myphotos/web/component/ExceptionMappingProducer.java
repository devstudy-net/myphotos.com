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

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static net.devstudy.myphotos.web.Constants.DEFAULT_ERROR_MESSAGE;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

import net.devstudy.myphotos.exception.AccessForbiddenException;
import net.devstudy.myphotos.exception.InvalidAccessTokenException;
import net.devstudy.myphotos.exception.ObjectNotFoundException;
import net.devstudy.myphotos.exception.ValidationException;

/**
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@Dependent
public class ExceptionMappingProducer {

    @Produces
    public Map<Class, Integer> getExceptionToStatusCodeMapping() {
        Map<Class<? extends Throwable>, Integer> map = new HashMap<>();
        map.put(ObjectNotFoundException.class, SC_NOT_FOUND);
        map.put(ValidationException.class, SC_BAD_REQUEST);
        map.put(AccessForbiddenException.class, SC_FORBIDDEN);
        map.put(InvalidAccessTokenException.class, SC_UNAUTHORIZED);
        //add mapping here
        return Collections.unmodifiableMap(map);
    }

    @Produces
    public Map<Integer, String> getStatusMessagesMapping() {
        Map<Integer, String> map = new HashMap<>();
        map.put(SC_BAD_REQUEST, "Current request is invalid");
        map.put(SC_UNAUTHORIZED, "Authentication required");
        map.put(SC_FORBIDDEN, "Requested operation not permitted");
        map.put(SC_NOT_FOUND, "Requested resource not found");
        map.put(SC_INTERNAL_SERVER_ERROR, DEFAULT_ERROR_MESSAGE);
        return map;
    }
}
