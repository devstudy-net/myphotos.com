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

package net.devstudy.myphotos.rest.errormapper;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import net.devstudy.myphotos.rest.model.ErrorMessageREST;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Provider
@ApplicationScoped
public class JAXRSExceptionMapper implements ExceptionMapper<WebApplicationException>{

    @Inject
    private Logger logger;
    
    @Override
    public Response toResponse(WebApplicationException exception) {
        logError(exception);
        return Response.
                status(exception.getResponse().getStatusInfo()).
                entity(new ErrorMessageREST(exception.getMessage())).
                type(MediaType.APPLICATION_JSON).
                build();
    }

    private void logError(WebApplicationException exception) {
        if(exception.getResponse().getStatus() >= 500) {
            logger.log(Level.SEVERE, exception.getMessage(), exception);
        } else {
            logger.log(Level.SEVERE, exception.getMessage());
        }
    }
}
