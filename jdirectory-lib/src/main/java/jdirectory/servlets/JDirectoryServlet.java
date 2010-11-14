package jdirectory.servlets;

import jdirectory.actions.JDirectoryAction;
import jdirectory.beans.JDirectoryRequestBean;
import jdirectory.beans.JDirectoryResponseBean;
import jdirectory.core.TreeNode;
import jdirectory.core.UnsupportedScanTargetException;
import jdirectory.util.Constants;
import jdirectory.util.Converter;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;

/**
 * Main servlet which responds to JDirectory tree's AJAX requests.
 *
 * @author Alexander Yurinsky
 */
public class JDirectoryServlet extends HttpServlet {
    private static final String RESOURCE_REQUEST_PATH = "/resource/";
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void init() throws ServletException {
        String initParameter = getServletContext().getInitParameter(Constants.ROOT_DIRECTORY_CONTEXT_PARAMETER);
        if (initParameter == null) {
            throw new ServletException("Root directory initialization parameter must be set: " +
                    Constants.ROOT_DIRECTORY_CONTEXT_PARAMETER);
        }
        initParameter = getServletContext().getInitParameter(Constants.JDIRECTORY_SERVLET_PATH_PARAMETER);
        if (initParameter == null) {
            throw new ServletException("JDirectory servlet path initialization parameter must be set: " +
                    Constants.JDIRECTORY_SERVLET_PATH_PARAMETER);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        Writer writer = null;
        try {
            JDirectoryRequestBean requestBean = Converter.getInstance()
                    .populateRequestBean(JDirectoryRequestBean.class, request);
            requestBean.setRootDirectoryPath(getServletContext().getInitParameter(
                    Constants.ROOT_DIRECTORY_CONTEXT_PARAMETER));
            writer = response.getWriter();
            try {
                JDirectoryAction action = new JDirectoryAction();
                JDirectoryResponseBean responseBean = action.perform(requestBean,
                        (TreeNode) request.getSession().getAttribute(Constants.CURRENT_TREE_NODE_ATTRIBUTE));
                Converter.getInstance().generateJSONResponse(responseBean, writer);
            } catch (UnsupportedScanTargetException e) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("response", "unsupported");
                writer.write(jsonObj.toJSONString());
            }
            writer.flush();
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && request.getPathInfo().startsWith(RESOURCE_REQUEST_PATH)) {
            pathInfo = pathInfo.substring(RESOURCE_REQUEST_PATH.length());
            URL resource = Thread.currentThread().getContextClassLoader().getResource(pathInfo);
            if (resource == null) {
                response.sendError(404);
                return;
            }
            response.setContentType(getServletContext().getMimeType(new File(resource.getFile()).getName()));
            // TODO: update the processing of cached request
            /*if (request.getHeader("Cache-Control") != null) {
                response.setStatus(304);
                return;
            }*/
            ServletOutputStream output = null;
            InputStream input = null;
            try {
                output = response.getOutputStream();
                input = resource.openStream();
                byte[] buff = new byte[response.getBufferSize()];
                int bytes;
                while ((bytes = input.read(buff)) >= 0) {
                    output.write(buff, 0, bytes);
                }
                output.flush();
            } finally {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            }
        }
    }
}
