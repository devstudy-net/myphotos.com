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

package net.devstudy.myphotos.ejb.service.impl;

import java.io.IOException;
import static java.lang.String.format;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import net.devstudy.myphotos.common.annotation.cdi.Property;
import net.devstudy.myphotos.common.config.ImageCategory;
import net.devstudy.myphotos.ejb.service.FileNameGeneratorService;
import net.devstudy.myphotos.ejb.service.ImageStorageService;
import net.devstudy.myphotos.exception.ApplicationException;
import net.devstudy.myphotos.model.OriginalImage;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@ApplicationScoped
public class LocalPathImageStorageService implements ImageStorageService {

    @Inject
    private Logger logger;

    @Inject
    @Property("myphotos.storage.root.dir")
    private String storageRoot;

    @Inject
    @Property("myphotos.media.absolute.root")
    private String mediaRoot;

    @Inject
    private FileNameGeneratorService fileNameGeneratorService;
    
    @Override
    public String saveProtectedImage(Path path) {
        String fileName = fileNameGeneratorService.generateUniqueFileName();
        Path destinationPath = Paths.get(storageRoot, fileName);
        saveImage(path, destinationPath);
        return fileName;
    }

    @Override
    public String savePublicImage(ImageCategory imageCategory, Path path) {
        String fileName = fileNameGeneratorService.generateUniqueFileName();
        Path destinationPath = Paths.get(mediaRoot, imageCategory.getRelativeRoot(), fileName);
        saveImage(path, destinationPath);
        return "/" + imageCategory.getRelativeRoot() + fileName;
    }
    
    /**
     * Try to move and then try to copy if move failed
     */
    private void saveImage(Path sourcePath, Path destinationPath) {
        try {
            Files.move(sourcePath, destinationPath, REPLACE_EXISTING);
        } catch (IOException | RuntimeException ex) {
            logger.log(Level.WARNING, String.format("Move failed from %s to %s. Try to copy...", sourcePath, destinationPath), ex);
            try {
                Files.copy(sourcePath, destinationPath, REPLACE_EXISTING);
            } catch(IOException e) {
                ApplicationException applicationException = new ApplicationException("Can't save image: " + destinationPath, e);
                applicationException.addSuppressed(ex);
                throw applicationException;
            }
        }
        logger.log(Level.INFO, "Saved image: {0}", destinationPath);
    }
    
    @Override
    public void deletePublicImage(String url) {
        Path destinationPath = Paths.get(mediaRoot, url.substring(1));
        try {
            Files.deleteIfExists(destinationPath);
        } catch (IOException | RuntimeException e) {
            logger.log(Level.SEVERE, "Delete public image failed: "+destinationPath, e);
        }
    }
    
    @Override
    public OriginalImage getOriginalImage(String originalUrl) {
    	Path originalPath = Paths.get(storageRoot, originalUrl);
    	try {
            return new OriginalImage(
                    Files.newInputStream(originalPath),
                    Files.size(originalPath),
                    originalPath.getFileName().toString());
        } catch (IOException ex) {
            throw new ApplicationException(format("Can't get access to original image: %s", originalPath), ex);
        }
    }
}
