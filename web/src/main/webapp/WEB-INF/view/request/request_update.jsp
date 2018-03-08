<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<form class="layui-form layui-form-pane" action="">
    <input type="hidden" name="requestMappingId" value="${tbRequestMapping.requestMappingId}">
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">请求相对路径</label>
        <div class="layui-input-block">
            <textarea class="layui-textarea" name="requestUrl" lay-verify="request_url"><c:out value="${tbRequestMapping.requestUrl}"/></textarea>
        </div>
    </div>
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">请求处理类的全路径</label>
        <div class="layui-input-block">
            <textarea class="layui-textarea" name="operationClass" lay-verify="operation_class"><c:out value="${tbRequestMapping.operationClass}"/></textarea>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">注释</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="urlComment" lay-verify="url_comment" value="<c:out value="${tbRequestMapping.urlComment}"/>">
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="request_submit">立即提交</button>
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
            request_url: function (value) {
                if (value.length == 0) {
                    return '请输入请求相对路径';
                }
            }
            , operation_class: function (value) {
                if (value.length == 0) {
                    return '请输入请求处理类的全路径';
                }
            }
            , url_comment: function (value) {
                if (value.length == 0) {
                    return '请输入注释';
                }
            }
        });
        form.on('submit(request_submit)', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                var index = parent.layer.getFrameIndex(window.name);
                $.post("${ctx}/request/update", data.field, function (data) {
                    layer.msg(data.msg);
                    if (data.code == code_success) {
                        parent.layer.close(index);
                        parent.reloadRequestList(1);
                    } else {
                        submitBtn.removeClass("layui-btn-disabled");
                    }
                }, "json");
            }
            return false;
        });
    });
</script>