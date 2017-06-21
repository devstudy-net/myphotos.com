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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@ApiModel("ValidationItem")
public class ValidationItemREST {

    private String field;

    private List<String> messages;

    public ValidationItemREST() {
    }

    public ValidationItemREST(String field, List<String> messages) {
        this.field = field;
        this.messages = messages;
    }

    @ApiModelProperty(required = true, value = "Parameter name")
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @ApiModelProperty(required = true, value = "String error message list")
    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
