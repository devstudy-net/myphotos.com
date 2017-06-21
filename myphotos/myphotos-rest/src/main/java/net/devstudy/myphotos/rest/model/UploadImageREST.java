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
package net.devstudy.myphotos.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import net.devstudy.myphotos.model.ImageResource;
import net.devstudy.myphotos.common.model.AbstractMimeTypeImageResource;
import org.apache.commons.fileupload.FileItem;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@ApiModel("UploadImage")
public class UploadImageREST {

    private final FileItem fileItem;

    public UploadImageREST(FileItem fileItem) {
        this.fileItem = fileItem;
    }

    @ApiModelProperty(hidden = true)
    public FileItem getFileItem() {
        return fileItem;
    }

    @ApiModelProperty(hidden = true)
    public ImageResource getImageResource() {
        return new FileItemImageResource();
    }

    /**
     *
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    private class FileItemImageResource extends AbstractMimeTypeImageResource {

        @Override
        protected String getContentType() {
            return fileItem.getContentType();
        }

        @Override
        public String toString() {
            return String.format("%s(%s)", getClass().getSimpleName(), fileItem);
        }

        @Override
        protected void copyContent() throws Exception {
            fileItem.write(getTempPath().toFile());
        }

        @Override
        protected void deleteTempResources() throws IOException {
            fileItem.delete();
        }
    }
}
