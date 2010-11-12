package jdirectory.test;

import jdirectory.servlets.JDirectoryServlet;
import jdirectory.util.Constants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Tests server side functionality.
 * 
 * @author Alexander Yurinsky
 */
public class JDirectoryServletTest {
    private JDirectoryServlet servlet;

    @Before
    public void setUp() {
        final String rootDirectoryPath = Thread.currentThread().getContextClassLoader()
                .getResource("testDirectory").getFile();
        servlet = new JDirectoryServlet() {
            @Override
            public ServletConfig getServletConfig() {
                return new ServletConfig() {
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
            }

            @Override
            public void service(HttpServletRequest req, HttpServletResponse resp)
                    throws ServletException, IOException {
                super.doPost(req, resp);
            }
        };
    }
    
    @Test
    public void testPlainDirectory() throws Exception {
        FakeResponse response = new FakeResponse();
        servlet.service(new FakeRequest("/"), response);
        Assert.assertEquals(response.toString(),
                "{\"response\":{\"\\/\":[\"archive.zip\",\"baz.png\",\"foo.txt\",\"other\"]}}");
    }

    @Test
    public void testZipArchive() throws Exception {
        FakeResponse response = new FakeResponse();
        servlet.service(new FakeRequest("/archive.zip!"), response);
        Assert.assertEquals(response.toString(),
                "{\"response\":{\"\\/archive.zip!\":[\"baz.png\",\"foo.txt\",\"other\"]}}");
    }
}
