package jdirectory.tags;

import jdirectory.core.DirectoryScanException;
import jdirectory.core.DirectoryScannerFactory;
import jdirectory.core.FilesystemItem;
import jdirectory.core.FilesystemItemType;
import jdirectory.util.Constants;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

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
        Object rootDirectory = pageContext.getServletContext().getInitParameter(
                Constants.ROOT_DIRECTORY_CONTEXT_PARAMETER);
        FilesystemItem[] items = DirectoryScannerFactory.getInstance()
                .getScanner(rootDirectory.toString(), "/").scan();
        TreeNode root = new TreeNode(new FilesystemItem("", FilesystemItemType.DIRECTORY));
        for (FilesystemItem item : items) {
            root.addChild(new TreeNode(item));
        }
        return root;
    }

    /*
     * Generates HTML for a particular tree node.
     */
    private void displayNode(TreeNode node) throws Exception {
        Context context = new VelocityContext();
        if (node.getChildren().size() > 0) {
            context.put(ContextAttribute.DIRECTORY_NAME.getName(), node.getItem().getName());
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
