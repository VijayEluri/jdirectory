package jdirectory.tags;

import jdirectory.core.*;
import jdirectory.util.Constants;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Properties;

/**
 * Represents a bean that helps displaying filesystem tree.
 * 
 * @author Alexander Yurinsky
 */
public class JDirectoryTreeTag extends TagSupport {
    private static final long serialVersionUID = -2045464501112154500L;
    private static final String VELOCITY_ENGINE_PAGE_ATTRIBUTE = "jdirectory.velocity.engine";

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPageContext(PageContext pageContext) {
        super.setPageContext(pageContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int doStartTag() throws JspException {
        try {
            initVelocityEngine();
            TreeNode root = createTree();
            displayHeader(root);
            displayNode(root);
        } catch (Exception e) {
            throw new JspException("Creation filesystem tree has failed", e);
        }
        return SKIP_BODY;
    }

    private void initVelocityEngine() throws Exception {
        Properties p = new Properties();
        p.setProperty("input.encoding", "UTF-8");
        p.setProperty("output.encoding", "UTF-8");
        p.setProperty("file.resource.loader.class", CustomResourceLoader.class.getName());
        VelocityEngine engine = new VelocityEngine();
        engine.init(p);
        pageContext.setAttribute(VELOCITY_ENGINE_PAGE_ATTRIBUTE, engine);
    }

    /*
     * Creates initial filesystem tree.
     */
    private TreeNode createTree() throws DirectoryScanException {
        TreeNode root = (TreeNode) pageContext.getSession().getAttribute(Constants.CURRENT_TREE_NODE_ATTRIBUTE);
        if (root != null) {
            return root;
        }
        String rootDirectory = pageContext.getServletContext().getInitParameter(
                Constants.ROOT_DIRECTORY_CONTEXT_PARAMETER);
        FilesystemItem[] items;
        try {
            items = DirectoryScannerFactory.getInstance().getScanner(rootDirectory, "/").scan();
        } catch (UnsupportedScanTargetException e) {
            throw new DirectoryScanException("Root directory path points to the filesystem" +
                    "item which is not supported: " + rootDirectory, e);
        }
        root = new TreeNode(new FilesystemItem("/", FilesystemItemType.DIRECTORY), null);
        root.setExpanded(true);
        for (FilesystemItem item : items) {
            root.addChild(new TreeNode(item, root));
        }
        pageContext.getSession().setAttribute(Constants.CURRENT_TREE_NODE_ATTRIBUTE, root);
        return root;
    }

    private void displayHeader(TreeNode node) throws Exception {
        Context context = new VelocityContext();
        String contextPath = ((HttpServletRequest) pageContext.getRequest()).getContextPath();
        context.put(ContextAttribute.JDIR_SERVLET_PATH.getName(), contextPath + 
                pageContext.getServletContext().getInitParameter(Constants.JDIRECTORY_SERVLET_PATH_PARAMETER));
        context.put(ContextAttribute.JDIR_TREE.getName(), node.toJSONString());
        writeTemplate(context, TemplateFile.HEADER);
    }

    /*
     * Generates HTML for a particular tree node.
     */
    private void displayNode(TreeNode node) throws Exception {
        Context context = new VelocityContext();
        context.put(ContextAttribute.ITEM_ID.getName(), node.getId());
        context.put(ContextAttribute.ITEM_TYPE.getName(), node.getItem().getType().name());
        if (node.getChildren().size() > 0 && node.isExpanded()) {
            context.put(ContextAttribute.DIRECTORY_NAME.getName(), node.getItem().getName());
            context.put(ContextAttribute.DIRECTORY_EXPANDED.getName(), node.isExpanded());
            writeTemplate(context, TemplateFile.SUB_TREE_START);
            for (TreeNode child : node.getChildren()) {
                displayNode(child);
            }
            writeTemplate(context, TemplateFile.SUB_TREE_END);
        } else {
            context.put(ContextAttribute.ITEM_NAME.getName(), node.getItem().getName());
            writeTemplate(context, TemplateFile.TREE_ITEM);
        }
    }

    /*
     * Writes content of specified template to the output stream.
     */
    private void writeTemplate(Context context, TemplateFile template) throws Exception {
        VelocityEngine engine = (VelocityEngine) pageContext.getAttribute(VELOCITY_ENGINE_PAGE_ATTRIBUTE);
        engine.getTemplate(template.getName()).merge(context, pageContext.getOut());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int doEndTag() throws JspException {
        pageContext.removeAttribute(VELOCITY_ENGINE_PAGE_ATTRIBUTE);
        return EVAL_PAGE;
    }
}
