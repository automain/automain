<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<form class="layui-form layui-form-pane" action="">
    <input type="hidden" name="userId" value="${user.userId}">
    <div class="layui-form-item">
        <label class="layui-form-label">原密码</label>
        <div class="layui-input-block">
            <input type="password" class="layui-input" autocomplete="off" name="originPwd" lay-verify="origin_pwd">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">新密码</label>
        <div class="layui-input-block">
            <input type="password" class="layui-input" autocomplete="off" id="newPwd" name="newPwd" lay-verify="new_pwd">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">重复新密码</label>
        <div class="layui-input-block">
            <input type="password" class="layui-input" autocomplete="off" name="repeatNewPwd" lay-verify="repeat_new_pwd">
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
            origin_pwd: function (value) {
                if (value.length == 0) {
                    return '请输入原密码';
                }
            }
            , new_pwd: function (value) {
                if (value.length < 6) {
                    return '请输入新密码(至少6位字符)';
                }
            }
            , repeat_new_pwd: function (value) {
                if (value.length < 6) {
                    return '请再次输入新密码(至少6位字符)';
                }
                var newPwd = $("#newPwd").val();
                if (newPwd != value) {
                    return '两次输入的密码不一致';
                }
            }
        });
        form.on('submit(user_submit)', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                $.post("${ctx}/user/update/pwd", data.field, function (data) {
                    layer.msg(data.msg);
                    if (data.code == code_fail) {
                        submitBtn.removeClass("layui-btn-disabled");
                    }
                }, "json");
            }
            return false;
        });
    });
</script>