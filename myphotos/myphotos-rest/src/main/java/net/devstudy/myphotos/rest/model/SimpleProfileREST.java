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
import javax.xml.bind.annotation.XmlType;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@XmlType(name = "")
@ApiModel("SimpleProfile")
public class SimpleProfileREST {

    private Long id;

    private String uid;

    @ApiModelProperty(required = true, value = "Profile id. Uses as unique identificator of profile via rest api")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ApiModelProperty(required = true, value = "Profile uid. Can be useful if user wants to open profile via browser from his mobile application. Profile unique url will be http://myphotos.com/${uid}")
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
