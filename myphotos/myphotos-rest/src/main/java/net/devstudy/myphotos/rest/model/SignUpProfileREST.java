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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlType;

import net.devstudy.myphotos.model.domain.Profile;
import net.devstudy.myphotos.model.validation.EnglishLanguage;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@XmlType(name="")
public class SignUpProfileREST extends AuthentificationCodeREST{
    private String firstName;
	
    private String lastName;
	
    private String jobTitle;
	
    private String location;
    
    public SignUpProfileREST() {
    }

    @NotNull(message = "{Profile.firstName.NotNull}")
    @Size(min = 1, message = "{Profile.firstName.Size}")
    @EnglishLanguage(withNumbers = false, withPunctuations = false, withSpecialSymbols = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @NotNull(message = "{Profile.lastName.NotNull}")
    @Size(min = 1, message = "{Profile.lastName.Size}")
    @EnglishLanguage(withNumbers = false, withPunctuations = false, withSpecialSymbols = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    @NotNull(message = "{Profile.jobTitle.NotNull}")
    @Size(min = 5, message = "{Profile.jobTitle.Size}")
    @EnglishLanguage(withSpecialSymbols = false)
    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    
    @NotNull(message = "{Profile.location.NotNull}")
    @Size(min = 5, message = "{Profile.location.Size}")
    @EnglishLanguage(withSpecialSymbols = false)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    public void copyToProfile(Profile profile) {
        profile.setFirstName(getFirstName());
        profile.setLastName(getLastName());
        profile.setJobTitle(getJobTitle());
        profile.setLocation(getLocation());
    }
}
