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

package net.devstudy.myphotos.common.model;

import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.devstudy.myphotos.exception.ApplicationException;
import net.devstudy.myphotos.exception.ValidationException;
import net.devstudy.myphotos.model.ImageResource;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
public abstract class AbstractMimeTypeImageResource implements ImageResource {
    
    private Path tempPath;
    
    protected abstract String getContentType();

    protected String getExtension() {
        String contentType = getContentType();
        if ("image/jpeg".equalsIgnoreCase(contentType)) {
            return "jpg";
        } else if ("image/png".equalsIgnoreCase(contentType)) {
            return "png";
        } else {
            throw new ValidationException("Only JPEG/JPG and PNG formats supported. Current format is " + contentType);
        }
    }

    protected abstract void copyContent() throws Exception;

    @Override
    public final Path getTempPath() {
        if (tempPath == null) {
            tempPath = TempFileFactory.createTempFile(getExtension());
            try {
                copyContent();
            } catch (Exception e) {
                throw new ApplicationException(
                        String.format("Can't copy content from %s to temp file %s", toString(), tempPath), e);
            }
        }
        return tempPath;
    }
    
    @Override
    public abstract String toString();

    @Override
    public final void close() {
        TempFileFactory.deleteTempFile(tempPath);
        try {
            deleteTempResources();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, "Can't delete temp resource from "+toString(), ex);
        }
    }
    
    protected abstract void deleteTempResources() throws Exception;
}

