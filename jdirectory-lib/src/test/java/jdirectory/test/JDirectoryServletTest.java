package jdirectory.test;

import jdirectory.servlets.JDirectoryServlet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.ServletException;

/**
 * Tests server side functionality.
 * 
 * @author Alexander Yurinsky
 */
public class JDirectoryServletTest {
    private JDirectoryServlet servlet;

    @Before
    public void setUp() throws ServletException {
        servlet = new SimpleJDirectoryServlet();
        servlet.init();
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
