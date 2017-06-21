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
package net.devstudy.myphotos.web.util;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import javax.json.JsonObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.devstudy.myphotos.web.security.SecurityUtils;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
public class RoutingUtils {

    public static void forwardToPage(String pageName, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("currentPage", String.format("../view/%s.jsp", pageName));
        request.getRequestDispatcher("/WEB-INF/template/page-template.jsp").forward(request, response);
    }

    public static void forwardToFragment(String fragmentName, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher(String.format("/WEB-INF/fragment/%s.jsp", fragmentName)).forward(request, response);
    }

    public static void redirectToUrl(String url, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(url);
    }

    public static void sendJson(JsonObject json, HttpServletRequest request, HttpServletResponse response) throws IOException {
        sendJson("application/json", json, request, response);
    }

    public static void redirectToValidAuthUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (SecurityUtils.isTempAuthenticated()) {
            redirectToUrl("/sign-up", request, response);
        } else {
            redirectToUrl("/" + SecurityUtils.getCurrentProfileId().getUid(), request, response);
        }
    }

    /*
     *  Based on recommendations: https://docs.fineuploader.com/endpoint_handlers/traditional.html#response
     */
    public static void sendFileUploaderJson(JsonObject json, HttpServletRequest request, HttpServletResponse response) throws IOException {
        sendJson("text/plain", json, request, response);
    }

    private static void sendJson(String contentType, JsonObject json, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String content = json.toString();
        int length = content.getBytes(StandardCharsets.UTF_8).length;
        response.setContentType(contentType);
        response.setContentLength(length);

        try (Writer wr = response.getWriter()) {
            wr.write(content);
            wr.flush();
        }
    }

    private RoutingUtils() {
    }
}
