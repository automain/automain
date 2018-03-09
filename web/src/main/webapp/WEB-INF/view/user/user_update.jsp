<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<form class="layui-form layui-form-pane" action="">
    <input type="hidden" name="userId" value="${bean.userId}">
    <div class="layui-form-item">
        <label class="layui-form-label">用户名</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="userName" lay-verify="user_name" value="<c:out value="${bean.userName}"/>">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">手机号</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="cellphone" maxlength="11" lay-verify="cellphone|phone" value="<c:out value="${bean.cellphone}"/>">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">邮箱</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="email" lay-verify="myemail|email" value="<c:out value="${bean.email}"/>">
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="user_submit">立即提交</button>
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
            user_name: function (value) {
                if (value.length == 0) {
                    return '请输入用户名';
                }
                var msg = '';
                $.ajax({
                    url: "${ctx}/user/check/exist",
                    type: "post",
                    async: false,
                    data: {
                        userName: value,
                        userId: '${bean.userId}'
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
            , cellphone: function (value) {
                if (value.length == 0) {
                    return '请输入手机号';
                }
            }
            , myemail: function (value) {
                if (value.length == 0) {
                    return '请输入邮箱';
                }
            }
        });
        form.on('submit(user_submit)', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                var index = parent.layer.getFrameIndex(window.name);
                $.post("${ctx}/user/update", data.field, function (data) {
                    layer.msg(data.msg);
                    if (data.code == code_success) {
                        parent.layer.close(index);
                        parent.reloadUserList(1);
                    } else {
                        submitBtn.removeClass("layui-btn-disabled");
                    }
                }, "json");
            }
            return false;
        });
    });
</script>