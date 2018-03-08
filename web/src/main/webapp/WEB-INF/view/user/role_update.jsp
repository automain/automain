<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<form class="layui-form layui-form-pane" action="">
    <input type="hidden" name="roleId" value="${tbRole.roleId}">
    <div class="layui-form-item">
        <label class="layui-form-label">角色名称</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="roleName" lay-verify="role_name" value="<c:out value="${tbRole.roleName}"/>">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">角色标识</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="roleLabel" lay-verify="role_label" value="<c:out value="${tbRole.roleLabel}"/>">
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="role_submit">立即提交</button>
        </div>
    </div>
</form>
</body>
</html>
<script>
    layui.use(['form', 'layer'], function () {
        var form = layui.form
            , layer = layui.layer;
        form.verify({
            role_name: function (value) {
                if (value.length == 0) {
                    return '请输入角色名称';
                }
            }
            , role_label: function (value) {
                if (value.length == 0) {
                    return '请输入角色标识';
                }
            }
        });
        form.on('submit(role_submit)', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                var index = parent.layer.getFrameIndex(window.name);
                $.post("${ctx}/role/update", data.field, function (data) {
                    layer.msg(data.msg);
                    if (data.code == code_success) {
                        parent.layer.close(index);
                        parent.reloadRoleList(1);
                    } else {
                        submitBtn.removeClass("layui-btn-disabled");
                    }
                }, "json");
            }
            return false;
        });
    });
</script>