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

import net.devstudy.myphotos.rest.model.ErrorMessageREST;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import static net.devstudy.myphotos.rest.StatusMessages.INTERNAL_ERROR;

/**
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@Provider
@ApplicationScoped
public class DefaultExceptionMapper implements ExceptionMapper<Throwable>{
    
    @Inject
    private Logger logger;

    @Override
    public Response toResponse(Throwable exception) {
        logger.log(Level.SEVERE, INTERNAL_ERROR, exception);
        return Response.
                status(INTERNAL_SERVER_ERROR).
                entity(new ErrorMessageREST(INTERNAL_ERROR)).
                type(MediaType.APPLICATION_JSON).
                build();
    }
}
