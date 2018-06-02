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
                            <input type="text" class="layui-input" autocomplete="off" id="request-url-search" placeholder="请输入请求相对路径">
                        </div>
                        <div class="layui-inline">
                            <button class="layui-btn layui-btn-sm layui-btn-warm" id="request_refresh">
                                <i class="fa fa-search"></i> 搜索
                            </button>
                        </div>
                    </div>
                </blockquote>
                <table class="layui-table" lay-skin="line" lay-filter="role_request" lay-data="{id: 'role_request'}">
                    <thead>
                    <tr>
                        <th lay-data="{field:'request_url', width:200}">请求相对路径</th>
                        <th lay-data="{field:'operation', width:180, fixed:'right'}">操作</th>
                    </tr>
                    </thead>
                    <tbody id="request_list_body">
                    </tbody>
                </table>
                <div id="request_page"></div>
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
        $("#request_refresh").click(function () {
            reloadRequestList(1);
        });
        reloadRequestList(1);
    });

    function reloadRequestList(page) {
        var index = layer.load();
        setTimeout(function () {
            $.post("${ctx}/role/request/list", {
                roleId: '${roleId}',
                requestUrl: $("#request-url-search").val(),
                page: page
            }, function (data) {
                if (data.code == code_success) {
                    $("#request_list_body").html(data.data);
                    renderPage(laypage, "request_page", data.count, data.curr, reloadRequestList);
                    table.init('role_request', {
                        height: 'full-190'
                    });
                    $(".grant-request-btn").click(function () {
                        var requestUrl = $(this).attr("request-url");
                        $.post("${ctx}/role/grant/request", {
                            roleId: '${roleId}'
                            , requestUrl: requestUrl
                        }, function (d) {
                            layer.msg(d.msg);
                            if (d.code == code_success) {
                                reloadRequestList(data.curr);
                            }
                        }, "json");
                    });
                    $(".revoke-request-btn").click(function () {
                        var requestUrl = $(this).attr("request-url");
                        $.post("${ctx}/role/revoke/request", {
                            roleId: '${roleId}'
                            , requestUrl: requestUrl
                        }, function (d) {
                            layer.msg(d.msg);
                            if (d.code == code_success) {
                                reloadRequestList(data.curr);
                            }
                        }, "json");
                    });
                }
                layer.close(index);
            }, "json");
        }, loadingTime);
    }
</script>