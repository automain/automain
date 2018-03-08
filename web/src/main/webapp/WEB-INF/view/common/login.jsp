<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <link rel="stylesheet" href="${ctx}/static/css/login/login.css" media="all"/>
    <title>登录</title>
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
            <input type="hidden" id="captchaPosition" name="captchaPosition" lay-verify="captcha_position"/>
            <input type="hidden" id="captchaRandomKey" name="captchaRandomKey"/>
            <div id="drag">
                <div class="drag_bg"></div>
                <div class="drag_text">滑动到指定位置</div>
                <div class="handler handler_bg" onselectstart="return false;"><i class="fa fa-angle-double-right"></i></div>
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
    var drag = $("#drag");
    var handler = drag.find('.handler');
    var drag_bg = drag.find('.drag_bg');
    function dragHandler() {
        $.post("${ctx}/user/captcha", {
            width: drag.width(),
            height: drag.height()
        }, function (data) {
            if (data.code == code_success) {
                $("#captchaRandomKey").val(data.captchaRandomKey);
                $("#drag").css("background", "url(data:image/png;base64," + data.image + ")");
            }
        }, "json");
        var x, isMove = false;
        var maxWidth = drag.width() - handler.width();  //能滑动的最大间距
        //鼠标按下时候的x轴的位置
        handler.mousedown(function (e) {
            isMove = true;
            x = e.pageX - parseInt(handler.css('left'), 10);
        });

        //鼠标指针在上下文移动时，移动距离大于0小于最大间距，滑块x轴位置等于鼠标移动距离
        handler.mousemove(function (e) {
            var _x = e.pageX - x;
            if (isMove) {
                if (_x > 0 && _x <= maxWidth) {
                    handler.css({'left': _x});
                    drag_bg.css({'width': _x});
                }
            }
        }).mouseup(function (e) {
            isMove = false;
            var _x = e.pageX - x;
            if (_x > maxWidth) { //鼠标松开时，如果大于最大距离位置，滑块就返回初始位置
                handler.css({'left': 0});
                drag_bg.css({'width': 0});
            } else {
                dragOk(_x);
            }
        });
    }
    //清空事件
    function dragOk(position) {
        handler.html('<i class="fa fa-check"></i>');
        $("#captchaPosition").val(position);
        drag.find(".drag_text").html("");
        handler.unbind('mousedown');
        handler.unbind('mousemove');
        handler.unbind('mouseup');
    }
    layui.use('form', function () {
        dragHandler();
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
            , captcha_position: function (value) {
                if (value.length == 0) {
                    return '滑动到指定位置';
                }
            }
        });
        form.on('submit(login)', function (data) {
            var loading = layer.load(2, {
                shade: [0.2, '#000']
            });
            var param = data.field;
            var username = param.username;
            var captchaPosition = param.captchaPosition;
            var password = param.password;
            var captchaRandomKey = param.captchaRandomKey;
            var token = md5(md5(password) + captchaPosition);
            $.post('${ctx}/user/login/action', {
                username: username,
                captchaPosition: captchaPosition,
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
                    layer.msg(data.msg, {icon: 2, anim: 6, time: 300});
                    dragHandler();
                    handler.html('<i class="fa fa-angle-double-right"></i>');
                    handler.css({'left': 0});
                    drag_bg.css({'width': 0});
                    drag.find(".drag_text").html("滑动到指定位置");
                }
            });
            return false;
        });
    });
</script>
