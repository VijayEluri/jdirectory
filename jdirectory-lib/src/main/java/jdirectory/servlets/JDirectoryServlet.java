package jdirectory.servlets;

import jdirectory.actions.JDirectoryAction;
import jdirectory.beans.JDirectoryRequestBean;
import jdirectory.beans.JDirectoryResponseBean;
import jdirectory.util.Constants;
import jdirectory.util.Converter;

import javax.servlet.ServletException;
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            JDirectoryRequestBean requestBean = Converter.getInstance()
                    .populateRequestBean(JDirectoryRequestBean.class, request);
            requestBean.setRootDirectoryPath(getServletConfig()
                    .getInitParameter(Constants.ROOT_DIRECTORY_CONTEXT_PARAMETER));
            JDirectoryAction action = new JDirectoryAction();
            JDirectoryResponseBean responseBean = action.perform(requestBean);
            Converter.getInstance().generateJSONResponse(responseBean, response.getOutputStream());
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
