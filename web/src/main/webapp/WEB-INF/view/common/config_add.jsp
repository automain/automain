<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<form class="layui-form layui-form-pane" action="">
    <div class="layui-form-item">
        <label class="layui-form-label">配置key</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="configKey" lay-verify="config_key">
        </div>
    </div>
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">配置value</label>
        <div class="layui-input-block">
            <textarea class="layui-textarea" name="configValue" lay-verify="config_value"></textarea>
        </div>
    </div>
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">配置描述</label>
        <div class="layui-input-block">
            <textarea class="layui-textarea" name="configComment" lay-verify="config_comment"></textarea>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="config_submit">立即提交</button>
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
            config_key: function (value) {
                if (value.length == 0) {
                    return '请输入配置key';
                }
            }
            , config_value: function (value) {
                if (value.length == 0) {
                    return '请输入配置value';
                }
            }
            , config_comment: function (value) {
                if (value.length == 0) {
                    return '请输入配置描述';
                }
            }
        });
        form.on('submit(config_submit)', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                var index = parent.layer.getFrameIndex(window.name);
                $.post("${ctx}/config/add", data.field, function (data) {
                    layer.msg(data.msg);
                    if (data.code == code_success) {
                        parent.layer.close(index);
                        parent.reloadConfigList(1);
                    } else {
                        submitBtn.removeClass("layui-btn-disabled");
                    }
                }, "json");
            }
            return false;
        });
    });
</script>