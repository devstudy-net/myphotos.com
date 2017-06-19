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
package net.devstudy.myphotos.remote.project.service;

import java.net.URI;
import javax.enterprise.inject.Vetoed;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import net.devstudy.myphotos.exception.ApplicationException;
import net.devstudy.myphotos.remote.project.rest.model.ProfileWithPhotos;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Vetoed
public class ProfileRestServiceImpl implements ProfileRestService{
    
    private final URI rootUri;

    public ProfileRestServiceImpl(String rootUrl, String apiVersion) {
        this.rootUri = UriBuilder.fromUri(rootUrl).path(apiVersion).build();
    }

    @Override
    public ProfileWithPhotos findById(Long id) {
        Client client = ClientBuilder.newClient();
        UriBuilder uriBuilder = UriBuilder.fromUri(rootUri).path("profile").path(String.valueOf(id));
        Response response = client.target(uriBuilder.build()).request(MediaType.APPLICATION_JSON).get();
        if(response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(ProfileWithPhotos.class);
        } else {
            throw new ApplicationException(String.format("Status: %s, Content: %s", response.getStatus(), response.readEntity(String.class)));
        }
    }
}
