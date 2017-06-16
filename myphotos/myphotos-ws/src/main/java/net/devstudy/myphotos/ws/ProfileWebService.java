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

package net.devstudy.myphotos.ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import net.devstudy.myphotos.ws.model.ProfileSOAP;
import net.devstudy.myphotos.ws.model.ProfilePhotosSOAP;

/**
 * http://soap.myphotos.com/ws/ProfileService?wsdl
 * http://soap.myphotos.com/ws/ProfileService?Tester
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@WebService(targetNamespace = "http://soap.myphotos.com/ws/ProfileService?wsdl")
public interface ProfileWebService {
    
    @WebMethod
    @WebResult(name = "profile") 
    public ProfileSOAP findById(
            @WebParam(name = "id") Long id,
            @WebParam(name = "withPhotos") boolean withPhotos,
            @WebParam(name = "limit") int limit);
    
    @WebMethod
    @WebResult(name = "profilePhotos") 
    public ProfilePhotosSOAP findProfilePhotos(
            @WebParam(name = "profileId") Long profileId,
            @WebParam(name = "page") int page,
            @WebParam(name = "limit") int limit);
}
