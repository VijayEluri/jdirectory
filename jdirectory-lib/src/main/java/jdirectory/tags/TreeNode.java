package jdirectory.tags;

import jdirectory.core.FilesystemItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the filesystem tree node.
 *
 * @author Alexander Yurinsky
 */
public class TreeNode {
    private TreeNode parent;
    private List<TreeNode> children = new ArrayList<TreeNode> ();
    private FilesystemItem item;

    /**
     * Creates a new instance of {@link TreeNode} with provided title.
     *
     * @param item An instance of {@link jdirectory.core.FilesystemItem}.
     */
    public TreeNode(FilesystemItem item) {
        this.item = item;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
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

    public void setItem(FilesystemItem item) {
        this.item = item;
    }
}
