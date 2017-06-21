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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.enterprise.inject.Vetoed;

/**
 *
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@Vetoed
public class ConverterUtils {

    public static boolean isAnnotationPresent(Class<? extends Annotation> annotationClass, Object bean, String propertyName) {
        return  isAnnotationPresentOnGetter(annotationClass, bean, propertyName)
                || 
                isAnnotationPresentOnField(annotationClass, bean, propertyName);
    }

    private static boolean isAnnotationPresentOnGetter(Class<? extends Annotation> annotationClass, Object bean, String propertyName) {
        Class<?> cl = bean.getClass();
        String[] methodNames = getGetterMethodNames(propertyName);
        while (cl != null) {
            for (String methodName : methodNames) {
                try {
                    Method method = cl.getDeclaredMethod(methodName);
                    if (method.isAnnotationPresent(annotationClass)) {
                        return true;
                    }
                } catch (NoSuchMethodException e) {
                    // do nothing
                }
            }
            cl = cl.getSuperclass();
        }
        return false;
    }

    private static boolean isAnnotationPresentOnField(Class<? extends Annotation> annotationClass, Object bean, String propertyName) {
        Class<?> cl = bean.getClass();
        while (cl != null) {
            try {
                Field field = cl.getDeclaredField(propertyName);
                if (field.isAnnotationPresent(annotationClass)) {
                    return true;
                }
            } catch (NoSuchFieldException ex) {
                // do nothing
            }
            cl = cl.getSuperclass();
        }
        return false;
    }

    private static String[] getGetterMethodNames(String propertyName) {
        if (propertyName.length() > 1) {
            return new String[]{
                "get" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1),
                "is" + Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1)
            };
        } else {
            return new String[]{
                "get" + Character.toUpperCase(propertyName.charAt(0)),
                "is" + Character.toUpperCase(propertyName.charAt(0))
            };
        }
    }
}
