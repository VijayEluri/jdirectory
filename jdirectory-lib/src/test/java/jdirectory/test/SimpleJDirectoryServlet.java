package jdirectory.test;

import jdirectory.servlets.JDirectoryServlet;
import jdirectory.util.Constants;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Test implementation of {@link jdirectory.servlets.JDirectoryServlet}.
 * 
 * @author Alexander Yurinsky
 */
public class SimpleJDirectoryServlet extends JDirectoryServlet {
    private FakeServletContext servletContext = new FakeServletContext();

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
