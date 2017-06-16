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

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.sql.DataSource;
import net.devstudy.myphotos.common.annotation.cdi.Property;
import net.devstudy.myphotos.common.config.ImageCategory;
import net.devstudy.myphotos.common.model.TempImageResource;
import net.devstudy.myphotos.ejb.service.bean.PhotoServiceBean;
import net.devstudy.myphotos.ejb.service.bean.ProfileServiceBean;
import net.devstudy.myphotos.generator.component.AbstractEnvironmentGenerator;
import net.devstudy.myphotos.generator.component.PhotoGenerator;
import net.devstudy.myphotos.generator.component.ProfileGenerator;
import net.devstudy.myphotos.generator.component.UpdatePhotoService;
import net.devstudy.myphotos.model.domain.Photo;
import net.devstudy.myphotos.model.domain.Profile;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class DataGenerator extends AbstractEnvironmentGenerator{
    
    @Inject
    private ProfileGenerator profileGenerator;

    @Inject
    private PhotoGenerator photoGenerator;
    
    @Inject
    private UpdatePhotoService updatePhotoService;
    
    @EJB
    private ProfileServiceBean profileServiceBean;

    @EJB
    private PhotoServiceBean photoServiceBean;
    
    @Resource(mappedName = "MyPhotosDBPool")
    private DataSource dataSource;
    
    @Inject
    @Property("myphotos.storage.root.dir")
    private String storageRoot;

    @Inject
    @Property("myphotos.media.absolute.root")
    private String mediaRoot;
    
    public static void main(String[] args) throws Exception {
        new DataGenerator().execute();
    }

    @Override
    protected void generate() throws Exception {
        clearExternalResources();
        List<Profile> profiles = profileGenerator.generateProfiles();
        List<Photo> uploadedPhotos = new ArrayList<>();
        for (Profile profile : profiles) {
            profileServiceBean.signUp(profile, false);
            profileServiceBean.uploadNewAvatar(profile, new PathImageResource(profile.getAvatarUrl()));
            List<String> photoPaths = photoGenerator.generatePhotos(profile.getPhotoCount());
            for (String path : photoPaths) {
                Profile dbProfile = profileServiceBean.findById(profile.getId());
                uploadedPhotos.add(photoServiceBean.uploadNewPhoto(dbProfile, new PathImageResource(path)));
            }
        }
        updatePhotoService.updatePhotos(uploadedPhotos);
        updateProfileRating();
        System.out.println("Generated " + profiles.size() + " profiles");
        System.out.println("Generated " + uploadedPhotos.size() + " photos");
    }
    
    private void clearExternalResources() throws SQLException, IOException {
        clearDatabase();
        clearDirectory(storageRoot);
        clearDirectory(mediaRoot + ImageCategory.LARGE_PHOTO.getRelativeRoot());
        clearDirectory(mediaRoot + ImageCategory.SMALL_PHOTO.getRelativeRoot());
        clearDirectory(mediaRoot + ImageCategory.PROFILE_AVATAR.getRelativeRoot());
    }
    
    private void clearDatabase() throws SQLException {
        try (Connection c = dataSource.getConnection(); 
                Statement st = c.createStatement()) {
            st.executeUpdate("TRUNCATE photo CASCADE");
            st.executeUpdate("TRUNCATE access_token CASCADE");
            st.executeUpdate("TRUNCATE profile CASCADE");
            st.executeQuery("SELECT SETVAL('profile_seq', 1, false)");
            st.executeQuery("SELECT SETVAL('photo_seq', 123456, false)");
        }
        System.out.println("Database cleared");
    }
    
    private void clearDirectory(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        if (Files.exists(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return super.visitFile(file, attrs);
                }
            });
            System.out.println("Directory " + directoryPath + " cleared");
        } else {
            Files.createDirectories(path);
        }
    }
    
    private void updateProfileRating() throws SQLException {
        try (Connection c = dataSource.getConnection();
                PreparedStatement ps = c.prepareStatement("SELECT update_rating()")) {
            c.setAutoCommit(false);
            ps.executeQuery();
            c.commit();
        }
    }

    /**
     *
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    private static class PathImageResource extends TempImageResource {

        public PathImageResource(String path) throws IOException {
            Files.copy(Paths.get(path), getTempPath(), REPLACE_EXISTING);
        }
    }
}
