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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import net.devstudy.myphotos.ejb.service.ProfileUidService;
import net.devstudy.myphotos.ejb.model.ProfileUidGenerator;
import static net.devstudy.myphotos.ejb.model.ProfileUidGenerator.Category.PRIMARY;
import static net.devstudy.myphotos.ejb.model.ProfileUidGenerator.Category.SECONDARY;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@ApplicationScoped
public class ProfileUidServiceManager {

    @Inject
    private Logger logger;
    
    @Inject
    @Any
    private Instance<ProfileUidService> profileUidServices;

    public List<String> getProfileUidCandidates(String englishFirstName, String englishLastName) {
        List<String> result = new ArrayList<>();
        addCandidates(new PrimaryProfileUidGenerator(),   result, englishFirstName, englishLastName);
        addCandidates(new SecondaryProfileUidGenerator(), result, englishFirstName, englishLastName);
        return Collections.unmodifiableList(result);
    }
    
    public String getDefaultUid(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    private void addCandidates(AnnotationLiteral<ProfileUidGenerator> selector, List<String> result, String englishFirstName, String englishLastName) {
        Instance<ProfileUidService> services = profileUidServices.select(selector);
        for (ProfileUidService service : services) {
            result.addAll(service.generateProfileUidCandidates(englishFirstName, englishLastName));
        }
    }
    
    @PostConstruct
    private void postConstruct(){
        StringBuilder logStr = new StringBuilder("Detected the following ProfileUidService beans\n");
        for(ProfileUidService service : profileUidServices) {
            logStr.append(String.format("%s\n", service.getClass().getName()));
        }
        logger.info(logStr.toString());
    }

    /**
     *
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    private class PrimaryProfileUidGenerator extends AnnotationLiteral<ProfileUidGenerator> implements ProfileUidGenerator {

        public ProfileUidGenerator.Category category() {
            return PRIMARY;
        }
    }

    /**
     *
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    private class SecondaryProfileUidGenerator extends AnnotationLiteral<ProfileUidGenerator> implements ProfileUidGenerator {

        public ProfileUidGenerator.Category category() {
            return SECONDARY;
        }
    }
}

