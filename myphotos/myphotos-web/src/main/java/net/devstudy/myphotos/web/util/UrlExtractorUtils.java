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

package net.devstudy.myphotos.web.util;

import net.devstudy.myphotos.exception.ValidationException;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public final class UrlExtractorUtils {

    public static String getPathVariableValue(String url, String preffix, String suffix) {
        if(url.length() >= preffix.length() + suffix.length() && url.startsWith(preffix) && url.endsWith(suffix)) {
            return url.substring(preffix.length(), url.length() - suffix.length());
        } else {
            throw new ValidationException(String.format("Can't extract path variable from url=%s with preffix=%s and suffuix=%s", url, preffix, suffix));    
        }
    }
    
    private UrlExtractorUtils() {}
}
