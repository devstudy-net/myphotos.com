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
import javax.persistence.NoResultException;
import javax.persistence.StoredProcedureQuery;
import net.devstudy.myphotos.ejb.repository.ProfileRepository;
import net.devstudy.myphotos.ejb.repository.jpa.StaticJPAQueryInitializer.JPAQuery;
import net.devstudy.myphotos.model.domain.Profile;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@Dependent
public class ProfileRepositoryImpl extends AbstractJPARepository<Profile, Long> implements ProfileRepository {

    @Override
    protected Class<Profile> getEntityClass() {
        return Profile.class;
    }

    @Override
    @JPAQuery("SELECT p FROM Profile p WHERE p.uid=:uid")
    public Optional<Profile> findByUid(String uid) {
        try {
            Profile profile = (Profile) em
                    .createNamedQuery("Profile.findByUid")
                    .setParameter("uid", uid)
                    .getSingleResult();
            return Optional.of(profile);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @JPAQuery("SELECT p FROM Profile p WHERE p.email=:email")
    public Optional<Profile> findByEmail(String email) {
        try {
            Profile profile = (Profile) em
                    .createNamedQuery("Profile.findByEmail")
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(profile);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void updateRating() {
        StoredProcedureQuery query = em
                .createStoredProcedureQuery("update_rating");
        query.execute();
    }

    @Override
    @JPAQuery("SELECT p.uid FROM Profile p WHERE p.uid IN :uids")
    public List<String> findUids(List<String> uids) {
        return em
                .createNamedQuery("Profile.findUids")
                .setParameter("uids", uids)
                .getResultList();
    }
}
