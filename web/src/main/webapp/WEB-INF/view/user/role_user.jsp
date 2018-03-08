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
<div class="layui-form">
    <table class="layui-table">
        <colgroup>
            <col width="50">
            <col width="270">
            <col>
        </colgroup>
        <thead>
        <tr>
            <th>操作</th>
            <th>用户名</th>
            <th>手机号</th>
            <th>创建时间</th>
            <th>邮箱</th>
        </tr>
        </thead>
        <tbody id="user_list_body">
        </tbody>
    </table>
</div>
<div id="user_page"></div>
</body>
</html>
<script>
    var form, laypage, layer;
    layui.use(['form', 'layer', 'laypage', 'laydate'], function () {
        form = layui.form;
        layer = layui.layer;
        laypage = layui.laypage;
        var laydate = layui.laydate;
        laydate.render({
            elem: '#create-time-search'
            ,type: 'datetime'
            ,range: true
        });
        $("#user_refresh").click(function () {
            reloadUserList(1);
        });
        reloadUserList(1);
    });
    function reloadUserList(page) {
        var index = layer.load();
        setTimeout(function () {
            $.post("${ctx}/role/user/list", {
                page: page,
                roleId: '${roleId}',
                userName: $("#user-name-search").val(),
                cellphone: $("#cellphone-search").val(),
                createTimeRange: $("#create-time-search").val()
            }, function (data) {
                if (data.code == code_success) {
                    $("#user_list_body").html(data.data);
                    renderPage(laypage, "user_page", data.count, data.curr, reloadUserList);
                    $(".grant-role-btn").click(function () {
                        var userId = $(this).attr("user-id");
                        $.post("${ctx}/role/grant/user",{
                            roleId: '${roleId}'
                            , userId: userId
                        }, function (d) {
                            layer.msg(d.msg);
                            if (d.code == code_success){
                                reloadUserList(data.curr);
                            }
                        }, "json");
                    });
                    $(".revoke-role-btn").click(function () {
                        var userId = $(this).attr("user-id");
                        $.post("${ctx}/role/revoke/user",{
                            roleId: '${roleId}'
                            , userId: userId
                        }, function (d) {
                            layer.msg(d.msg);
                            if (d.code == code_success){
                                reloadUserList(data.curr);
                            }
                        }, "json");
                    });
                }
                layer.close(index);
            }, "json");
        }, loadingTime);
    }
</script>