<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row">
        <div class="layui-col-md12">
            <div class="layui-card">
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
                <table class="layui-table" lay-skin="line" lay-filter="menu_role" lay-data="{id: 'menu_role'}">
                    <thead>
                    <tr>
                        <th lay-data="{field:'role_name', width:160}">角色名称</th>
                        <th lay-data="{field:'role_label', width:160}">角色标识</th>
                        <th lay-data="{field:'operation', width:180, fixed:'right'}">操作</th>
                    </tr>
                    </thead>
                    <tbody id="role_list_body">
                    </tbody>
                </table>
                <div id="role_page"></div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<script>
    var form, laypage, layer, table;
    layui.use(['form', 'layer', 'laypage', 'table'], function () {
        form = layui.form;
        layer = layui.layer;
        laypage = layui.laypage;
        table = layui.table;
        $("#role_refresh").click(function () {
            reloadRoleList(1);
        });
        reloadRoleList(1);
    });

    function reloadRoleList(page) {
        var index = layer.load();
        setTimeout(function () {
            $.post("${ctx}/menu/role/list", {
                page: page,
                menuId: '${menuId}',
                roleName: $("#role-name-search").val()
            }, function (data) {
                if (data.code == code_success) {
                    $("#role_list_body").html(data.data);
                    renderPage(laypage, "role_page", data.count, data.curr, reloadRoleList);
                    table.init('menu_role', {
                        height: 'full-190'
                    });
                    $(".grant-role-btn").click(function () {
                        var roleId = $(this).attr("role-id");
                        $.post("${ctx}/menu/grant/role", {
                            roleId: roleId
                            , menuId: '${menuId}'
                        }, function (d) {
                            layer.msg(d.msg);
                            if (d.code == code_success) {
                                reloadRoleList(data.curr);
                            }
                        }, "json");
                    });
                    $(".revoke-role-btn").click(function () {
                        var roleId = $(this).attr("role-id");
                        $.post("${ctx}/menu/revoke/role", {
                            roleId: roleId
                            , menuId: '${menuId}'
                        }, function (d) {
                            layer.msg(d.msg);
                            if (d.code == code_success) {
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