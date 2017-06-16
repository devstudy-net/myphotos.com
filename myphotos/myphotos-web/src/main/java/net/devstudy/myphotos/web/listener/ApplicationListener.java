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

package net.devstudy.myphotos.web.listener;

import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import net.devstudy.myphotos.common.annotation.qualifier.GooglePlus;
import net.devstudy.myphotos.service.SocialService;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@WebListener
public class ApplicationListener implements ServletContextListener{

    @Inject
    private Logger logger;
    
    @Inject
    @GooglePlus
    private SocialService googlePlusSocialService;
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("googlePlusClientId", googlePlusSocialService.getClientId());
        logger.info("Application 'myphotos' initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Application 'myphotos' destroyed");
    }

}
