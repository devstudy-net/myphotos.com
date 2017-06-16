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

package net.devstudy.myphotos.web.model;

import java.io.IOException;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.Objects;
import javax.servlet.http.Part;
import net.devstudy.myphotos.common.model.AbstractMimeTypeImageResource;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
public final class PartImageResource extends AbstractMimeTypeImageResource {

    private final Part part;

    public PartImageResource(Part part) {
        this.part = Objects.requireNonNull(part);
    }

    @Override
    protected String getContentType() {
        return part.getContentType();
    }

    @Override
    protected void copyContent() throws IOException {
        Files.copy(part.getInputStream(), getTempPath(), REPLACE_EXISTING);
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", getClass().getSimpleName(), part);
    }

    @Override
    protected void deleteTempResources() throws IOException {
        part.delete();
    }
}
