<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<form class="layui-form layui-form-pane" action="">
    <input type="hidden" name="privilegeId" value="${bean.privilegeId}">
    <div class="layui-form-item">
        <label class="layui-form-label">权限标识</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="privilegeLabel" lay-verify="privilege_label" value="<c:out value='${bean.privilegeLabel}'/>">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">权限名称</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="privilegeName" lay-verify="privilege_name" value="<c:out value='${bean.privilegeName}'/>">
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="privilege_submit">立即提交</button>
        </div>
    </div>
</form>
</body>
</html>
<script>
    layui.use(['form', 'layer', 'laydate'], function () {
        var form = layui.form
            , layer = layui.layer
            , laydate = layui.laydate;
        form.verify({
            privilege_label: function (value) {
                if (value.length == 0) {
                    return '请输入权限标识';
                }
                var msg = '';
                $.ajax({
                    url: "${ctx}/privilege/check/exist",
                    type: "post",
                    async: false,
                    data: {
                        privilegeLabel: value,
                        privilegeId: '${bean.privilegeId}'
                    },
                    dataType: "json",
                    success: function (data) {
                        if (data.code == code_fail) {
                            msg = data.msg;
                        }
                    }
                });
                return msg;
            }
            , privilege_name: function (value) {
                if (value.length == 0) {
                    return '请输入权限名称';
                }
            }
        });
        form.on('submit(privilege_submit)', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                var index = parent.layer.getFrameIndex(window.name);
                $.post("${ctx}/privilege/update", data.field, function (data) {
                    layer.msg(data.msg);
                    if (data.code == code_success) {
                        parent.layer.close(index);
                        parent.reloadPrivilegeList(1);
                    } else {
                        submitBtn.removeClass("layui-btn-disabled");
                    }
                }, "json");
            }
            return false;
        });
    });
</script>