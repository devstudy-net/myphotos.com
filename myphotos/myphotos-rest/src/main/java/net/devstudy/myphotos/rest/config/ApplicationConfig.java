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

package net.devstudy.myphotos.rest.config;

import io.swagger.jaxrs.config.BeanConfig;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import static net.devstudy.myphotos.rest.Constants.CURRENT_VERSION;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@ApplicationPath(CURRENT_VERSION)
public class ApplicationConfig extends Application {

    public ApplicationConfig() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setTitle("MyPhotos.com API (Version 1.0)");
        beanConfig.setBasePath("/v1");
        beanConfig.setResourcePackage("net.devstudy.myphotos.rest");
        
        beanConfig.setPrettyPrint(true);
        beanConfig.setLicenseUrl("http://www.apache.org/licenses/LICENSE-2.0");
        beanConfig.setScan(true);
    }
}
