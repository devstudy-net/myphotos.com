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

package net.devstudy.myphotos.common.producer;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import net.devstudy.myphotos.common.annotation.cdi.Property;
import net.devstudy.myphotos.exception.ConfigException;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Dependent
public class PropertyProducer {

    @Inject
    private ApplicationPropertiesStorage applicationPropertiesStorage;

    @Produces
    @Property
    public String resolveStringPropertyValue(InjectionPoint injectionPoint) {
        return resolvePropertyValue(injectionPoint);
    }
    
    @Produces
    @Property
    public int resolveIntPropertyValue(InjectionPoint injectionPoint) {
        return Integer.parseInt(resolvePropertyValue(injectionPoint));
    }
    
    @Produces
    @Property
    public boolean resolveBooleanPropertyValue(InjectionPoint injectionPoint) {
        return Boolean.parseBoolean(resolvePropertyValue(injectionPoint));
    }
    
    private String resolvePropertyValue(InjectionPoint injectionPoint) {
        String className = injectionPoint.getMember().getDeclaringClass().getName();
        String memberName = injectionPoint.getMember().getName();
        Property property = injectionPoint.getAnnotated().getAnnotation(Property.class);
        return resolvePropertyValue(className, memberName, property);
    }

    private String resolvePropertyValue(String className, String memberName, Property property) {
        String propertyName = getPropertyName(className, memberName, property);
        String propertyValue = applicationPropertiesStorage.getApplicationProperties().getProperty(propertyName);
        if(propertyValue == null) {
            throw new ConfigException(String.format("Can't resolve property: '%s' for injection to %s.%s", propertyName, className, memberName));
        } else {
            return propertyValue;
        }
    }

    private String getPropertyName(String className, String memberName, Property property) {
        String value = property.value();
        if("".equals(value)) {
            return String.format("%s.%s", className, memberName);
        } else {
            return value;
        }
    }
}
