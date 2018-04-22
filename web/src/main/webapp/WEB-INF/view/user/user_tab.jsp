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
            <button class="layui-btn layui-btn-sm layui-btn-normal" id="user_add">
                <i class="fa fa-plus"></i> 添加
            </button>
        </div>
        <div class="layui-inline">
            <button class="layui-btn layui-btn-sm layui-btn-danger" id="user_delete">
                <i class="fa fa-remove"></i> 删除
            </button>
        </div>
        <div class="layui-inline">
            <input type="text" class="layui-input" autocomplete="off" id="user-name-search" placeholder="请输入用户名">
        </div>
        <div class="layui-inline">
            <input type="text" class="layui-input" autocomplete="off" id="cellphone-search" placeholder="请输入手机号">
        </div>
        <div class="layui-inline">
            <input type="text" class="layui-input" id="create-time-search" placeholder="请选择创建时间范围">
        </div>
        <div class="layui-inline">
            <button class="layui-btn layui-btn-sm layui-btn-warm" id="user_refresh">
                <i class="fa fa-search"></i> 搜索
            </button>
        </div>
    </div>
</blockquote>
<table class="layui-table" lay-skin="line" lay-filter="tb_user" lay-data="{id: 'tb_user'}">
    <thead>
    <tr>
        <th lay-data="{field:'user_id',checkbox:true, fixed:'left'}"></th>
        <th lay-data="{field:'user_name', width:160}">用户名</th>
        <th lay-data="{field:'cellphone', width:160}">手机号</th>
        <th lay-data="{field:'create_time', width:160}">创建时间</th>
        <th lay-data="{field:'email', width:250}">邮箱</th>
        <th lay-data="{field:'operation', width:270, fixed:'right'}">操作</th>
    </tr>
    </thead>
    <tbody id="user_list_body">
    </tbody>
</table>
<div id="user_page"></div>
</body>
</html>
<script>
    var form, laypage, layer, table;
    layui.use(['form', 'layer', 'laypage', 'laydate', 'table'], function () {
        form = layui.form;
        layer = layui.layer;
        laypage = layui.laypage;
        table = layui.table;
        var laydate = layui.laydate;
        laydate.render({
            elem: '#create-time-search'
            ,type: 'datetime'
            ,range: true
        });
        $("#user_add").click(function () {
            alertByFull(layer, "添加", "${ctx}/user/forward?forwardType=add");
        });
        $("#user_delete").click(function () {
            layer.confirm('确认删除?', {icon: 3, title:'提示'}, function(index) {
                var checkStatusData = table.checkStatus('tb_user').data;
                var deleteCheck = new Array();
                checkStatusData.forEach(function(val, index){
                    deleteCheck.push(val.user_id);
                });
                doDelete(layer, deleteCheck, "${ctx}/user/delete", reloadUserList(1));
                layer.close(index);
            });
        });
        $("#user_refresh").click(function () {
            reloadUserList(1);
        });
        reloadUserList(1);
    });
    function reloadUserList(page) {
        var index = layer.load();
        setTimeout(function () {
            $.post("${ctx}/user/list", {
                page: page,
                userName: $("#user-name-search").val(),
                cellphone: $("#cellphone-search").val(),
                createTimeRange: $("#create-time-search").val()
            }, function (data) {
                if (data.code == code_success) {
                    $("#user_list_body").html(data.data);
                    renderPage(laypage, "user_page", data.count, data.curr, reloadUserList);
                    table.init('tb_user', {
                        height: 'full-190'
                    });
                    $(".update-btn").click(function () {
                        var updateId = $(this).attr("update-id");
                        alertByFull(layer, "编辑", "${ctx}/user/forward?forwardType=update&userId=" + updateId);
                    });
                    $(".reset-pwd-btn").click(function () {
                        var userId = $(this).attr("user-id");
                        layer.confirm('确认重置密码?',{icon: 3, title:'重置密码'}, function(index){
                            $.post("${ctx}/user/reset/pwd", {
                                userId: userId
                            },function(data){
                                layer.msg(data.msg);
                                if (data.code == code_success) {
                                    reloadUserList(1);
                                }
                            },"json");
                            layer.close(index);
                        });
                    });
                    $(".grant-role-btn").click(function () {
                        var userId = $(this).attr("user-id");
                        var usreName = $(this).attr("user-name");
                        alertByFull(layer, "分配角色(" + usreName + ")", "${ctx}/user/forward?forwardType=role&userId=" + userId);
                    });
                }
                layer.close(index);
            }, "json");
        }, loadingTime);
    }
</script>