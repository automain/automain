<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<blockquote class="layui-elem-quote">
    <div class="layui-form">
        <div class="layui-inline">
            <input type="text" class="layui-input" autocomplete="off" id="role-name-search" placeholder="请输入角色名称">
        </div>
        <div class="layui-inline">
            <button class="layui-btn layui-btn-sm layui-btn-warm" id="role_refresh">
                <i class="fa fa-search"></i> 搜索
            </button>
        </div>
    </div>
</blockquote>
<div class="layui-form">
    <table class="layui-table">
        <colgroup>
            <col width="70">
            <col>
        </colgroup>
        <thead>
        <tr>
            <th>操作</th>
            <th>角色名称</th>
            <th>角色标识</th>
        </tr>
        </thead>
        <tbody id="role_list_body">
        </tbody>
    </table>
</div>
<div id="role_page"></div>
</body>
</html>
<script>
    var form, laypage, layer;
    layui.use(['form', 'layer', 'laypage'], function () {
        form = layui.form;
        layer = layui.layer;
        laypage = layui.laypage;
        $("#role_refresh").click(function () {
            reloadRoleList(1);
        });
        reloadRoleList(1);
    });
    function reloadRoleList(page) {
        var index = layer.load();
        setTimeout(function () {
            $.post("${ctx}/request/role/list", {
                page: page,
                requestMappingId: '${requestMappingId}',
                roleName: $("#role-name-search").val()
            }, function (data) {
                if (data.code == code_success) {
                    $("#role_list_body").html(data.data);
                    renderPage(laypage, "role_page", data.count, data.curr, reloadRoleList);
                    $(".grant-role-btn").click(function () {
                        var roleId = $(this).attr("role-id");
                        $.post("${ctx}/role/grant/request",{
                            roleId: roleId
                            , requestMappingId: '${requestMappingId}'
                        }, function (d) {
                            layer.msg(d.msg);
                            if (d.code == code_success){
                                reloadRoleList(data.curr);
                            }
                        }, "json");
                    });
                    $(".revoke-role-btn").click(function () {
                        var roleId = $(this).attr("role-id");
                        $.post("${ctx}/role/revoke/request",{
                            roleId: roleId
                            , requestMappingId: '${requestMappingId}'
                        }, function (d) {
                            layer.msg(d.msg);
                            if (d.code == code_success){
                                reloadRoleList(data.curr);
                            }
                        }, "json");
                    });
                }
                layer.close(index);
            }, "json");
        }, loadingTime);
    }
</script>