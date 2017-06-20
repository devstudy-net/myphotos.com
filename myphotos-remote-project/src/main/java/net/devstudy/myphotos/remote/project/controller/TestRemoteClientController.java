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
package net.devstudy.myphotos.remote.project.controller;

import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.remote.project.rest.model.ProfileWithPhotos;
import net.devstudy.myphotos.remote.project.service.ProfileRestService;
import net.devstudy.myphotos.remote.project.service.RestEndpoint;
import net.devstudy.myphotos.remote.project.ws.model.ProfileSOAP;
import net.devstudy.myphotos.remote.project.ws.model.ProfileService;
import net.devstudy.myphotos.rmi.model.RemoteProfile;
import net.devstudy.myphotos.rmi.service.ProfileRemoteService;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@WebServlet(value="/index.html", loadOnStartup = 1)
public class TestRemoteClientController extends HttpServlet {

    @EJB(lookup = "java:jboss/exported/myphotos-ear-1.0/myphotos-ejb/ProfileServiceBean!net.devstudy.myphotos.rmi.service.ProfileRemoteService")
    private ProfileRemoteService profileEjbService;
    
    @WebServiceRef(wsdlLocation = "http://soap.myphotos.com/ws/ProfileService?wsdl")
    private ProfileService profileSoapService;
    
    @Inject
    @RestEndpoint(baseUrl = "http://api.myphotos.com/", version = "v1")
    private ProfileRestService profileRestService;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Profile profileEjb = profileEjbService.findById(1L);
        String res = ToStringBuilder.reflectionToString(profileEjb);
        resp.getWriter().println("EJB remote Profile:\n" + res + "\n\n\n");

        RemoteProfile profile = profileEjbService.findRemoteById(1L);
        res = ToStringBuilder.reflectionToString(profile);
        resp.getWriter().println("EJB remote RemoteProfile:\n" + res + "\n\n\n");
        
        ProfileSOAP profileSOAP = profileSoapService.getProfileServicePort().findById(1L, false, 10);
        res = ToStringBuilder.reflectionToString(profileSOAP);
        resp.getWriter().println("SOAP client:\n" + res + "\n\n\n");
        
        ProfileWithPhotos profileRest = profileRestService.findById(1L);
        res = ToStringBuilder.reflectionToString(profileRest);
        resp.getWriter().println("REST client:\n" + res + "\n\n\n");
    }
}
