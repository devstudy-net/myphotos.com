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

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import net.devstudy.myphotos.exception.ApplicationException;
import net.devstudy.myphotos.model.domain.Profile;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@ApplicationScoped
public class ProfileGenerator{
    
    private final Random random = new Random();

    public List<Profile> generateProfiles() {
        File file = new File("external/test-data/profiles.xml");
        try {    
            JAXBContext jaxbContext = JAXBContext.newInstance(Profiles.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Profiles profiles = (Profiles) jaxbUnmarshaller.unmarshal(file);
            final Date created = new Date();
            return Collections.unmodifiableList(profiles.getProfile().stream().map((Profile t) -> {
                t.setUid(String.format("%s-%s", t.getFirstName(), t.getLastName()).toLowerCase());
                t.setEmail(t.getUid()+"@myphotos.com");
                t.setPhotoCount(random.nextInt(15) + random.nextInt(5) + 3);
                t.setCreated(created);
                return t;
            }).collect(Collectors.toList()));
        } catch (JAXBException e) {
            throw new ApplicationException("Can't load test data from: " + file.getAbsolutePath(), e);
        }
    }

    @XmlRootElement(name = "profiles")
    private static class Profiles {
        private List<Profile> profile;

        public List<Profile> getProfile() {
            return profile;
        }

        public void setProfile(List<Profile> profile) {
            this.profile = profile;
        }
    }
}

