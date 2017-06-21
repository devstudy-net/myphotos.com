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
import net.devstudy.myphotos.common.annotation.converter.ConvertAsURL;

import javax.xml.bind.annotation.XmlType;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@XmlType(name = "")
@ApiModel("ProfilePhoto")
public class ProfilePhotoREST {

    private Long id;

    private String smallUrl;

    private long views;

    private long downloads;

    @ApiModelProperty(required = true, value = "Photo id. Uses as unique identificator for /preview and /download api")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ConvertAsURL
    @ApiModelProperty(required = true)
    public String getSmallUrl() {
        return smallUrl;
    }

    public void setSmallUrl(String smallUrl) {
        this.smallUrl = smallUrl;
    }

    @ApiModelProperty(required = true)
    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    @ApiModelProperty(required = true)
    public long getDownloads() {
        return downloads;
    }

    public void setDownloads(long downloads) {
        this.downloads = downloads;
    }
}
