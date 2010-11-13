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
    private String rootDirectoryPath = Thread.currentThread().getContextClassLoader()
            .getResource("testDirectory").getFile();
    private FakeServletContext servletContext = new FakeServletContext();
    private ServletConfig servletConfig = new ServletConfig() {
            @Override
            public String getServletName() {
                return null;
            }
            @Override
            public ServletContext getServletContext() {
                return null;
            }
            @Override
            public String getInitParameter(String name) {
                return name.equals(Constants.ROOT_DIRECTORY_CONTEXT_PARAMETER) ? rootDirectoryPath : null;
            }
            @Override
            public Enumeration getInitParameterNames() {
                return null;
            }
        };

    @Override
    public ServletConfig getServletConfig() {
        return servletConfig;
    }

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
