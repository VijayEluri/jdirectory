package jdirectory.tags;

/**
 * Represents an enumeration of velocity context attributes.
 *
 * @author Alexander Yurinsky
 */
public enum ContextAttribute {
    JDIR_SERVLET_PATH("jdirServletPath"),
    JDIR_TREE("jdirTree"),
    NODE_TYPE("nodeType"),
    NODE("node");

    private String name;

    private ContextAttribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
