<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<form class="layui-form layui-form-pane" action="">
    <input type="hidden" name="userId" value="${user.userId}">
    <input type="hidden" id="uploadFileId" name="uploadFileId">
    <div class="layui-form-item">
        <label class="layui-form-label">用户名</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" value="<c:out value="${user.userName}"/>" disabled>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">手机号</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="cellphone" maxlength="11" lay-verify="cellphone|phone" value="<c:out value="${user.cellphone}"/>">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">邮箱</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="email" lay-verify="myemail|email" value="<c:out value="${user.email}"/>">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">选择头像</label>
        <div class="layui-input-block">
            <button type="button" class="layui-btn" id="file"><i class="fa fa-upload"></i>选择头像</button>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">头像</label>
        <div class="layui-input-block">
            <img id="uploadImage" src="${imgPath}" <c:if test="${imgPath != null}">width="200px" </c:if>>
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
    layui.use(['form', 'layer','upload'], function () {
        var form = layui.form
            , layer = layui.layer
            , upload = layui.upload;
        upload.render({
            elem: "#file"
            ,url: '${ctx}/upload?json=1'
            ,size: 2048
            ,done: function(res){
                $("#uploadFileId").val(res.uploadFileId);
                $("#uploadImage").attr("src",res.imagePath);
                $("#uploadImage").attr("width","200px");
                layer.msg(res.msg);
            }
        });

        form.verify({
            cellphone: function (value) {
                if (value.length == 0) {
                    return '请输入手机号';
                }
            }
            , myemail: function (value) {
                if (value.length == 0) {
                    return '请输入邮箱';
                }
            }
        });
        form.on('submit(user_submit)', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                $.post("${ctx}/user/update", data.field, function (data) {
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