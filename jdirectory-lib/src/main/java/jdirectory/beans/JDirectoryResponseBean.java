package jdirectory.beans;

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

    private Map<String, List<String>> response;

    public Map<String, List<String>> getResponse() {
        return response;
    }

    public void setResponse(Map<String, List<String>> response) {
        this.response = response;
    }
}
