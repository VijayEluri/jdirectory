package jdirectory.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a bean that contains a set of local paths which point to the
 * directories or archives that should be scanned.
 *
 * @author Alexander Yurinsky
 */
public class JDirectoryRequestBean implements Serializable {
    private static final long serialVersionUID = -1241000045478810050L;

    private String paths;
    private String rootDirectoryPath;

    public String getPaths() {
        return paths;
    }

    public void setPaths(String paths) {
        this.paths = paths;
    }

    public String getRootDirectoryPath() {
        return rootDirectoryPath;
    }

    public void setRootDirectoryPath(String rootDirectoryPath) {
        this.rootDirectoryPath = rootDirectoryPath;
    }

    /**
     * Gets a set of local paths which point to the
     * directories or archives that should be scanned.
     *
     * @return A set of local paths.
     */
    public String[] getLocalPaths() {
        return paths != null ? paths.split(";") : new String[0];
    }
}
