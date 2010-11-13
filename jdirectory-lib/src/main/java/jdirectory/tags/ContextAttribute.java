package jdirectory.tags;

/**
 * Represents an enumeration of velocity context attributes.
 *
 * @author Alexander Yurinsky
 */
public enum ContextAttribute {
    DIRECTORY_NAME("directoryName"),
    ITEM_NAME("itemName");

    private String name;

    private ContextAttribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
