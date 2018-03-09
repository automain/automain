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
            <button class="layui-btn layui-btn-sm layui-btn-normal" id="request_add">
                <i class="fa fa-plus"></i> 添加
            </button>
        </div>
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
<div class="layui-form">
    <table class="layui-table">
        <thead>
        <tr>
            <th style="min-width:150px; width: 150px;">操作</th>
            <th style="min-width: 150px;">请求相对路径</th>
            <th style="min-width: 150px;">请求处理类的全路径</th>
            <th style="min-width: 150px;">注释</th>
        </tr>
        </thead>
        <tbody id="request_list_body">
        </tbody>
    </table>
</div>
<div id="request_page"></div>
</body>
</html>
<script>
    var form, laypage, layer;
    layui.use(['form', 'layer', 'laypage'], function () {
        form = layui.form;
        layer = layui.layer;
        laypage = layui.laypage;
        $("#request_add").click(function () {
            alertByFull(layer, "添加", "${ctx}/request/forward?forwardType=add");
        });
        $("#request_refresh").click(function () {
            reloadRequestList(1);
        });
        reloadRequestList(1);
    });
    function reloadRequestList(page) {
        var index = layer.load();
        setTimeout(function () {
            $.post("${ctx}/request/list", {
                requestUrl: $("#request-url-search").val(),
                page: page
            }, function (data) {
                if (data.code == code_success) {
                    $("#request_list_body").html(data.data);
                    renderPage(laypage, "request_page", data.count, data.curr, reloadRequestList);
                    $(".update-btn").click(function () {
                        var updateId = $(this).attr("update-id");
                        alertByFull(layer, "编辑", "${ctx}/request/forward?forwardType=update&requestMappingId=" + updateId);
                    });
                    $(".grant-role-btn").click(function () {
                        var requestId = $(this).attr("request-id");
                        var requestUrl = $(this).attr("request-url");
                        alertByFull(layer, "分配角色(" + requestUrl + ")", "${ctx}/request/forward?forwardType=role&requestMappingId=" + requestId);
                    });
                }
                layer.close(index);
            }, "json");
        }, loadingTime);
    }
</script>