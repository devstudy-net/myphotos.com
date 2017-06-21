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

import java.util.Collections;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import static net.devstudy.myphotos.web.security.SecurityUtils.PROFILE_ROLE;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
public class ProfileRealm extends AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof ProfileAuthenticationToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if(principals.getPrimaryPrincipal() instanceof ProfileId) {
            return new SimpleAuthorizationInfo(Collections.singleton(PROFILE_ROLE));
        } else {
            return null;
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        ProfileAuthenticationToken token = (ProfileAuthenticationToken) authenticationToken;
        return new SimpleAuthenticationInfo(token.getPrincipal(), token.getCredentials(), ProfileRealm.class.getSimpleName());
    }

    @Override
    protected Object getAvailablePrincipal(PrincipalCollection principals) {
        Object principal = super.getAvailablePrincipal(principals);
        if (principal instanceof ProfileId) {
            return ((ProfileId) principal).getEmail();
        }
        return principal;
    }
}
