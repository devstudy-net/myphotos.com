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

package net.devstudy.myphotos.ejb.repository.mock;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import static net.devstudy.myphotos.ejb.repository.mock.InMemoryDataBase.PHOTOS;
import static net.devstudy.myphotos.ejb.repository.mock.InMemoryDataBase.PROFILE;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@ApplicationScoped
public class PhotoRepositoryInvocationHandler implements InvocationHandler{

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        switch (method.getName()) {
            case "findProfilePhotosLatestFirst":
                return findProfilePhotosLatestFirst(args);
            case "findAllOrderByViewsDesc":
            case "findAllOrderByAuthorRatingDesc":
                return findAll(args);
            case "countAll":
                return countAll(args);
            default:
                throw new UnsupportedOperationException(String.format("Method %s not implemented yet", method));
        }
    }

    private Object findProfilePhotosLatestFirst(Object[] args) {
        Long profileId = (Long) args[0];
        int offset = (Integer) args[1];
        int limit = (Integer) args[2];
        if(profileId.equals(PROFILE.getId())) {
            return PHOTOS.stream().skip(offset).limit(limit).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    private Object findAll(Object[] args) {
        int offset = (Integer) args[0];
        int limit = (Integer) args[1];
        return PHOTOS.stream().skip(offset).limit(limit).collect(Collectors.toList());
    }

    private Object countAll(Object[] args) {
        return (long)PHOTOS.size();
    }
}

