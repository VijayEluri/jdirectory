package jdirectory.tags;

/**
 * Represents an enumeration of velocity template files.
 * 
 * @author Alexander Yurinsky
 */
public enum TemplateFile {
    HEADER("header.vm"),
    SUB_TREE_START("subtree-start.vm"),
    SUB_TREE_END("subtree-end.vm"),
    TREE_ITEM("tree-item.vm");

    private String name;

    private TemplateFile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
