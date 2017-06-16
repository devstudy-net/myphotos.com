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

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import net.devstudy.myphotos.ejb.repository.AccessTokenRepository;
import net.devstudy.myphotos.ejb.repository.PhotoRepository;
import net.devstudy.myphotos.ejb.repository.ProfileRepository;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Dependent
public class InMemoryRepositoryFactory {

    @Inject
    private ProfileRepositoryInvocationHandler profileRepositoryInvocationHandler;
    
    @Inject
    private PhotoRepositoryInvocationHandler photoRepositoryInvocationHandler;

    @Produces
    public ProfileRepository getProfileRepository(){
        return (ProfileRepository) Proxy.newProxyInstance(getClass().getClassLoader(), 
                new Class[]{ProfileRepository.class}, profileRepositoryInvocationHandler);
    }
    
    @Produces
    public PhotoRepository getPhotoRepository(){
        return (PhotoRepository) Proxy.newProxyInstance(getClass().getClassLoader(), 
                new Class[]{PhotoRepository.class}, photoRepositoryInvocationHandler);
    }
    
    @Produces
    public AccessTokenRepository getAccessTokenRepository(){
        return (AccessTokenRepository) Proxy.newProxyInstance(getClass().getClassLoader(), 
                new Class[]{AccessTokenRepository.class}, (Object proxy, Method method, Object[] args) -> {
                    throw new UnsupportedOperationException("Not implemented yet.");
        });
    }
}
