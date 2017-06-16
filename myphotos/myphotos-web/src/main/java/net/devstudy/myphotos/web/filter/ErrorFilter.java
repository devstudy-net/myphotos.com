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
package net.devstudy.myphotos.web.filter;

import static net.devstudy.myphotos.web.Constants.EMPTY_MESSAGE;
import static net.devstudy.myphotos.web.util.RoutingUtils.forwardToPage;
import static net.devstudy.myphotos.web.util.RoutingUtils.sendJson;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import net.devstudy.myphotos.exception.BusinessException;
import net.devstudy.myphotos.web.component.ExceptionConverter;
import net.devstudy.myphotos.web.component.HttpStatusException;
import net.devstudy.myphotos.web.model.ErrorModel;
import net.devstudy.myphotos.web.util.WebUtils;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@WebFilter(filterName = "ErrorFilter", asyncSupported = true)
public class ErrorFilter extends AbstractFilter {

    @Inject
    private Logger logger;

    @Inject
    private ExceptionConverter exceptionConverter;

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, new ThrowExceptionInsteadOfSendErrorResponse(response));
        } catch (Throwable th) {
            Throwable throwable = unWrapThrowable(th);
            logError(request, throwable);
            ErrorModel errorModel = exceptionConverter.convertToHttpStatus(throwable);
            handleError(request, errorModel, response);
        }
    }

    private Throwable unWrapThrowable(Throwable th) {
        if (th instanceof ServletException && th.getCause() != null) {
            return th.getCause();
        } else {
            return th;
        }
    }

    private void logError(HttpServletRequest request, Throwable th) {
        String errorMessage = String.format("Can't process request: %s -> %s", request.getRequestURI(), th.getMessage());
        if (th instanceof BusinessException) {
            logger.log(Level.WARNING, errorMessage);
        } else {
            logger.log(Level.SEVERE, errorMessage, th);
        }
    }

    private void handleError(HttpServletRequest request, ErrorModel errorModel, HttpServletResponse response) throws ServletException, IOException {
        response.reset();
        response.setStatus(errorModel.getStatus());
        if (WebUtils.isAjaxRequest(request)) {
            sendAjaxJsonErrorResponse(errorModel, request, response);
        } else {
            sendErrorPage(errorModel, request, response);
        }
    }

    private void sendAjaxJsonErrorResponse(ErrorModel errorModel, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObject json = Json.createObjectBuilder().add("success", false).add("error", errorModel.getMessage()).build();
        sendJson(json, request, response);
    }

    private void sendErrorPage(ErrorModel errorModel, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("errorModel", errorModel);
        forwardToPage("error", request, response);
    }

    /**
     *
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    private static class ThrowExceptionInsteadOfSendErrorResponse extends HttpServletResponseWrapper {

        public ThrowExceptionInsteadOfSendErrorResponse(HttpServletResponse response) {
            super(response);
        }

        @Override
        public void sendError(int sc) throws IOException {
            sendError(sc, EMPTY_MESSAGE);
        }

        @Override
        public void sendError(int sc, String msg) throws IOException {
            throw new HttpStatusException(sc, msg);
        }
    }
}
