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

package net.devstudy.myphotos.ejb.model;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import net.devstudy.myphotos.exception.ApplicationException;
import net.devstudy.myphotos.common.model.AbstractMimeTypeImageResource;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
public class URLImageResource extends AbstractMimeTypeImageResource {

    private final String url;
    private final URLConnection urlConnection;

    public URLImageResource(String url) {
        this.url = url;
        try {
            this.urlConnection = new URL(url).openConnection();
        } catch (IOException ex) {
            throw new ApplicationException("Can't open connection to url: "+url, ex);
        }
    }

    @Override
    protected String getContentType() {
        return urlConnection.getContentType();
    }

    @Override
    protected void copyContent() throws IOException {
        Files.copy(urlConnection.getInputStream(), getTempPath(), REPLACE_EXISTING);
    }
    
    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), url);
    }

    @Override
    protected void deleteTempResources() {
        //do nothing
    }
}

