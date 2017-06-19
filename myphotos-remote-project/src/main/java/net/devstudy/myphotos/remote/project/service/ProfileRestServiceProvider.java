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
package net.devstudy.myphotos.remote.project.service;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import net.devstudy.myphotos.exception.ConfigException;
/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Dependent
public class ProfileRestServiceProvider {

    @Produces
    @RestEndpoint(baseUrl = "", version = "")
    public ProfileRestService createProfileRestService(InjectionPoint injectionPoint){
        RestEndpoint restEndpoint = injectionPoint.getAnnotated().getAnnotation(RestEndpoint.class);
        if(restEndpoint == null) {
            throw new ConfigException("Missing @RestEndpoint for ProfileRestService injection point: "+injectionPoint);
        }
        return new ProfileRestServiceImpl(restEndpoint.baseUrl(), restEndpoint.version());
    }
}
