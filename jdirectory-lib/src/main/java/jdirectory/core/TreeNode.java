package jdirectory.core;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the filesystem tree node.
 *
 * @author Alexander Yurinsky
 */
public class TreeNode implements JSONAware {
    private String id;
    private TreeNode parent;
    private List<TreeNode> children = new ArrayList<TreeNode> ();
    private FilesystemItem item;
    private boolean expanded;

    /**
     * Creates a new instance of {@link TreeNode} with provided title.
     *
     * @param item An instance of {@link jdirectory.core.FilesystemItem}.
     * @param parent An instance of parent {@link TreeNode}.
     */
    public TreeNode(FilesystemItem item, TreeNode parent) {
        this.item = item;
        this.parent = parent;
        this.id = getParent() != null ? getParent().getId() + "-" + getParent().getChildren().size() : "0";
    }

    public String getId() {
        return id;
    }

    public TreeNode getParent() {
        return parent;
    }

    /**
     * Adds a new child instance of {@link TreeNode}.
     *
     * @param child A new instance of child.
     */
    public void addChild(TreeNode child) {
        children.add(child);
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public FilesystemItem getItem() {
        return item;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public String toJSONString() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("id", id);
        jsonObj.put("item", item);
        jsonObj.put("children", children);
        return jsonObj.toJSONString();
    }
}
