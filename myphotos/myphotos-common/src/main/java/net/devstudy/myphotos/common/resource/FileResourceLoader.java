package net.devstudy.myphotos.common.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author devstudy
 * @see http://devstudy.net
 */
@ApplicationScoped
public class FileResourceLoader implements ResourceLoader {

    @Override
    public boolean isSupport(String resourceName) {
        try {
            return new File(resourceName).exists();
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public InputStream getInputStream(String resourceName) throws IOException {
        return new FileInputStream(resourceName);
    }
}
