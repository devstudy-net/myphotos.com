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
package net.devstudy.myphotos.common.converter.impl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import net.devstudy.myphotos.common.annotation.converter.ConvertAsURL;
import net.devstudy.myphotos.common.converter.ModelConverter;
import net.devstudy.myphotos.common.converter.UrlConveter;
import static net.devstudy.myphotos.common.converter.impl.ConverterUtils.isAnnotationPresent;
import net.devstudy.myphotos.exception.ConfigException;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@ApplicationScoped
public class DefaultModelConverter implements ModelConverter {

    @Inject
    private UrlConveter urlConveter;

    @Override
    public <S, D> D convert(S source, Class<D> destinationClass) {
        try {
            D result = destinationClass.newInstance();
            copyProperties(result, Objects.requireNonNull(source, "Source can't be null"));
            return result;
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            throw new ConfigException(String.format("Can't convert object from %s to %s: %s", source.getClass(), destinationClass, ex.getMessage()), ex);
        }
    }

    @Override
    public <S, D> List<D> convertList(List<S> source, Class<D> destinationClass) {
        List<D> result = new ArrayList<>(source.size());
        for (S item : source) {
            result.add(convert(item, destinationClass));
        }
        return result;
    }

    private <S, D> void copyProperties(D result, S source) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        PropertyUtilsBean propertyUtils = BeanUtilsBean.getInstance().getPropertyUtils();
        PropertyDescriptor[] propertyDescriptors = propertyUtils.getPropertyDescriptors(result);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String name = propertyDescriptor.getName();
            if (!"class".equals(name) && propertyUtils.isReadable(source, name) && propertyUtils.isWriteable(result, name)) {
                Object value = propertyUtils.getProperty(source, name);
                if (value != null) {
                    Object convertedValue = convertValue(propertyUtils, result, name, value);
                    propertyUtils.setProperty(result, name, convertedValue);
                }
            }
        }
    }

    private <D> Object convertValue(PropertyUtilsBean propertyUtils, D result, String name, Object value)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> destinationClass = propertyUtils.getPropertyType(result, name);
        if (destinationClass.isPrimitive() || value.getClass().isPrimitive()) {
            return value;
        } else if (isAnnotationPresent(ConvertAsURL.class, result, name)) {
            return urlConveter.convert(String.valueOf(value));
        } else if (value.getClass() != destinationClass) {
            return convert(value, destinationClass);
        }
        return value;
    }

}
