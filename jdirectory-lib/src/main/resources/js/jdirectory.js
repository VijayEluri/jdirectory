jQuery.nodeSearcher = {
    searchNode: function(nodeId, callback) {
        var nodeNumbers = nodeId.split('-');
        return this.getNodeById(jdirTree, nodeNumbers, 1, callback);
    },
    getNodeById: function(node, nodeNumbers, idx, callback) {
        node = node.children[parseInt(nodeNumbers[idx++])];
        callback(node);
        if (idx < nodeNumbers.length) {
            node = this.getNodeById(node, nodeNumbers, idx, callback);
        }
        return node;
    }
};

jQuery.addSubTree = function(node, responseObjectArray) {
    for (var i = 0; i < responseObjectArray.length; i++) {
        node.children[i] = {
            id: node.id + '-' + node.children.length,
            item: {name: responseObjectArray[i].name, type: responseObjectArray[i].type},
            children: new Array()
        };
    }
    $.addHtmlSubNodes(node);
};

jQuery.addHtmlSubNodes = function(parentNode) {
    var div = $('<div>').addClass('sub-tree');
    $('#' + parentNode.id).parentsUntil("table").last().parent().after(div);
    for (var i = 0; i < parentNode.children.length; i++) {
        div.append($.createItemElement(parentNode.children[i]));
    }
};

jQuery.registeredItemTypes = new Array();
$.registeredItemTypes['PICTURE'] = {style: 'picture-symbol', expanded: false};
$.registeredItemTypes['DIRECTORY'] = {style: 'dir-symbol', expanded: true};
$.registeredItemTypes['FILE'] = {style: 'file-symbol', expanded: false};
$.registeredItemTypes['ARCHIVE'] = {style: 'archive-symbol', expanded: true};
$.registeredItemTypes['JAR'] = {style: 'jar-symbol', expanded: true};

jQuery.createItemElement = function(node) {
    var row = $('<tr>');
    if (!$.registeredItemTypes[node.item.type].expanded) {
        row.append($('<td>').append($('<div>')
                .addClass($.registeredItemTypes[node.item.type].style)
                .html('&nbsp;')));
    } else {
        row.append($('<td>').append($('<a>')
                .addClass('tree-directory')
                .attr('id', node.id)
                .attr('href', '#')
                .text('+')
                .click($.requestTreeNode)))
                .append($('<td>').append($('<div>')
                .addClass($.registeredItemTypes[node.item.type].style)
                .html('&nbsp;')));
    }
    row.append($('<td>').text(node.item.name));
    return $('<table>').append($('<tbody>').append(row));
};

jQuery.expandTreeNode = function(node, parentTable) {
    if (parentTable.hasClass('opened-item')) {
        if (parentTable.hasClass('expanded-item')) {
            parentTable.removeClass('expanded-item').next().hide();
            $('#' + node.id).text('+');
        } else {
            parentTable.addClass('expanded-item').next().show();
            $('#' + node.id).text('-');
        }
    }
};

jQuery.animateTreeNode = function(cellNode, originalLabel, animationProperties) {
    jQuery.fx.interval = 500;
    cellNode.animate({left: '0'}, {duration: 30000, step: function() {
        var label = cellNode.text();
        cellNode.text((label.length > 3 && label.substring(label.length - 3) == '...')
                ? label.substring(0, label.length - 3) : label + '.');
    }});
};

jQuery.requestTreeNode = function(e) {
    // Animation and disabling clicking
    var cellNode = $(this).parent().next().next();
    var originalLabel = cellNode.text();
    $(this).unbind('click');
    $.animateTreeNode(cellNode);

    // Initialization
    var nodeId = $(this).attr('id');
    var pathParameter = "";
    var node = $.nodeSearcher.searchNode(nodeId, function(currentNode) {
        pathParameter += '/' + currentNode.item.name;
        if (currentNode.item.type == 'ARCHIVE' || currentNode.item.type == 'JAR') {
            pathParameter += '!';
        }
    });
    var tableElement = $(this).parentsUntil("table").last().parent();
    var thisElement = $(this);

    //Ajax call
    $.ajax({
        url: jdirServletPath,
        data: tableElement.hasClass('opened-item')
                ? {paths : pathParameter, expanded: !tableElement.hasClass('expanded-item') + ""}
                : {paths : pathParameter},
        dataType: 'json',
        type: 'POST',
        success: function(data) {
            cellNode.stop();
            cellNode.text(originalLabel);
            if (data.response == 'unsupported') {
                alert('Expanding RAR archives is unsupported');
            } else {
                var responseObjectArray = data.response[pathParameter];
                if (responseObjectArray != null) {
                    $.addSubTree(node, responseObjectArray);
                    tableElement.addClass('opened-item');
                }
                $.expandTreeNode(node, tableElement);
            }
            thisElement.bind('click', $.requestTreeNode);
        }
    });
    
    e.preventDefault();
    return false;
};

$(function() {
    $('.tree-directory').click($.requestTreeNode);
});