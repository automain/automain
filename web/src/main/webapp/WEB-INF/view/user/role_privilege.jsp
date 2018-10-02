<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <link rel="stylesheet" href="${ctx}/static/ztree/css/zTreeStyle.css?v=${staticVersion}" media="all">
    <title></title>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row">
        <div class="layui-col-md12">
            <div class="layui-card">
                <form class="layui-form layui-form-pane" action="">
                    <input type="hidden" id="roleId" value="${roleId}">
                    <div class="layui-form-item">
                        <ul id="privilege-tree" class="ztree"></ul>
                    </div>
                    <div class="layui-form-item">
                        <button class="layui-btn" lay-submit lay-filter="grant_privilege_submit">立即提交</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<script src="${ctx}/static/ztree/js/jquery.ztree.core.min.js?v=${staticVersion}"></script>
<script src="${ctx}/static/ztree/js/jquery.ztree.excheck.min.js?v=${staticVersion}"></script>
<script>
    var setting = {
        check: {
            enable: true,
            chkboxType: {"Y": "ps", "N": "ps"}
        },
        view: {
            showIcon: false
        }
    };
    layui.use(['form', 'layer'], function () {
        var form = layui.form
            , layer = layui.layer;
        var treeObj = $.fn.zTree.init($("#privilege-tree"), setting, ${privilegeVOList});
        treeObj.expandAll(true);
        form.on('submit(grant_privilege_submit)', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                var index = parent.layer.getFrameIndex(window.name);
                var privilegeCheck = new Array();
                var zTree = $.fn.zTree.getZTreeObj("privilege-tree"),
                    nodes = zTree.getCheckedNodes();
                for (var i = 0, l = nodes.length; i < l; i++) {
                    privilegeCheck.push(nodes[i].id);
                }
                $.ajax({
                    url: "${ctx}/role/grant/privilege",
                    type: "post",
                    traditional: true,
                    data: {
                        roleId: $("#roleId").val(),
                        privilegeCheck: privilegeCheck
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