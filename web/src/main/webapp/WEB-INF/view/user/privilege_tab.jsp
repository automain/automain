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
                    <a href="${ctx}/privilege/forward?parentId=${parent.parentId}">${parent.menuName}</a>
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
                            <button class="layui-btn layui-btn-sm layui-btn-normal" id="privilege_add">
                                <i class="fa fa-plus"></i> 添加
                            </button>
                        </div>
                        <div class="layui-inline">
                            <button class="layui-btn layui-btn-sm layui-btn-danger" id="privilege_delete">
                                <i class="fa fa-remove"></i> 删除
                            </button>
                        </div>
                        <div class="layui-inline">
                            <input type="text" class="layui-input" autocomplete="off" id="privilege_name_search" placeholder="请输入权限名称">
                        </div>
                        <div class="layui-inline">
                            <button class="layui-btn layui-btn-sm layui-btn-warm" id="privilege_search">
                                <i class="fa fa-search"></i> 搜索
                            </button>
                        </div>
                    </div>
                </blockquote>
                <table class="layui-table" lay-skin="line" lay-filter="tb_privilege" lay-data="{id: 'tb_privilege'}">
                    <thead>
                    <tr>
                        <th lay-data="{field:'privilege_id',checkbox:true, fixed:'left'}"></th>
                        <th lay-data="{field:'privilege_label', width:160}">权限标识</th>
                        <th lay-data="{field:'privilege_name', width:160}">权限名称</th>
                        <th lay-data="{field:'operation', width:270, fixed:'right'}">操作</th>
                    </tr>
                    </thead>
                    <tbody id="privilege_list_body">
                    </tbody>
                </table>
                <div id="privilege_page"></div>
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
        $("#privilege_add").click(function () {
            alertByFull(layer, "添加", "${ctx}/privilege/forward?forwardType=add&parentId=${parentId}&topId=${topId}");
        });
        $("#privilege_delete").click(function () {
            layer.confirm('确认删除?', {icon: 3, title:'提示'}, function(index) {
                var checkStatusData = table.checkStatus('tb_privilege').data;
                var deleteCheck = new Array();
                checkStatusData.forEach(function(val, index){
                    deleteCheck.push(val.privilege_id);
                });
                doDelete(layer, deleteCheck, "${ctx}/privilege/delete", reloadPrivilegeList(1));
                layer.close(index);
            });
        });
        $("#privilege_search").click(function () {
            reloadPrivilegeList(1);
        });
        reloadPrivilegeList(1);
    });
    function reloadPrivilegeList(page) {
        var index = layer.load();
        setTimeout(function () {
            $.post("${ctx}/privilege/list", {
                page: page
                , parentId: '${parentId}'
                ,privilegeName: $("#privilege_name_search").val()
            }, function (data) {
                if (data.code == code_success) {
                    $("#privilege_list_body").html(data.data);
                    renderPage(laypage, "privilege_page", data.count, data.curr, reloadPrivilegeList);
                    table.init('tb_privilege', {
                        height: 'full-210'
                    });
                    $(".update-btn").click(function () {
                        var updateId = $(this).attr("update-id");
                        alertByFull(layer, "编辑", "${ctx}/privilege/forward?forwardType=update&privilegeId=" + updateId);
                    });
                    $(".child-btn").click(function () {
                        var parentId = $(this).attr("parent-id");
                        window.location.href = '${ctx}/privilege/forward?parentId=' + parentId;
                    });
                    $(".grant-role-btn").click(function () {
                        var privilegeId = $(this).attr("privilege-id");
                        var privilegeName = $(this).attr("privilege-name");
                        alertByFull(layer, "分配角色(" + privilegeName + ")", "${ctx}/privilege/forward?forwardType=role&privilegeId=" + privilegeId);
                    });
                }
                layer.close(index);
            }, "json");
        }, loadingTime);
    }
</script>