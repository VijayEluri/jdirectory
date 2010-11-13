<%@ tag body-content="empty" pageEncoding="UTF-8" description="JDirectory tree tag" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="treeBean" class="jdirectory.tagsupport.JDirectoryTreeTagBean"/>
<jsp:setProperty name="treeBean" property="rootDirectoryPath"
                 value="${applicationScope['jdirectory.root.directory.path']}" />

<div style="width: 300px">
    <table width="100%">
        <tbody>
            <tr>
                <td width="10px"><a href="#" style="text-decoration:none">+</a></td>
                <td>test1</td>
            </tr>
        </tbody>
    </table>
    <div style="padding-left:10px;">
        <table width="100%">
            <tbody>
            <tr>
                <td width="10px"><a href="#" style="text-decoration:none">+</a></td>
                <td>test1.1</td>
            </tr>
            </tbody>
        </table>
        <div style="padding-left:10px;">
            <table width="100%">
                <tbody>
                <tr>
                    <td width="10px">-</td>
                    <td>test1.1.1</td>
                </tr>
                </tbody>
            </table>
            <table width="100%">
                <tbody>
                <tr>
                    <td width="10px">-</td>
                    <td>test1.1.2</td>
                </tr>
                </tbody>
            </table>
            <table width="100%">
                <tbody>
                <tr>
                    <td width="10px">-</td>
                    <td>test1.1.3</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
    <table width="100%">
        <tbody>
            <tr>
                <td width="10px"><a href="#" style="text-decoration:none">+</a></td>
                <td>test2</td>
            </tr>
        </tbody>
    </table>
</div>
