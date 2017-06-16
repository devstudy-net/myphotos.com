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

import static net.devstudy.myphotos.web.security.SecurityUtils.TEMP_PASS;
import org.apache.shiro.authc.AuthenticationToken;
import static net.devstudy.myphotos.web.security.SecurityUtils.TEMP_PROFILE;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
public class TempAuthentificationToken implements AuthenticationToken {
	private static final long serialVersionUID = -3307991499732406972L;

	@Override
    public Object getPrincipal() {
        return TEMP_PROFILE;
    }

    @Override
    public Object getCredentials() {
        return TEMP_PASS;
    }
}
