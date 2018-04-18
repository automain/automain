<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <link rel="stylesheet" href="${ctx}/static/ztree/css/zTreeStyle.css?v=${staticVersion}" media="all">
    <title></title>
</head>
<body>
<form class="layui-form layui-form-pane" action="">
    <input type="hidden" id="roleId" value="${roleId}">
    <div class="layui-form-item">
        <ul id="menu-tree" class="ztree"></ul>
    </div>
    <div class="layui-form-item">
        <button class="layui-btn" lay-submit lay-filter="grant_menu_submit">立即提交</button>
    </div>
</form>
</body>
</html>
<script src="${ctx}/static/ztree/js/jquery.ztree.core.min.js?v=${staticVersion}"></script>
<script src="${ctx}/static/ztree/js/jquery.ztree.excheck.min.js?v=${staticVersion}"></script>
<script>
    var setting = {
        check: {
            enable: true,
            chkboxType:  { "Y" : "ps", "N" : "ps" }
        },
        view: {
            showIcon: false
        }
    };
    layui.use(['form', 'layer'], function () {
        var form = layui.form
            , layer = layui.layer;
        $.fn.zTree.init($("#menu-tree"), setting, ${menuVOList});
        form.on('submit(grant_menu_submit)', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                var index = parent.layer.getFrameIndex(window.name);
                var menuCheck = new Array();
                var zTree = $.fn.zTree.getZTreeObj("menu-tree"),
                    nodes = zTree.getCheckedNodes();
                for (var i=0, l=nodes.length; i<l; i++) {
                    menuCheck.push(nodes[i].id);
                }
                $.ajax({
                    url: "${ctx}/role/grant/menu",
                    type: "post",
                    traditional: true,
                    data: {
                        roleId: $("#roleId").val(),
                        menuCheck: menuCheck
                    },
                    dataType: "json",
                    success: function (data) {
                        layer.msg(data.msg);
                        if (data.code == code_success) {
                            parent.layer.close(index);
                            parent.reloadRoleList(1);
                        } else {
                            submitBtn.removeClass("layui-btn-disabled");
                        }
                    }
                });
            }
            return false;
        });
    });
</script>