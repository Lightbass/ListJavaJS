<%@ page import = "com.fnoz.MainJSON" language="java" contentType="text/html; charset=Cp1251" pageEncoding="Cp1251"%>

<jsp:useBean id="calca" class="com.fnoz.MainJSON" scope="session"/>
<jsp:setProperty name="calca" property="*"/>


<html xmlns="http://www.w3.org/1999/xhtml">
    <link rel="stylesheet" href="dist/themes/default/style.min.css" />
    <body>
        <head>
            <title> Tree </title>
        </head>
        <h2> Tree </h2>
        <div id="lazy"></div>
        <script src="dist/jquery-3.2.1.min.js"></script>
        <script src="dist/jstree.min.js"></script>
    <script>
    var prevNode;
    $('#lazy').on("changed.jstree", function (e, data) {
          console.log(data.selected);
        });
    $(function () {
        $('#lazy').jstree({
		'core' : {
		    "animation" : 0,
            "check_callback" : true,
        	"themes" : { "stripes" : true },
        	"multiple": false,
			'data' : {
				"url" : function(node){
				    return node.id === '#' ? "./json?load=root" : "./json?load="+node.id;
				}
			}
		},
		'types' : {
            "default" : {
        	    "icon" : "./dist/themes/closed_16.png"
        	}
        },
        "contextmenu":{
            "items": function($node) {
                var tree = $("#lazy").jstree(true);
                return {
                    "Create": {
                        "separator_before": false,
                        "separator_after": false,
                        "label": "Create",
                        "action": function (obj) {
                            $node = tree.create_node($node);
                            tree.edit($node);
                        }
                    },
                    "Rename": {
                        "separator_before": false,
                        "separator_after": false,
                        "label": "Rename",
                        "action": function (obj) {
                            tree.edit($node);
                        }
                    },
                    "Remove": {
                        "separator_before": false,
                        "separator_after": false,
                        "label": "Remove",
                        "action": function (obj) {
                            tree.delete_node($node);
                        }
                    }
                };
            }
        },
        "plugins" : [
            "contextmenu", "dnd", "wholerow", "types"
          ]
	});

    function create(crNode){
        var tree = $('#lazy').jstree(true).get_json(crNode.id, {
                                "flat":false,
                                "no_state":true,
                                "no_data":true,
                                "no_li_attr":true,
                                "no_a_attr":true,
                                "no_children":true
                                });
                tree.parent = $('#lazy').jstree(true).get_parent(crNode);
                var text = JSON.stringify(tree);
                $.post("./json", "create=" + text);
    }
    function changedTree(actNode){
        if($('#lazy').jstree(true).is_open(actNode.id)){
            if($('#lazy').jstree(true).is_selected(actNode.id))
                $('#lazy').jstree(true).set_icon(actNode.id, "./dist/themes/selected_open_16.png");
            else
                $('#lazy').jstree(true).set_icon(actNode.id, "./dist/themes/open_16.png");

        }
        else{
            if($('#lazy').jstree(true).is_selected(actNode.id))
                $('#lazy').jstree(true).set_icon(actNode.id, "./dist/themes/selected_closed_16.png");
            else
                $('#lazy').jstree(true).set_icon(actNode.id, "./dist/themes/closed_16.png");
        }
    }

    $('#lazy').on('changed.jstree', function(e, data){
       if(prevNode != null){
            changedTree(prevNode);
        }
        prevNode = data.instance.get_node(data.selected[0]);
        changedTree(data.instance.get_node(data.selected[0]));
    })
    .on('after_open.jstree', function(e, data){
        changedTree(data.instance.get_node(data.node));
    });
    $('#lazy').on('close_node.jstree', function(e, data){
        changedTree(data.instance.get_node(data.node));
    })
    .on('create_node.jstree', function(e, data){
        create(data.node);
    })
    .on('rename_node.jstree', function(e, data){
        create(data.node);
    })
    .on('delete_node.jstree', function(e, data){
        $.post("./json", "delete=" + data.node.id);
    })
    .on('move_node.jstree', function(e, data){
        create(data.node);
    });
  });

  </script>
</body>
</html>
