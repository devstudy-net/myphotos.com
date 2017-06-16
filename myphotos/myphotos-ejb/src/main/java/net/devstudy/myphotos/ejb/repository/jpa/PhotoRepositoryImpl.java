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
import javax.enterprise.context.Dependent;
import net.devstudy.myphotos.ejb.repository.PhotoRepository;
import net.devstudy.myphotos.ejb.repository.jpa.StaticJPAQueryInitializer.JPAQuery;
import net.devstudy.myphotos.model.domain.Photo;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Dependent
public class PhotoRepositoryImpl extends AbstractJPARepository<Photo, Long> implements PhotoRepository {

    @Override
    protected Class<Photo> getEntityClass() {
        return Photo.class;
    }

    @Override
    @JPAQuery("SELECT ph FROM Photo ph WHERE ph.profile.id=:profileId ORDER BY ph.id DESC")
    public List<Photo> findProfilePhotosLatestFirst(Long profileId, int offset, int limit) {
        return em.createNamedQuery("Photo.findProfilePhotosLatestFirst")
                .setParameter("profileId", profileId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    @JPAQuery("SELECT COUNT(ph) FROM Photo ph WHERE ph.profile.id=:profileId")
    public int countProfilePhotos(Long profileId) {
        Object count = em
        		.createNamedQuery("Photo.countProfilePhotos")
                .setParameter("profileId", profileId)
                .getSingleResult();
        return ((Number)count).intValue();
    }
    
    @Override
    @JPAQuery("SELECT e FROM Photo e JOIN FETCH e.profile ORDER BY e.views DESC")
    public List<Photo> findAllOrderByViewsDesc(int offset, int limit) {
        return em.createNamedQuery("Photo.findAllOrderByViewsDesc")
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    @JPAQuery("SELECT e FROM Photo e JOIN FETCH e.profile p ORDER BY p.rating DESC, e.views DESC")
    public List<Photo> findAllOrderByAuthorRatingDesc(int offset, int limit) {
        return em.createNamedQuery("Photo.findAllOrderByAuthorRatingDesc")
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    @JPAQuery("SELECT COUNT(ph) FROM Photo ph")
    public long countAll() {
        Object count = em
        		.createNamedQuery("Photo.countAll")
        		.getSingleResult();
        return ((Number)count).intValue();
    }

}
