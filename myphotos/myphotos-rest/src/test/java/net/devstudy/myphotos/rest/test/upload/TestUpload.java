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

package net.devstudy.myphotos.rest.test.upload;

import java.io.File;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.devstudy.myphotos.rest.Constants;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
public class TestUpload {

    private static final Long profileId = 11L;

    private static final String accessToken = "test";

    public static void main(String[] args) {
        makeUploadRequest(
                String.format("http://api.myphotos.com/v1/profile/%s/avatar", profileId),
                System.getProperty("user.home") + "/Documents/NetBeansProjects/myphotos-generator/external/test-data/avatar/Richard Hendricks.jpg"
        );
        makeUploadRequest(
                String.format("http://api.myphotos.com/v1/profile/%s/photo", profileId),
                System.getProperty("user.home") + "/Documents/NetBeansProjects/myphotos-generator/external/test-data/photos/10.jpg"
        );
    }

    private static void makeUploadRequest(String uploadAvatarUrl, String filePath) {
        Client client = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class)
                .build();
        Entity<?> entity = createUploadEntity(filePath);
        Response response = client
                .target(uploadAvatarUrl)
                .request(MediaType.APPLICATION_JSON)
                .header(Constants.ACCESS_TOKEN_HEADER, accessToken)
                .post(entity);
        System.out.println("Request: " + uploadAvatarUrl);
        System.out.println("Status: " + response.getStatus());
        System.out.println("Content: " + response.readEntity(String.class));
    }

    private static Entity<?> createUploadEntity(String filePath) {
        MultiPart multiPart = new MultiPart();
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);
        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("file",
                new File(filePath), new MediaType("image", "jpeg"));
        multiPart.bodyPart(fileDataBodyPart);
        return Entity.entity(multiPart, multiPart.getMediaType());
    }
}
