<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<fieldset class="layui-elem-field">
    <div class="layui-field-box">
        <div class="layadmin-header">
            <span class="layui-breadcrumb">
                <c:forEach items="${parentList}" var="parent">
                    <a href="${ctx}/menu/forward?parentId=${parent.parentId}">${parent.menuName}</a>
                </c:forEach>
                <a><cite><c:out value="${cite}"/></cite></a>
            </span>
        </div>
    </div>
</fieldset>
<div class="layui-fluid">
    <div class="layui-row">
        <div class="layui-col-md12">
            <div class="layui-card">
                <blockquote class="layui-elem-quote">
                    <div class="layui-form">
                        <div class="layui-inline">
                            <button class="layui-btn layui-btn-sm layui-btn-normal" id="menu_add">
                                <i class="fa fa-plus"></i> 添加
                            </button>
                        </div>
                        <div class="layui-inline">
                            <button class="layui-btn layui-btn-sm layui-btn-danger" id="menu_delete">
                                <i class="fa fa-remove"></i> 删除
                            </button>
                        </div>
                        <div class="layui-inline">
                            <input type="text" class="layui-input" autocomplete="off" id="menu-name-search" placeholder="请输入菜单名称">
                        </div>
                        <div class="layui-inline">
                            <button class="layui-btn layui-btn-sm layui-btn-warm" id="menu_refresh">
                                <i class="fa fa-search"></i> 搜索
                            </button>
                        </div>
                    </div>
                </blockquote>
                <table class="layui-table" lay-skin="line" lay-filter="tb_menu" lay-data="{id: 'tb_menu'}">
                    <thead>
                    <tr>
                        <th lay-data="{field:'menu_id',checkbox:true, fixed:'left'}"></th>
                        <th lay-data="{field:'request_url', width:260}">请求路径</th>
                        <th lay-data="{field:'menu_name', width:160}">菜单名称</th>
                        <th lay-data="{field:'menu_icon', width:160}">菜单图标</th>
                        <th lay-data="{field:'sequence_number', width:160}">菜单排序</th>
                        <th lay-data="{field:'parent_id', width:160}">父级菜单</th>
                        <th lay-data="{field:'is_spread', width:160}">是否默认展开</th>
                        <th lay-data="{field:'is_leaf', width:160}">是否是叶子节点</th>
                        <th lay-data="{field:'operation', width:270, fixed:'right'}">操作</th>
                    </tr>
                    </thead>
                    <tbody id="menu_list_body">
                    </tbody>
                </table>
                <div id="menu_page"></div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<script>
    var form, laypage, layer, table;
    layui.use(['form', 'layer', 'laypage', 'element', 'table'], function () {
        form = layui.form;
        layer = layui.layer;
        laypage = layui.laypage;
        table = layui.table;
        $("#menu_add").click(function () {
            alertByFull(layer, "添加", "${ctx}/menu/forward?forwardType=add&parentId=${parentId}&topId=${topId}");
        });
        $("#menu_delete").click(function () {
            layer.confirm('确认删除?', {icon: 3, title: '提示'}, function (index) {
                var checkStatusData = table.checkStatus('tb_menu').data;
                var deleteCheck = new Array();
                checkStatusData.forEach(function (val, index) {
                    deleteCheck.push(val.menu_id);
                });
                doDelete(layer, deleteCheck, "${ctx}/menu/delete", reloadMenuList(1));
                layer.close(index);
            });
        });
        $("#menu_refresh").click(function () {
            reloadMenuList(1);
        });
        reloadMenuList(1);
    });

    function reloadMenuList(page) {
        var index = layer.load();
        setTimeout(function () {
            $.post("${ctx}/menu/list", {
                page: page,
                parentId: '${parentId}',
                menuName: $("#menu-name-search").val()
            }, function (data) {
                if (data.code == code_success) {
                    $("#menu_list_body").html(data.data);
                    renderPage(laypage, "menu_page", data.count, data.curr, reloadMenuList);
                    table.init('tb_menu', {
                        height: 'full-210'
                    });
                    $(".update-btn").click(function () {
                        var updateId = $(this).attr("update-id");
                        alertByFull(layer, "编辑", "${ctx}/menu/forward?forwardType=update&menuId=" + updateId);
                    });
                    $(".child-btn").click(function () {
                        var parentId = $(this).attr("parent-id");
                        window.location.href = '${ctx}/menu/forward?parentId=' + parentId;
                    });
                    $(".grant-role-btn").click(function () {
                        var menuId = $(this).attr("menu-id");
                        var menuName = $(this).attr("menu-name");
                        alertByFull(layer, "分配角色(" + menuName + ")", "${ctx}/menu/forward?forwardType=role&menuId=" + menuId);
                    });
                }
                layer.close(index);
            }, "json");
        }, loadingTime);
    }
</script>