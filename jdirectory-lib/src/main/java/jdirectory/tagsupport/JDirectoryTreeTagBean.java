package jdirectory.tagsupport;

import java.io.Serializable;

/**
 * Represents a bean that helps displaying filesystem tree.
 * 
 * @author Alexander Yurinsky
 */
public class JDirectoryTreeTagBean implements Serializable {
    private static final long serialVersionUID = -2045464501112154500L;

    private String rootDirectoryPath;

    /**
     * Initializes helper bean parameters.
     */
    public JDirectoryTreeTagBean() {}

    public String getRootDirectoryPath() {
        return rootDirectoryPath;
    }

    public void setRootDirectoryPath(String rootDirectoryPath) {
        this.rootDirectoryPath = rootDirectoryPath;
    }
}
