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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.commons.fileupload.RequestContext;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class JAXRSRequestContext implements RequestContext{
    private final MultivaluedMap<String, String> httpHeaders;
    private final InputStream entityStream;
    private final String contentType;

    public JAXRSRequestContext(MultivaluedMap<String, String> httpHeaders, InputStream entityStream, String contentType) {
        this.httpHeaders = httpHeaders;
        this.entityStream = entityStream;
        this.contentType = contentType;
    }
    
    @Override
    public String getCharacterEncoding() {
        return StandardCharsets.UTF_8.name();
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public int getContentLength() {
        return Integer.parseInt(httpHeaders.getFirst("Content-Length"));
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return entityStream;
    }
}
