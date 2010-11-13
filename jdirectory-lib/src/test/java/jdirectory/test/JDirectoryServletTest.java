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
                "{\"response\":{\"\\/\":[{\"name\":\"archive.zip\",\"type\":\"ARCHIVE\"}," +
                                        "{\"name\":\"baz.png\",\"type\":\"FILE\"}," +
                                        "{\"name\":\"foo.txt\",\"type\":\"FILE\"}," +
                                        "{\"name\":\"other\",\"type\":\"FILE\"}]}}");
    }

    @Test
    public void testZipArchive() throws Exception {
        FakeResponse response = new FakeResponse();
        servlet.service(new FakeRequest("/archive.zip!"), response);
        Assert.assertEquals(response.toString(),
                "{\"response\":{\"\\/archive.zip!\":[{\"name\":\"baz.png\",\"type\":\"FILE\"}," +
                                                    "{\"name\":\"foo.txt\",\"type\":\"FILE\"}," +
                                                    "{\"name\":\"other\",\"type\":\"FILE\"}]}}");
    }
}
