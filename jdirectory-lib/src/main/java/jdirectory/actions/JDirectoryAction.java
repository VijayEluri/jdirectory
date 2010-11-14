package jdirectory.actions;

import jdirectory.beans.JDirectoryRequestBean;
import jdirectory.beans.JDirectoryResponseBean;
import jdirectory.core.DirectoryScannerFactory;
import jdirectory.core.FilesystemItem;
import jdirectory.core.TreeNode;
import jdirectory.core.UnsupportedScanTargetException;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An action which scans current directory or archive for items.
 *
 * @author Alexander Yurinsky
 */
public class JDirectoryAction {
    /**
     * Performs an action which scans specified filesystem directory and updates view
     * state with obtained results.
     *
     * @param requestBean An instance of request bean that contains request parameters.
     * @param treeNode An instance of the tree that stores current client's view state.
     * @return An instance of response bean that contains obtained results.
     * @throws ActionException If an error has encountered while operation.
     * @throws UnsupportedScanTargetException If scanning of the requested filesystem item is unsupported.
     */
    public JDirectoryResponseBean perform(JDirectoryRequestBean requestBean, TreeNode treeNode)
            throws ActionException, UnsupportedScanTargetException {
        try {
            JDirectoryResponseBean response = new JDirectoryResponseBean();
            Map<String, List<FilesystemItem>> responseMap = new HashMap<String, List<FilesystemItem>> ();
            for (String localPath : requestBean.getLocalPaths()) {
                List<FilesystemItem> scanResults = null;
                boolean expanded = true;
                if (requestBean.getExpanded() == null) {
                    scanResults = Arrays.asList(DirectoryScannerFactory.getInstance()
                            .getScanner(requestBean.getRootDirectoryPath(), localPath).scan());
                    responseMap.put(localPath, scanResults);
                } else {
                    expanded = Boolean.TRUE.toString().equals(requestBean.getExpanded()); 
                }
                String[] paths = localPath.replace("!", "").split("/");
                if (paths.length == 0) {
                    paths = new String[]{"/"};
                } else {
                    paths[0] = "/"; // root node name
                }
                updateTreeNode(treeNode, paths, 0, scanResults, expanded);
            }
            response.setResponse(responseMap);
            return response;
        } catch (UnsupportedScanTargetException uste) {
            throw uste;
        } catch (Exception e) {
            throw new ActionException(MessageFormat.format("An error has encountered while performing " +
                    "main action, path string is: {0}", requestBean.getPaths()), e);
        }
    }

    /*
     * Updates current filesystem tree's view state.
     */
    private void updateTreeNode(TreeNode treeNode, String[] paths, int pathIdx,
                                List<FilesystemItem> scanResults, boolean expanded) throws ActionException {
        if (pathIdx == paths.length - 1) {
            treeNode.setExpanded(expanded);
            if (scanResults != null) {
                for (FilesystemItem item : scanResults) {
                    treeNode.addChild(new TreeNode(item, treeNode));
                }
            }
            return;
        }
        String message = "Unable to update filesystem items tree: " +
                "tree structure doesn't correspond to the requested local path";
        if (treeNode.getChildren().size() == 0) {
            throw new ActionException(message);
        }
        pathIdx++;
        for (TreeNode child : treeNode.getChildren()) {
            if (child.getItem().getName().equals(paths[pathIdx])) {
                updateTreeNode(child, paths, pathIdx, scanResults, expanded);
                return;
            }
        }
        throw new ActionException(message);
    }
}
