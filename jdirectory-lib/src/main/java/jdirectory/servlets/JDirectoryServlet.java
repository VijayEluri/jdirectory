package jdirectory.servlets;

import jdirectory.actions.JDirectoryAction;
import jdirectory.beans.JDirectoryRequestBean;
import jdirectory.beans.JDirectoryResponseBean;
import jdirectory.util.Constants;
import jdirectory.util.Converter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Main servlet which responds to JDirectory tree's AJAX requests.
 *
 * @author Alexander Yurinsky
 */
public class JDirectoryServlet extends HttpServlet {
    /**
     * {@inheritDoc}
     */
    @Override
    public void init() throws ServletException {
        String initParameter = getServletConfig().getInitParameter(Constants.ROOT_DIRECTORY_CONTEXT_PARAMETER);
        if (initParameter == null) {
            throw new ServletException("Root directory initialization parameter must be set: " +
                    Constants.ROOT_DIRECTORY_CONTEXT_PARAMETER);
        }
        getServletContext().setAttribute(Constants.ROOT_DIRECTORY_CONTEXT_PARAMETER, initParameter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletOutputStream stream = null;
        try {
            JDirectoryRequestBean requestBean = Converter.getInstance()
                    .populateRequestBean(JDirectoryRequestBean.class, request);
            requestBean.setRootDirectoryPath((String) getServletContext().getAttribute(
                    Constants.ROOT_DIRECTORY_CONTEXT_PARAMETER));
            JDirectoryAction action = new JDirectoryAction();
            JDirectoryResponseBean responseBean = action.perform(requestBean);
            stream = response.getOutputStream();
            Converter.getInstance().generateJSONResponse(responseBean, stream);
            stream.flush();
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
}
