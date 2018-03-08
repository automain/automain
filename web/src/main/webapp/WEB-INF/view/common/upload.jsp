<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="common.jsp" %>
    <title>文件上传</title>
</head>
<body>
    <img id="uploadImage">
    <input type="hidden" id="uploadFileId">
    <button type="button" class="layui-btn" id="file"><i class="fa fa-upload"></i>选择文件</button>
</body>
</html>
<script>
    layui.use(['layer','upload'], function () {
        var layer = layui.layer
            , upload = layui.upload;
        upload.render({
            elem: "#file"
            ,url: '${ctx}/upload'
            ,size: 2048
            ,done: function(res){
                $("#uploadFileId").val(res.uploadFileId);
                $("#uploadImage").attr("src",res.imagePath);
                layer.msg(res.msg);
            }
        });
    });
</script>