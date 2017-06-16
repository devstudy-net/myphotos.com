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

import java.util.Optional;
import javax.enterprise.context.Dependent;
import javax.persistence.NoResultException;
import net.devstudy.myphotos.ejb.repository.AccessTokenRepository;
import net.devstudy.myphotos.ejb.repository.jpa.StaticJPAQueryInitializer.JPAQuery;
import net.devstudy.myphotos.model.domain.AccessToken;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@Dependent
public class AccessTokenRepositoryImpl extends AbstractJPARepository<AccessToken, String> implements AccessTokenRepository {

    @Override
    protected Class<AccessToken> getEntityClass() {
        return AccessToken.class;
    }

    @Override
    @JPAQuery("SELECT at FROM AccessToken at JOIN FETCH at.profile WHERE at.token=:token")
    public Optional<AccessToken> findByToken(String token) {
        try {
            AccessToken accessToken = (AccessToken) em
                    .createNamedQuery("AccessToken.findByToken")
                    .setParameter("token", token)
                    .getSingleResult();
            return Optional.of(accessToken);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @JPAQuery("DELETE FROM AccessToken at WHERE at.token=:token")
    public boolean removeAccessToken(String token) {
        int result = em
                .createNamedQuery("AccessToken.removeAccessToken")
                .setParameter("token", token)
                .executeUpdate();
        return result == 1;
    }
}
