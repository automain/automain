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
            <input type="text" class="layui-input" autocomplete="off" id="create_time_search" placeholder="请输入创建时间">
        </div>
        <div class="layui-inline">
            <button class="layui-btn layui-btn-sm layui-btn-warm" id="slow_log_search">
                <i class="fa fa-search"></i> 搜索
            </button>
        </div>
    </div>
</blockquote>
<table class="layui-table" lay-skin="line" lay-filter="db_slow_log" lay-data="{id: 'db_slow_log'}">
    <thead>
    <tr>
        <th lay-data="{field:'create_time', width:160}">创建时间</th>
        <th lay-data="{field:'pool_name', width:160}">连接池名称</th>
        <th lay-data="{field:'slow_db', width:160}">慢查询库</th>
        <th lay-data="{field:'slow_state', width:160}">慢查询状态</th>
        <th lay-data="{field:'slow_time', width:160}">慢查询用时</th>
        <th lay-data="{field:'slow_sql', width:460}">慢查询sql</th>
    </tr>
    </thead>
    <tbody id="slow_log_list_body">
    </tbody>
</table>
<div id="slow_log_page"></div>
</body>
</html>
<script>
    var form, laypage, layer, table;
    layui.use(['form', 'layer', 'laypage', 'table', 'laydate'], function () {
        form = layui.form;
        layer = layui.layer;
        laypage = layui.laypage;
        table = layui.table;
        var laydate = layui.laydate;
        laydate.render({
            elem: '#create_time_search'
            ,type: 'datetime'
            ,range: true
        });
        $("#slow_log_search").click(function () {
            reloadSlowLogList(1);
        });
        reloadSlowLogList(1);
    });
    function reloadSlowLogList(page) {
        var index = layer.load();
        setTimeout(function () {
            $.post("${ctx}/monitor/slowlog/list", {
                page: page
                ,createTimeRange: $("#create_time_search").val()
            }, function (data) {
                if (data.code == code_success) {
                    $("#slow_log_list_body").html(data.data);
                    renderPage(laypage, "slow_log_page", data.count, data.curr, reloadSlowLogList);
                    table.init('db_slow_log', {
                        height: 'full-190'
                    });
                }
                layer.close(index);
            }, "json");
        }, loadingTime);
    }
</script>