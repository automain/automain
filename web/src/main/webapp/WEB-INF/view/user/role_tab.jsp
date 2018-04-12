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
            <button class="layui-btn layui-btn-sm layui-btn-normal" id="role_add">
                <i class="fa fa-plus"></i> 添加
            </button>
        </div>
        <div class="layui-inline">
            <button class="layui-btn layui-btn-sm layui-btn-danger" id="role_delete">
                <i class="fa fa-remove"></i> 删除
            </button>
        </div>
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
<table class="layui-table" lay-skin="line" lay-filter="tb_role" lay-data="{id: 'tb_role'}">
    <thead>
    <tr>
        <th lay-data="{field:'role_id',checkbox:true, fixed:'left'}"></th>
        <th lay-data="{field:'role_name', width:150}">角色名称</th>
        <th lay-data="{field:'role_label', width:150}">角色标识</th>
        <th lay-data="{field:'operation', width:350, fixed:'right'}">操作</th>
    </tr>
    </thead>
    <tbody id="role_list_body">
    </tbody>
</table>
<div id="role_page"></div>
</body>
</html>
<script>
    var form, laypage, layer, table;
    layui.use(['form', 'layer', 'laypage', 'table'], function () {
        form = layui.form;
        layer = layui.layer;
        laypage = layui.laypage;
        table = layui.table;
        $("#role_add").click(function () {
            alertByFull(layer, "添加", "${ctx}/role/forward?forwardType=add");
        });
        $("#role_delete").click(function () {
            layer.confirm('确认删除?', {icon: 3, title:'提示'}, function(index) {
                var checkStatusData = table.checkStatus('tb_role').data;
                var deleteCheck = new Array();
                checkStatusData.forEach(function(val, index){
                    deleteCheck.push(val.role_id);
                });
                doDelete(layer, deleteCheck, "${ctx}/role/delete", reloadRoleList(1));
                layer.close(index);
            });
        });
        $("#role_refresh").click(function () {
            reloadRoleList(1);
        });
        reloadRoleList(1);
    });
    function reloadRoleList(page) {
        var index = layer.load();
        setTimeout(function () {
            $.post("${ctx}/role/list", {
                page: page,
                roleName: $("#role-name-search").val()
            }, function (data) {
                if (data.code == code_success) {
                    $("#role_list_body").html(data.data);
                    renderPage(laypage, "role_page", data.count, data.curr, reloadRoleList);
                    table.init('tb_role', {
                        height: 'full-190'
                    });
                    $(".update-btn").click(function () {
                        var updateId = $(this).attr("update-id");
                        alertByFull(layer, "编辑", "${ctx}/role/forward?forwardType=update&roleId=" + updateId);
                    });
                    $(".grant-menu-btn").click(function () {
                        var roleId = $(this).attr("role-id");
                        alertByFull(layer, "分配菜单", "${ctx}/role/forward?forwardType=menu&roleId=" + roleId);
                    });
                    $(".grant-request-btn").click(function () {
                        var roleId = $(this).attr("role-id");
                        alertByFull(layer, "分配权限", "${ctx}/role/forward?forwardType=request&roleId=" + roleId);
                    });
                    $(".grant-user-btn").click(function () {
                        var roleId = $(this).attr("role-id");
                        alertByFull(layer, "相关用户", "${ctx}/role/forward?forwardType=user&roleId=" + roleId);
                    });
                }
                layer.close(index);
            }, "json");
        }, loadingTime);
    }
</script>