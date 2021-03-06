package jdirectory.util;

/**
 * Holds project scope constants.
 *
 * @author Alexander Yurinsky
 */
public interface Constants {
    /**
     * Root directory application scoped parameter name.
     */
    String ROOT_DIRECTORY_CONTEXT_PARAMETER = "jdirectory.root.directory.path";
    /**
     * JDirectory servlet path application scoped parameter name.
     */
    String JDIRECTORY_SERVLET_PATH_PARAMETER = "jdirectory.servlet.path";
    /**
     * A session key for currently used tree node structure.
     */
    String CURRENT_TREE_NODE_ATTRIBUTE = "jdirectory.current.tree.node";
}
