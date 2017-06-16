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

package net.devstudy.myphotos.ejb.repository.jpa;

import java.util.List;
import java.util.Optional;
import javax.enterprise.context.Dependent;
import net.devstudy.myphotos.ejb.repository.ProfileRepository;
import net.devstudy.myphotos.model.domain.Profile;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Dependent
public class ProfileRepositoryImpl extends AbstractJPARepository<Profile, Long> implements ProfileRepository{

    @Override
    protected Class<Profile> getEntityClass() {
        return Profile.class;
    }

    @Override
    public Optional<Profile> findByUid(String uid) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public Optional<Profile> findByEmail(String email) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void updateRating() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public List<String> findUids(List<String> uids) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
