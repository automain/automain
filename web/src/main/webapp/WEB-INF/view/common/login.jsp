<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <link rel="stylesheet" href="${ctx}/static/css/login/login.css" media="all"/>
    <title>CMS</title>
</head>
<body class="bg-body">
<form class="layui-form" action="#">
    <div class="layui-layout layui-layout-login">
        <h1>
            <strong>CMS管理系统后台</strong>
            <em>Management System</em>
        </h1>
        <div class="layui-login layui-login-icon fa fa-user">
            <input type="text" placeholder="账号" class="login_txtbx" name="username" lay-verify="username"/>
        </div>
        <div class="layui-login layui-login-icon fa fa-lock">
            <input type="password" placeholder="密码" class="login_txtbx" name="password" lay-verify="password"/>
        </div>
        <div class="layui-val-icon layui-login">
            <input type="hidden" id="captchaRandomKey" name="captchaRandomKey"/>
            <div class="layui-code-box">
                <input type="text" name="captchaValue" placeholder="验证码" maxlength="4" class="login_txtbx" lay-verify="captcha">
                <img src="" class="login_captcha_img" id="login_captcha_img" onclick="initCaptcha()">
            </div>
        </div>
        <div class="layui-submit layui-login">
            <input type="button" value="立即登陆" class="submit-btn" lay-submit lay-filter="login"/>
        </div>
        <div class="layui-login-text">
            <p>&copy; 2016-2017 Automain 版权所有</p>
            <p><a href="https://automain.github.io/desc" title="">Powered By Automain</a></p>
        </div>
    </div>
</form>
</body>
</html>
<script src="${ctx}/static/js/plugin/md5.min.js"></script>
<script>
    function initCaptcha() {
        $.post("${ctx}/user/captcha", {}, function (data) {
            if (data.code == code_success) {
                $("#captchaRandomKey").val(data.captchaRandomKey);
                $("#login_captcha_img").attr("src", "data:image/png;base64," + data.image);
            }
        }, "json");
    }
    layui.use('form', function () {
        initCaptcha();
        var form = layui.form;
        form.verify({
            username: function (value) {
                if (value.length == 0) {
                    return '账号不能为空';
                }
            }
            , password: function (value) {
                if (value.length == 0) {
                    return '密码不能为空';
                }
            }
            , captcha: function (value) {
                if (value.length == 0) {
                    return '验证码不能为空';
                }
            }
        });
        form.on('submit(login)', function (data) {
            var loading = layer.load(2, {
                shade: [0.2, '#000']
            });
            var param = data.field;
            var username = param.username;
            var captchaValue = param.captchaValue;
            var password = param.password;
            var captchaRandomKey = param.captchaRandomKey;
            var token = md5(md5(password) + captchaValue);
            $.post('${ctx}/user/login/action', {
                username: username,
                captchaValue: captchaValue,
                captchaRandomKey: captchaRandomKey,
                token: token
            }, function (data) {
                if (data.code == code_success) {
                    layer.close(loading);
                    layer.msg(data.msg, {icon: 1, time: 300}, function () {
                        location.href = '${ctx}/user/frame';
                    });
                } else {
                    layer.close(loading);
                    layer.msg(data.msg, {icon: 2, anim: 6, time: 500});
                    initCaptcha();
                }
            });
            return false;
        });
    });
</script>
