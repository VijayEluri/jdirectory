package jdirectory.beans;

import jdirectory.core.FilesystemItem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Represents a bean that contains a set of directory scan results
 * which are used as a response to the client.
 *
 * @author Alexander Yurinsky
 */
public class JDirectoryResponseBean implements Serializable {
    private static final long serialVersionUID = 5431045488778777771L;

    private Map<String, List<FilesystemItem>> response;

    public Map<String, List<FilesystemItem>> getResponse() {
        return response;
    }

    public void setResponse(Map<String, List<FilesystemItem>> response) {
        this.response = response;
    }
}
