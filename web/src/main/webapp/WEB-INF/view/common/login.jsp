<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <link rel="stylesheet" href="${ctx}/static/css/common/login.css?v=${staticVersion}" media="all"/>
    <title>登录</title>
</head>
<body>
<form class="layui-form" action="#">
    <div class="layadmin-user-login layadmin-user-display-show">
        <div class="layadmin-user-login-main">
            <div class="layadmin-user-login-box layadmin-user-login-header">
                <h2>CMS管理系统</h2>
                <p>Management System</p>
            </div>
            <div class="layadmin-user-login-box layadmin-user-login-body layui-form">
                <div class="layui-form-item">
                    <label class="layadmin-user-login-icon layui-icon layui-icon-username" for="username"></label>
                    <input type="text" name="username" id="username" lay-verify="username" placeholder="用户名" class="layui-input">
                </div>
                <div class="layui-form-item">
                    <label class="layadmin-user-login-icon layui-icon layui-icon-password" for="password"></label>
                    <input type="password" name="password" id="password" lay-verify="password" placeholder="密码" class="layui-input">
                </div>
                <div class="layui-form-item">
                    <div class="layui-row">
                        <div class="layui-col-xs7">
                            <input type="hidden" id="captchaRandomKey" name="captchaRandomKey"/>
                            <label class="layadmin-user-login-icon layui-icon layui-icon-vercode" for="captcha"></label>
                            <input type="text" id="captcha-value" name="captchaValue" maxlength="4" id="captcha" lay-verify="captcha" placeholder="图形验证码" class="layui-input">
                        </div>
                        <div class="layui-col-xs5">
                            <div style="margin-left: 10px;">
                                <img src="" class="layadmin-user-login-codeimg" id="login_captcha_img" onclick="initCaptcha()">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-form-item">
                    <button class="layui-btn layui-btn-fluid" id="submit-btn" lay-submit lay-filter="login">登 录</button>
                </div>
            </div>
        </div>
        <div class="layui-trans layadmin-user-login-footer">
            <p>&copy; 2018 <a href="https://automain.github.io/desc" target="_blank">automain</a></p>
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
        $("#captcha-value").bind("keydown", function (e) {
            var theEvent = e || window.event;
            var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
            if (code == 13) {
                $("#submit-btn").click();
            }
        });
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
