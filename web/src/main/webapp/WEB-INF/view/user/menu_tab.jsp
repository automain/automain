<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<span class="layui-breadcrumb">
    <c:forEach items="${parentList}" var="parent">
        <a href="${ctx}/menu/forward?parentId=${parent.parentId}">${parent.menuName}</a>
    </c:forEach>
    <a><cite><c:out value="${cite}"/></cite></a>
</span>
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
<div class="layui-form">
    <table class="layui-table">
        <thead>
        <tr>
            <th style="width: 18px;"><input type="checkbox" name="allDeleteMenu" lay-skin="primary" lay-filter="all_delete_menu"></th>
            <th style="min-width: 230px; width: 230px;">操作</th>
            <th style="min-width: 150px;">请求路径</th>
            <th style="min-width: 150px;">菜单名称</th>
            <th style="min-width: 150px;">菜单图标</th>
            <th style="min-width: 150px;">菜单排序</th>
            <th style="min-width: 150px;">父级菜单</th>
            <th style="min-width: 150px;">是否是叶子节点</th>
        </tr>
        </thead>
        <tbody id="menu_list_body">
        </tbody>
    </table>
</div>
<div id="menu_page"></div>
</body>
</html>
<script>
    var form, laypage, layer;
    layui.use(['form', 'layer', 'laypage', 'element'], function () {
        form = layui.form;
        layer = layui.layer;
        laypage = layui.laypage;
        $("#menu_add").click(function () {
            alertByFull(layer, "添加", "${ctx}/menu/forward?forwardType=add&parentId=${parentId}&topId=${topId}");
        });
        $("#menu_delete").click(function () {
            layer.confirm('确认删除?', {icon: 3, title:'提示'}, function(index) {
                doDelete(layer, "deleteMenu", "${ctx}/menu/delete", reloadMenuList(1));
                layer.close(index);
            });
        });
        checkIsAllCheck(form, "all_delete_menu", "delete_menu", "allDeleteMenu", "deleteMenu");
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
                    $('thead input[name="allDeleteMenu"]')[0].checked = false;
                    form.render();
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