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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import net.devstudy.myphotos.ejb.service.ProfileUidService;
import net.devstudy.myphotos.ejb.model.ProfileUidGenerator;
import static net.devstudy.myphotos.ejb.model.ProfileUidGenerator.Category.SECONDARY;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@ApplicationScoped
@ProfileUidGenerator(category = SECONDARY)
public class SecondaryProfileUidService implements ProfileUidService{

    @Override
    public List<String> generateProfileUidCandidates(String englishFirstName, String englishLastName) {
        return Collections.unmodifiableList(Arrays.asList(
                String.format("%s-%s", englishFirstName.charAt(0), englishLastName).toLowerCase(),
                String.format("%s.%s", englishFirstName.charAt(0), englishLastName).toLowerCase(),
                String.format("%s%s", englishFirstName.charAt(0), englishLastName).toLowerCase(),
                englishLastName.toLowerCase(),
                englishFirstName.toLowerCase()
        ));
    }
}
