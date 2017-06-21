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

package net.devstudy.myphotos.generator.component;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.enterprise.context.ApplicationScoped;
import net.devstudy.myphotos.exception.ConfigException;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@ApplicationScoped
public class PhotoGenerator {

    private final Random random = new Random();
    
    private final List<String> fileNames = getAllTestPhotos();
    
    private int index = 0;

    public List<String> generatePhotos(int photoCount) {
        List<String> photos = new ArrayList<>();
        for (int i = 0; i < photoCount; i++) {
            photos.add(getPhoto());
        }
        return Collections.unmodifiableList(photos);
    }
    
    private String getPhoto(){
        if(index >= fileNames.size()) {
            index = 0;
        }
        return fileNames.get(index++);
    }

    private List<String> getAllTestPhotos() {
        List<String> list = new ArrayList<>();
        Path rootPath = Paths.get("external/test-data/photos");
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(rootPath)) {
            for (Path path : directoryStream) {
                list.add(path.toAbsolutePath().toString());
            }
        } catch (IOException ex) {
           throw new ConfigException("Can't get list photos from "+rootPath.toAbsolutePath().toString(), ex);
        }
        Collections.shuffle(list);
        return list;
    }
}

