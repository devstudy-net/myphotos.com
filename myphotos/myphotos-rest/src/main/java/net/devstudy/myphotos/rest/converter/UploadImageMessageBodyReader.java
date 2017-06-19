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

package net.devstudy.myphotos.rest.converter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import static net.devstudy.myphotos.common.config.Constants.MAX_UPLOADED_PHOTO_SIZE_IN_BYTES;
import net.devstudy.myphotos.exception.ApplicationException;
import net.devstudy.myphotos.exception.ValidationException;
import net.devstudy.myphotos.rest.model.UploadImageREST;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import static org.apache.commons.fileupload.disk.DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@Provider
@ApplicationScoped
@Consumes(MULTIPART_FORM_DATA)
public class UploadImageMessageBodyReader implements MessageBodyReader<UploadImageREST> {

    private File tempDirectory;

    @PostConstruct
    private void postConstruct() {
        try {
            tempDirectory = Files.createTempDirectory("upload").toFile();
        } catch (IOException ex) {
            throw new ApplicationException("Can't create temp directory", ex);
        }
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return UploadImageREST.class.isAssignableFrom(type);
    }

    @Override
    public UploadImageREST readFrom(Class<UploadImageREST> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        DiskFileItemFactory factory = new DiskFileItemFactory(DEFAULT_SIZE_THRESHOLD, tempDirectory);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_UPLOADED_PHOTO_SIZE_IN_BYTES);
        try {
            List<FileItem> items = upload.parseRequest(new JAXRSRequestContext(httpHeaders, entityStream, mediaType.toString()));
            for (FileItem fileItem : items) {
                if (!fileItem.isFormField()) {
                    return new UploadImageREST(fileItem);
                }
            }
        } catch (Exception e) {
            throw new ApplicationException("Can't parse multipart request: "+e.getMessage(), e);
        }
        throw new ValidationException("Missing content");
    }
}
