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
package net.devstudy.myphotos.rest.model;

import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@XmlType(name="")
public class ErrorMessageREST {

    private String message;
    
    private boolean userError;

    public ErrorMessageREST() {
    }

    public ErrorMessageREST(String message) {
        this(message, false);
    }

    public ErrorMessageREST(String message, boolean userError) {
        this.message = message;
        this.userError = userError;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUserError() {
        return userError;
    }

    public void setUserError(boolean userError) {
        this.userError = userError;
    }
}
