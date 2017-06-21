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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
public class TempFileFactory {

    public static Path createTempFile(String extension) {
        String uniqueFileName = String.format("%s.%s", UUID.randomUUID(), extension);
        String tempDirectoryPath = System.getProperty("java.io.tmpdir");
        Path tempFilePath = Paths.get(tempDirectoryPath, uniqueFileName);
        try {
            return Files.createFile(tempFilePath);
        } catch (IOException e) {
            throw new CantCreateTempFileException(tempFilePath, e);
        }
    }

    public static void deleteTempFile(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException | RuntimeException ex) {
            Logger.getLogger("TempFileEraser").log(Level.WARNING, "Can't delete temp file: " + path, ex);
        }
    }

    /**
     *
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    private static class CantCreateTempFileException extends IllegalStateException {

        private CantCreateTempFileException(Path tempFilePath, Throwable throwable) {
            super("Can't create temp file: " + tempFilePath, throwable);
        }
    }
}
