<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<form class="layui-form layui-form-pane" action="">
    <input type="hidden" name="innerId" value="${bean.innerId}">
    <div class="layui-form-item">
        <label class="layui-form-label">内部地址</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="ip" lay-verify="ip" value="<c:out value='${bean.ip}'/>">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">端口号</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="port" lay-verify="port" value="<c:out value='${bean.port}'/>">
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="inner_ip_port_submit">立即提交</button>
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
            ip: function (value) {
                if (value.length == 0) {
                    return '请输入内部地址';
                }
            }
            , port: function (value) {
                if (value.length == 0) {
                    return '请输入端口号';
                }
            }
        });
        form.on('submit(inner_ip_port_submit)', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                var index = parent.layer.getFrameIndex(window.name);
                $.post("${ctx}/inner/ip/port/update", data.field, function (data) {
                    layer.msg(data.msg);
                    if (data.code == code_success) {
                        parent.layer.close(index);
                        parent.reloadInnerIpPortList(1);
                    } else {
                        submitBtn.removeClass("layui-btn-disabled");
                    }
                }, "json");
            }
            return false;
        });
    });
</script>