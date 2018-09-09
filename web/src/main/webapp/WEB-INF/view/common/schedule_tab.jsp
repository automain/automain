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
                            <button class="layui-btn layui-btn-sm layui-btn-normal" id="schedule_add">
                                <i class="fa fa-plus"></i> 添加
                            </button>
                        </div>
                        <div class="layui-inline">
                            <input type="text" class="layui-input" autocomplete="off" id="schedule_name_search" placeholder="请输入任务名称">
                        </div>
                        <div class="layui-inline">
                            <input type="text" class="layui-input" autocomplete="off" id="start_execute_time_search" placeholder="请输入开始执行时间">
                        </div>
                        <div class="layui-inline">
                            <button class="layui-btn layui-btn-sm layui-btn-warm" id="schedule_search">
                                <i class="fa fa-search"></i> 搜索
                            </button>
                        </div>
                    </div>
                </blockquote>
                <table class="layui-table" lay-skin="line" lay-filter="tb_schedule" lay-data="{id: 'tb_schedule'}">
                    <thead>
                    <tr>
                        <th lay-data="{field:'schedule_name', width:160}">任务名称</th>
                        <th lay-data="{field:'schedule_url', width:160}">任务请求url</th>
                        <th lay-data="{field:'start_execute_time', width:160}">开始执行时间</th>
                        <th lay-data="{field:'delay_time', width:160}">间隔时间长度(秒)</th>
                        <th lay-data="{field:'last_execute_time', width:160}">上次执行时间</th>
                        <th lay-data="{field:'update_time', width:160}">修改时间</th>
                        <th lay-data="{field:'change', width:160}">状态</th>
                        <th lay-data="{field:'operation', width:180, fixed:'right'}">操作</th>
                    </tr>
                    </thead>
                    <tbody id="schedule_list_body">
                    </tbody>
                </table>
                <div id="schedule_page"></div>
            </div>
        </div>
    </div>
</div>
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
            elem: '#start_execute_time_search'
            ,type: 'datetime'
            ,range: true
        });
        $("#schedule_add").click(function () {
            alertByFull(layer, "添加", "${ctx}/schedule/forward?forwardType=add");
        });
        $("#schedule_search").click(function () {
            reloadScheduleList(1);
        });
        reloadScheduleList(1);
    });
    function reloadScheduleList(page) {
        var index = layer.load();
        setTimeout(function () {
            $.post("${ctx}/schedule/list", {
                page: page
                ,scheduleName: $("#schedule_name_search").val()
                ,startExecuteTimeRange: $("#start_execute_time_search").val()
            }, function (data) {
                if (data.code == code_success) {
                    $("#schedule_list_body").html(data.data);
                    renderPage(laypage, "schedule_page", data.count, data.curr, reloadScheduleList);
                    table.init('tb_schedule', {
                        height: 'full-190'
                    });
                    $(".update-btn").click(function () {
                        var updateId = $(this).attr("update-id");
                        alertByFull(layer, "编辑", "${ctx}/schedule/forward?forwardType=update&scheduleId=" + updateId);
                    });
                    $(".schedule-btn").click(function () {
                        var scheduleUrl = $(this).attr("schedule-url");
                        layer.confirm('确认执行?', {icon: 3, title: '提示'}, function (index) {
                            $.post("${ctx}" + scheduleUrl, function (data) {
                                layer.msg(data.msg);
                            });
                            layer.close(index);
                        });
                    });
                    form.on('switch(change)', function(obj){
                        var changeId = this.value;
                        $.post("${ctx}/schedule/change",{
                            changeId: changeId
                        }, function (data) {
                            layer.msg(data.msg);
                        },"json");
                    });
                }
                layer.close(index);
            }, "json");
        }, loadingTime);
    }
</script>