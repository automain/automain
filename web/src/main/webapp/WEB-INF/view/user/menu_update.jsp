<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<form class="layui-form layui-form-pane" action="">
    <input type="hidden" name="menuId" value="${bean.menuId}">
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">请求路径</label>
        <div class="layui-input-block">
            <textarea class="layui-textarea" name="requestUrl" lay-verify="request_url"><c:out value="${bean.requestUrl}"/></textarea>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">菜单名称</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="menuName" lay-verify="menu_name" value="<c:out value="${bean.menuName}"/>">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">菜单图标</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="menuIcon" id="menu-icon" onblur="showIcon()" lay-verify="menu_icon" value="<c:out value="${bean.menuIcon}"/>">
        </div>
        <div class="layui-form-mid layui-word-aux">图标代码参考<a href="http://www.fontawesome.com.cn/faicons/" target="_blank">font awesome</a>
        &nbsp;<i id="icon-show" class="fa fa-<c:out value="${bean.menuIcon}"/>"></i></div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">菜单排序</label>
        <div class="layui-input-block">
            <input type="number" class="layui-input" autocomplete="off" name="sequenceNumber" lay-verify="sequence_number" value="${bean.sequenceNumber}">
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="menu_submit">立即提交</button>
        </div>
    </div>
</form>
</body>
</html>
<script>
    layui.use(['form', 'layer'], function () {
        var form = layui.form
            , layer = layui.layer;
        form.verify({
            menu_name: function (value) {
                if (value.length == 0) {
                    return '请输入菜单名称';
                }
            }
        });
        form.on('submit(menu_submit)', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                var index = parent.layer.getFrameIndex(window.name);
                $.post("${ctx}/menu/update", data.field, function (data) {
                    layer.msg(data.msg);
                    if (data.code == code_success) {
                        parent.layer.close(index);
                        parent.reloadMenuList(1);
                    } else {
                        submitBtn.removeClass("layui-btn-disabled");
                    }
                }, "json");
            }
            return false;
        });
    });
    function showIcon(){
        var cla = $("#menu-icon").val();
        $("#icon-show").removeAttr("class").addClass("fa").addClass("fa-"+cla);
    }
</script>