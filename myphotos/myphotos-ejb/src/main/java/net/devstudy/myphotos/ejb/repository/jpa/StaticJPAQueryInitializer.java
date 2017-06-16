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

package net.devstudy.myphotos.ejb.repository.jpa;

import static java.lang.annotation.ElementType.METHOD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import org.apache.commons.lang3.reflect.MethodUtils;

/**
 * 
 * 
 * @author devstudy
 * @see http://devstudy.net
 */
@Singleton
@Startup
public class StaticJPAQueryInitializer {

    @Inject
    private JPARepositoryFinder jPARepositoryFinder;

    @Inject
    private JPAQueryParser jPAQueryParser;

    @Inject
    private JPAQueryCreator jPAQueryCreator;

    @PostConstruct
    private void postConstruct() {
        Set<Class<?>> jpaRepositoryClasses = jPARepositoryFinder.getJPARepositoryClasses();
        Map<String, String> namedQueriesMap = jPAQueryParser.getNamedQueriesMap(jpaRepositoryClasses);
        jPAQueryCreator.allAllNamedQueries(namedQueriesMap);
    }

    /**
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    @Retention(RUNTIME)
    @Target({METHOD})
    public @interface JPAQuery {

        String name() default "";

        String value();
    }

    /**
     *
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    @Dependent
    public static class JPARepositoryFinder {

        @Inject
        protected Logger logger;

        @Inject
        protected BeanManager beanManager;

        protected Class<?> getRepositoryClass() {
            return AbstractJPARepository.class;
        }

        protected boolean isCandidateValid(Bean<?> bean) {
            return true;
        }

        public Set<Class<?>> getJPARepositoryClasses() {
            Set<Class<?>> result = new HashSet<>();
            for (Bean<?> bean : beanManager.getBeans(Object.class, new AnnotationLiteral<Any>() {})) {
                Class<?> beanClass = bean.getBeanClass();
                if (isCandidateValid(bean) && getRepositoryClass().isAssignableFrom(beanClass)) {
                    result.add(beanClass);
                    logger.log(Level.INFO, "Found {0} JPA repository class", beanClass.getName());
                }
            }
            return result;
        }
    }

    /**
     *
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    @Dependent
    public static class JPAQueryParser {

        public Map<String, String> getNamedQueriesMap(Set<Class<?>> jpaRepositoryClasses) {
            Map<String, String> namedQueriesMap = new HashMap<>();
            for (Class<?> jpaRepositoryClass : jpaRepositoryClasses) {
                addQueriesFromJPARepository(jpaRepositoryClass, namedQueriesMap);
            }
            return namedQueriesMap;
        }

        protected void addQueriesFromJPARepository(Class<?> jpaRepositoryClass, Map<String, String> namedQueriesMap) {
            for (Method method : MethodUtils.getMethodsWithAnnotation(jpaRepositoryClass, JPAQuery.class)) {
                JPAQuery jPAQuery = method.getAnnotation(JPAQuery.class);
                String key = jPAQuery.name();
                if (key.isEmpty()) {
                    key = getDefaultKey(jpaRepositoryClass, method);
                }
                String query = jPAQuery.value();
                if (namedQueriesMap.put(key, query) != null) {
                    throw new IllegalStateException("Detected named query duplicates: " + key);
                }
            }
        }

        protected String getDefaultKey(Class<?> jpaRepositoryClass, Method method) {
            return String.format("%s.%s", getEntityClass(jpaRepositoryClass), method.getName());
        }

        protected String getEntityClass(Class<?> jpaRepositoryClass) {
            Type type = jpaRepositoryClass;
            while (type != null) {
                if (type instanceof ParameterizedType) {
                    return ((Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0]).getSimpleName();
                }
                type = ((Class<?>) type).getGenericSuperclass();
            }
            throw new IllegalArgumentException("JPA class " + jpaRepositoryClass + " is not generic class");
        }
    }

    /**
     *
     *
     * @author devstudy
     * @see http://devstudy.net
     */
    @Dependent
    public static class JPAQueryCreator {

        @Inject
        protected Logger logger;

        @PersistenceUnit
        protected EntityManagerFactory entityManagerFactory;

        protected void allAllNamedQueries(Map<String, String> namedQueriesMap) {
            EntityManager em = entityManagerFactory.createEntityManager();
            try {
                for (Map.Entry<String, String> entry : namedQueriesMap.entrySet()) {
                    try {
                        entityManagerFactory.addNamedQuery(entry.getKey(), em.createQuery(entry.getValue()));
                        logger.log(Level.FINE, "Added named query: {0} -> {1}", new Object[]{entry.getKey(), entry.getValue()});
                    } catch (RuntimeException e) {
                        throw new RuntimeException("Invalid query: "+entry.getKey(), e);
                    }
                }
            } finally {
                em.close();
            }
        }
    }
}
