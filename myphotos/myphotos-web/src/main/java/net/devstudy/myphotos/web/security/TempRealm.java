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
import static net.devstudy.myphotos.web.security.SecurityUtils.TEMP_PASS;
import static net.devstudy.myphotos.web.security.SecurityUtils.TEMP_ROLE;
import org.apache.shiro.authc.Account;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import static net.devstudy.myphotos.web.security.SecurityUtils.TEMP_PROFILE;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class TempRealm extends AuthorizingRealm {
    
    private static final Account ACCOUNT = new SimpleAccount(
                new SimplePrincipalCollection(TEMP_PROFILE, TempRealm.class.getSimpleName()), 
                TEMP_PASS, 
                Collections.singleton(TEMP_ROLE));

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof TempAuthentificationToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        return ACCOUNT;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if(TEMP_PROFILE.equals(principals.getPrimaryPrincipal())) {
            return ACCOUNT;
        } else {
            return null;
        }
    }
}
