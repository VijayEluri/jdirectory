package jdirectory.core;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * Represents a filesystem item of particular type: file, directory or archive
 * which are sufficient for application.
 *
 * @author Alexander Yurinsky
 */
public class FilesystemItem implements Comparable<FilesystemItem>, JSONAware {
    private String name;
    private FilesystemItemType type;

    /**
     * Constructs an instance of filesystem item.
     *
     * @param name The item's name.
     * @param type The item's type.
     */
    public FilesystemItem(String name, FilesystemItemType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public FilesystemItemType getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(FilesystemItem o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilesystemItem that = (FilesystemItem) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (type == that.type) return true;
        else return false;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + type.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public String toJSONString() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("name", this.name);
        jsonObj.put("type", this.type.name());
        return jsonObj.toJSONString();
    }
}
