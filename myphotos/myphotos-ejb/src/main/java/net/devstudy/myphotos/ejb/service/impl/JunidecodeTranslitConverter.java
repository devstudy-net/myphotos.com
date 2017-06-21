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

package net.devstudy.myphotos.ejb.service.impl;

import java.util.Map;
import java.util.Properties;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.devstudy.myphotos.common.annotation.cdi.PropertiesSource;
import net.devstudy.myphotos.ejb.service.TranslitConverter;
import net.sf.junidecode.Junidecode;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@ApplicationScoped
public class JunidecodeTranslitConverter implements TranslitConverter {

    private Properties properties;

    @Inject
    public void setProperties(@PropertiesSource("classpath:translit.properties") Properties properties) {
        this.properties = extendTranslitProperties(properties);
    }

    @Override
    public String translit(String text) {
        String result = executeCustomTranslit(text);
        return Junidecode.unidecode(result);
    }

    private String executeCustomTranslit(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            String ch = String.valueOf(text.charAt(i));
            sb.append(properties.getProperty(ch, ch));
        }
        return sb.toString();
    }

    private Properties extendTranslitProperties(final Properties properties) {
        Properties result = (Properties) properties.clone();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            if (value.length() <= 1) {
                result.setProperty(key.toUpperCase(), value.toUpperCase());
            } else {
                result.setProperty(key.toUpperCase(), Character.toUpperCase(value.charAt(0)) + value.substring(1));
            }
        }
        return result;
    }
}

