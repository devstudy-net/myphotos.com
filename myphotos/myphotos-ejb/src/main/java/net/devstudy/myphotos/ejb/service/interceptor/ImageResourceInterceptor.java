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

package net.devstudy.myphotos.ejb.service.interceptor;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import net.devstudy.myphotos.model.ImageResource;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Interceptor
public class ImageResourceInterceptor {

    @AroundInvoke
    public Object aroundProcessImageResource(InvocationContext ic) throws Exception {
        try(ImageResource imageResource = (ImageResource) ic.getParameters()[0]) {
            return ic.proceed();
        }
    }
}
