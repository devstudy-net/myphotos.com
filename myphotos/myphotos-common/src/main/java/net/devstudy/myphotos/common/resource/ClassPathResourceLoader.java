package net.devstudy.myphotos.common.resource;

import java.io.InputStream;

import javax.enterprise.context.ApplicationScoped;

import net.devstudy.myphotos.exception.ConfigException;

/**
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@ApplicationScoped
public class ClassPathResourceLoader implements ResourceLoader {

    @Override
    public boolean isSupport(String resourceName) {
        return resourceName.startsWith("classpath:");
    }

    @Override
    public InputStream getInputStream(String resourceName) {
        String classPathResourceName = resourceName.replace("classpath:", "");
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader != null) {
            InputStream inputStream = classLoader.getResourceAsStream(classPathResourceName);
            if (inputStream != null) {
                return inputStream;
            }
        }
        throw new ConfigException("Classpath resource not found: " + classPathResourceName);
    }
}
