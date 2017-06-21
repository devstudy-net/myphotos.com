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
package net.devstudy.myphotos.web.security;


import org.apache.shiro.subject.Subject;

import net.devstudy.myphotos.model.domain.Profile;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
public class SecurityUtils {
		
    static final String TEMP_PROFILE = "";
    
    static final String TEMP_PASS = "";
    
    static final String TEMP_ROLE = "TEMP";
    
    static final String PROFILE_ROLE = "PROFILE";
    
    public static void logout() {
        Subject currentSubject = org.apache.shiro.SecurityUtils.getSubject();
        currentSubject.logout();
    }

    public static void authentificate(Profile profile) {
        Subject currentSubject = org.apache.shiro.SecurityUtils.getSubject();
        currentSubject.login(new ProfileAuthenticationToken(profile));
    }

    public static void authentificate() {
        Subject currentSubject = org.apache.shiro.SecurityUtils.getSubject();
        currentSubject.login(new TempAuthentificationToken());
    }
    
    public static boolean isAuthenticated() {
        Subject currentSubject = org.apache.shiro.SecurityUtils.getSubject();
        return currentSubject.isAuthenticated();
    }
    
    public static boolean isTempAuthenticated() {
        Subject currentSubject = org.apache.shiro.SecurityUtils.getSubject();
        return currentSubject.isAuthenticated() && TEMP_PROFILE.equals(currentSubject.getPrincipal());
    }
    
    public static ProfileId getCurrentProfileId() {
        Subject currentSubject = org.apache.shiro.SecurityUtils.getSubject();
        if(currentSubject.isAuthenticated()) {
            return (ProfileId)currentSubject.getPrincipal();
        } else {
            throw new IllegalStateException("Current subject is not authenticated");
        }
    }
}
