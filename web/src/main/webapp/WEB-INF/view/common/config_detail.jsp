<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<form class="layui-form layui-form-pane" >
    <div class="layui-form-item">
        <label class="layui-form-label">配置key</label>
        <div class="layui-input-block">
            <input type="text" disabled="disabled" class="layui-input" value="<c:out value='${bean.configKey}'/>">
        </div>
    </div>
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">配置value</label>
        <div class="layui-input-block">
            <textarea class="layui-textarea" disabled="disabled"><c:out value="${bean.configValue}"/></textarea>
        </div>
    </div>
</form>
</body>
</html>
