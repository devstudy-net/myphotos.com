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

package net.devstudy.myphotos.ws.error;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.soap.SOAPFaultException;
import net.devstudy.myphotos.exception.BusinessException;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@Interceptor
public class ExceptionMapperInterceptor {

    @Inject
    private Logger logger;

    @AroundInvoke
    public Object aroundProcessImageResource(InvocationContext ic) throws Exception {
        validateParameters(ic.getParameters());
        try {
            return ic.proceed();
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                logger.log(Level.WARNING, "Ws request failed: {0}", e.getMessage());
                throw new SOAPFaultException(createSOAPFault(e.getMessage(), true));
            } else {
                logger.log(Level.SEVERE, "Ws request failed: " + e.getMessage(), e);
                throw new SOAPFaultException(createSOAPFault("Internal error", false));
            }
        }
    }

    private void validateParameters(Object[] parameters) throws SOAPException {
        for (int i = 0; i < parameters.length; i++) {
            if(parameters[i] == null) {
                throw new SOAPFaultException(createSOAPFault("Parameter ["+i+"] is invalid", true));
            }
        }
    }
    
    private SOAPFault createSOAPFault(String message, boolean isClientError) throws SOAPException {
        String errorType = isClientError ? "Client" : "Server";
        SOAPFactory soapFactory = SOAPFactory.newInstance();
        return soapFactory.createFault(message, new QName("http://schemas.xmlsoap.org/soap/envelope/", errorType, ""));
    }
}

