package jdirectory.actions;

import jdirectory.beans.JDirectoryRequestBean;
import jdirectory.beans.JDirectoryResponseBean;
import jdirectory.core.DirectoryScannerFactory;
import jdirectory.core.FilesystemItem;
import jdirectory.core.UnsupportedScanTargetException;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander Yurinsky
 */
public class JDirectoryAction {
    public JDirectoryResponseBean perform(JDirectoryRequestBean requestBean)
            throws ActionException, UnsupportedScanTargetException {
        try {
            JDirectoryResponseBean response = new JDirectoryResponseBean();
            Map<String, List<FilesystemItem>> responseMap = new HashMap<String, List<FilesystemItem>> ();
            for (String localPath : requestBean.getLocalPaths()) {
                responseMap.put(localPath, Arrays.asList(DirectoryScannerFactory.getInstance()
                        .getScanner(requestBean.getRootDirectoryPath(), localPath).scan()));
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
}
