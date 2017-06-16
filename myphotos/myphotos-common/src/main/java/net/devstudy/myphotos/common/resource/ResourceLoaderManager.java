package net.devstudy.myphotos.common.resource;

import java.io.IOException;
import java.io.InputStream;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import net.devstudy.myphotos.exception.ConfigException;

/**
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@ApplicationScoped
public class ResourceLoaderManager {

    @Inject
    @Any
    private Instance<ResourceLoader> resourceLoaders;

    public InputStream getResourceInputStream(String resourceName) throws IOException {
        for (ResourceLoader resourceLoader : resourceLoaders) {
            if (resourceLoader.isSupport(resourceName)) {
                return resourceLoader.getInputStream(resourceName);
            }
        }

        throw new ConfigException("Can't get input stream for resource: " + resourceName);
    }
}
