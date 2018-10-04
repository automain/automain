<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>创建分表</legend>
</fieldset>
<form class="layui-form layui-form-pane" action="">
    <div class="layui-form-item">
        <label class="layui-form-label">主表</label>
        <div class="layui-input-block">
            <select class="layui-select" id="main-table-name" name="mainTableName" lay-filter="main_table_name" lay-search>
                <c:forEach items="${tableNameList}" var="tableName">
                    <option value="${tableName}">${tableName}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">月份</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" id="sub-table-time" name="subTableTime" lay-verify="sub_table_time">
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="sub_table_submit">立即提交</button>
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
        laydate.render({
            elem: '#sub-table-time'
            , type: 'month'
        });
        form.verify({
            main_table_name: function (value) {
                if (value.length == 0) {
                    return '请选择主表';
                }
            }
            , sub_table_time: function (value) {
                if (value.length == 0) {
                    return '请选择月份';
                }
            }
        });
        form.on('submit(sub_table_submit)', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                $.post("${ctx}/subtable/add", data.field, function (data) {
                    layer.msg(data.msg);
                    if (data.code == code_success) {
                        setTimeout(function () {
                            window.location.href = "${ctx}/subtable/forward";
                        }, 1000);
                    } else {
                        submitBtn.removeClass("layui-btn-disabled");
                    }
                }, "json");
            }
            return false;
        });
    });
</script>