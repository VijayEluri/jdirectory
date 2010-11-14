package jdirectory.tags;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import java.io.InputStream;
import java.net.URL;

/**
 * Represents a custom instance of {@link org.apache.velocity.runtime.resource.loader.ResourceLoader}
 * which is responsible for loading velocity template files.
 * 
 * @author Alexander Yurinsky
 */
public class CustomResourceLoader extends ResourceLoader {
    private static final String VELOCITY_TEMPLATES_PATH = "META-INF/templates";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void init(ExtendedProperties extendedProperties) {}

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getResourceStream(String name) throws ResourceNotFoundException {
        try {
            URL resource = Thread.currentThread().getContextClassLoader()
                    .getResource(VELOCITY_TEMPLATES_PATH + "/" + name);
            return resource != null ? resource.openStream() : null;
        } catch (Exception e) {
            throw new ResourceNotFoundException("Unable to find resource: " + name);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSourceModified(Resource resource) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getLastModified(Resource resource) {
        return 0;
    }
}
