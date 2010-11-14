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
    if (node.children.length > 0) {
        if (parentTable.hasClass('expanded-item')) {
            parentTable.removeClass('expanded-item').next().hide();
            $('#' + node.id).text('+');
        } else {
            parentTable.addClass('expanded-item').next().show();
            $('#' + node.id).text('-');
        }
    }
};

jQuery.requestTreeNode = function(e) {
    var nodeId = $(this).attr('id');
    var pathParameter = "";
    var node = $.nodeSearcher.searchNode(nodeId, function(currentNode) {
        pathParameter += '/' + currentNode.item.name;
        if (currentNode.item.type == 'ARCHIVE') {
            pathParameter += '!';
        }
    });
    var tableElement = $(this).parentsUntil("table").last().parent();
    if (!tableElement.hasClass('opened-item')) {
        $.ajax({
            url: jdirServletPath,
            data: {paths : pathParameter},
            dataType: 'json',
            type: 'POST',
            success: function(data) {
                if (data.response == 'unsupported') {
                    alert('Expanding this type of archive is unsupported');
                } else {
                    $.addSubTree(node, data.response[pathParameter]);
                    $.expandTreeNode(node, tableElement);
                }
            }
        });
        tableElement.addClass('opened-item');
    } else {
        $.expandTreeNode(node, tableElement);
    }
    e.preventDefault();
    return false;
};

$(function() {
    $('.tree-directory').click($.requestTreeNode);
});