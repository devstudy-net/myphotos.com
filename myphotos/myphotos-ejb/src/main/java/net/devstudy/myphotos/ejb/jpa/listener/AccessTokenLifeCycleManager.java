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

package net.devstudy.myphotos.ejb.jpa.listener;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import net.devstudy.myphotos.exception.InvalidWorkFlowException;
import net.devstudy.myphotos.model.domain.AccessToken;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class AccessTokenLifeCycleManager {

    private final Logger logger = Logger.getLogger(AccessTokenLifeCycleManager.class.getName());

    @PrePersist
    public void setToken(AccessToken model) {
        model.setToken(UUID.randomUUID().toString().replace("-", ""));
        logger.log(Level.FINE, "Generate new uid token {0} for entity {1}", new Object[]{model.getToken(), model.getClass()});
    }
    
    @PreUpdate
    public void rejectUpdate(AccessToken model) {
        throw new InvalidWorkFlowException("AccessToken is not updatedable");
    }
}
