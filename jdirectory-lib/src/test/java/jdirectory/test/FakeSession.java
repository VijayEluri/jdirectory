package jdirectory.test;

import jdirectory.core.*;
import jdirectory.util.Constants;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;

/**
 * Represents a simple test HTTP session.
 *
 * @author Alexander Yurinsky
 */
public class FakeSession implements HttpSession {
    private TreeNode root;

    public FakeSession(ServletContext context) throws Exception {
        String rootDirectory = context.getInitParameter(
                Constants.ROOT_DIRECTORY_CONTEXT_PARAMETER);
        FilesystemItem[] items = DirectoryScannerFactory.getInstance().getScanner(rootDirectory, "/").scan();
        root = new TreeNode(new FilesystemItem("/", FilesystemItemType.DIRECTORY), null);
        root.setExpanded(true);
        for (FilesystemItem item : items) {
            root.addChild(new TreeNode(item, root));
        }
    }

    @Override
    public long getCreationTime() {
        return 0;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public long getLastAccessedTime() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int interval) {
      
    }

    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getAttribute(String name) {
        return name.equals(Constants.CURRENT_TREE_NODE_ATTRIBUTE) ? root : null;
    }

    @Override
    public Object getValue(String name) {
        return null;
    }

    @Override
    public Enumeration getAttributeNames() {
        return null;
    }

    @Override
    public String[] getValueNames() {
        return new String[0];
    }

    @Override
    public void setAttribute(String name, Object value) {
      
    }

    @Override
    public void putValue(String name, Object value) {
      
    }

    @Override
    public void removeAttribute(String name) {
      
    }

    @Override
    public void removeValue(String name) {
      
    }

    @Override
    public void invalidate() {
      
    }

    @Override
    public boolean isNew() {
        return false;
    }
}
