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

package net.devstudy.myphotos.common.cdi.extension;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.logging.Logger;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

/**
 * https://docs.jboss.org/weld/reference/latest/en-US/html/extend.html
 *
 * @author devstudy
 * @see http://devstudy.net
 */
public class PrintAllDiscoveredBeansExtension implements Extension {

    private final Logger logger = Logger.getLogger(getClass().getName());

    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> processAnnotatedType, BeanManager bm) {
        String className = processAnnotatedType.getAnnotatedType().getJavaClass().getName();
        if (className.startsWith("net.devstudy")) {
            Set<Annotation> annotations = processAnnotatedType.getAnnotatedType().getAnnotations();
            logger.info(String.format("Discovered bean of %s with annotations: %s", className, annotations));
        }
    }
}
