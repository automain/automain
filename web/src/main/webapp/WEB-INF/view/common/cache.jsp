<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="common.jsp" %>
    <title>刷新缓存</title>
</head>
<body>
<div class="layui-container" style="margin-top: 30px;">
    <div class="layui-row">
        <div class="layui-col-md3"><button class="layui-btn layui-btn-lg" onclick="reloadCache('requestMapping')">访问路径</button></div>
        <div class="layui-col-md3"><button class="layui-btn layui-btn-lg" onclick="reloadCache('properties')">配置文件</button></div>
        <div class="layui-col-md3"><button class="layui-btn layui-btn-lg" onclick="reloadCache('dictionary')">数据字典</button></div>
        <div class="layui-col-md3"><button class="layui-btn layui-btn-lg" onclick="reloadCache('role')">角色权限</button></div>
    </div>
</div>
</body>
</html>
<script>
    var layer;
    layui.use(['layer'], function () {
        layer = layui.layer;
    });
    function reloadCache(reloadLabel) {
        $.post("${ctx}/reload/cache",{
            reloadLabel: reloadLabel
        },function(data){
            if (data.code == code_success){
                layer.msg(data.msg);
            }
        },"json")
    }
</script>