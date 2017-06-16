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
package net.devstudy.myphotos.generator;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * https://unsplash.com/documentation
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class DownloadPhotosFromUnsplash {

    public static void main(String[] args) throws Exception {
        new DownloadPhotosFromUnsplash().execute(
                "https://api.unsplash.com/photos?per_page=30",
                Paths.get("external/test-data/photos")
        );
    }

    public void execute(String unsplashSourceUrl, Path destinationDirectoryPath) throws IOException {
        createDestinationIfNecessary(destinationDirectoryPath);
        Response response = getPhotoLinks(unsplashSourceUrl);
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            parseValidResponse(response, destinationDirectoryPath);
        } else {
            displayErrorMessage(response);
        }
    }
    
    protected void createDestinationIfNecessary(Path destinationDirectoryPath) throws IOException {
        if(!Files.exists(destinationDirectoryPath)){
            Files.createDirectories(destinationDirectoryPath);
        }
    }

    protected Response getPhotoLinks(String url) {
        Client client = ClientBuilder.newClient();
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        client.register(new JacksonJaxbJsonProvider(objectMapper, JacksonJaxbJsonProvider.DEFAULT_ANNOTATIONS));

        return client.target(url)
                .request(MediaType.APPLICATION_JSON)
                .header("Accept-Version", "v1")
                .header("Authorization", "Client-ID " + getSystemEnvironmentVariable("UNSPLASH_KEY"))
                .get();
    }

    protected String getSystemEnvironmentVariable(String name) {
        Map<String, String> env = System.getenv();
        for (Map.Entry<String, String> entry : env.entrySet()) {
            if (entry.getKey().equals(name)) {
                return entry.getValue();
            }
        }
        throw new IllegalStateException("System variable not defined: " + name);
    }

    protected void parseValidResponse(Response response, Path destinationDirectoryPath) {
        System.out.println("X-Ratelimit-Remaining=" + response.getHeaderString("X-Ratelimit-Remaining"));

        List<Item> items = response.readEntity(new GenericType<List<Item>>() {
        });
        int id = 10;
        for (Item item : items) {
            Path file = Paths.get(String.format("%s/%s.jpg", destinationDirectoryPath.toAbsolutePath().toString(), id));
            downloadImage(item.getLinks().getDownload(), file);
            id++;
            System.out.println("Successful downloaded " + item.getLinks().getDownload());
        }
    }

    protected void downloadImage(String url, Path file) {
        Response response = ClientBuilder.newClient()
                .register(new FollowRedirectFilter())
                .target(url)
                .request("image/jpeg")
                .get();
        try (InputStream in = response.readEntity(InputStream.class)) {
            Files.copy(in, file, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            System.err.println("Download file failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    protected static class FollowRedirectFilter implements ClientResponseFilter {

        @Override
        public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
            if (responseContext.getStatusInfo().getFamily() == Response.Status.Family.REDIRECTION) {
                Response resp = requestContext.getClient()
                        .target(responseContext.getLocation())
                        .request()
                        .method(requestContext.getMethod());
                responseContext.setEntityStream((InputStream) resp.getEntity());
                responseContext.setStatusInfo(resp.getStatusInfo());
                responseContext.setStatus(resp.getStatus());
            }
        }
    }

    protected void displayErrorMessage(Response response) {
        System.out.println(String.format("Status: %s %s", response.getStatusInfo().getStatusCode(), response.getStatusInfo().getReasonPhrase()));
        System.out.println(response.readEntity(String.class));
    }
    
    /**
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    protected static class Item {

        private Links links;

        public Links getLinks() {
            return links;
        }

        public void setLinks(Links links) {
            this.links = links;
        }
    }

    /**
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    protected static class Links {

        private String download;

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }
    }
}
