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

package net.devstudy.myphotos.generator.alternative;

import javax.enterprise.context.Dependent;
import net.devstudy.myphotos.ejb.repository.jpa.PhotoRepositoryImpl;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Dependent
@TestDataGeneratorEnvironment
public class PhotoRepositoryImplAlternative extends PhotoRepositoryImpl{

    @Override
    public int countProfilePhotos(Long profileId) {
        Object count = em.createQuery("SELECT COUNT(ph) FROM Photo ph WHERE ph.profile.id=:profileId")
                .setParameter("profileId", profileId)
                .getSingleResult();
        return ((Number)count).intValue();
    }
}
