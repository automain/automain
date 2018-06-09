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
                            <button class="layui-btn layui-btn-sm layui-btn-normal" id="inner_ip_port_add">
                                <i class="fa fa-plus"></i> 添加
                            </button>
                        </div>
                        <div class="layui-inline">
                            <button class="layui-btn layui-btn-sm layui-btn-danger" id="inner_ip_port_delete">
                                <i class="fa fa-remove"></i> 删除
                            </button>
                        </div>
                        <div class="layui-inline">
                            <input type="text" class="layui-input" autocomplete="off" id="ip_search" placeholder="请输入内部地址">
                        </div>
                        <div class="layui-inline">
                            <input type="text" class="layui-input" autocomplete="off" id="port_search" placeholder="请输入端口号">
                        </div>
                        <div class="layui-inline">
                            <button class="layui-btn layui-btn-sm layui-btn-warm" id="inner_ip_port_search">
                                <i class="fa fa-search"></i> 搜索
                            </button>
                        </div>
                    </div>
                </blockquote>
                <table class="layui-table" lay-skin="line" lay-filter="tb_inner_ip_port" lay-data="{id: 'tb_inner_ip_port'}">
                    <thead>
                    <tr>
                        <th lay-data="{field:'inner_id',checkbox:true, fixed:'left'}"></th>
                        <th lay-data="{field:'ip', width:160}">内部地址</th>
                        <th lay-data="{field:'port', width:160}">端口号</th>
                        <th lay-data="{field:'operation', width:90, fixed:'right'}">操作</th>
                    </tr>
                    </thead>
                    <tbody id="inner_ip_port_list_body">
                    </tbody>
                </table>
                <div id="inner_ip_port_page"></div>
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
        $("#inner_ip_port_add").click(function () {
            alertByFull(layer, "添加", "${ctx}/inner/ip/port/forward?forwardType=add");
        });
        $("#inner_ip_port_delete").click(function () {
            layer.confirm('确认删除?', {icon: 3, title:'提示'}, function(index) {
                var checkStatusData = table.checkStatus('tb_inner_ip_port').data;
                var deleteCheck = new Array();
                checkStatusData.forEach(function(val, index){
                    deleteCheck.push(val.inner_id);
                });
                doDelete(layer, deleteCheck, "${ctx}/inner/ip/port/delete", reloadInnerIpPortList(1));
                layer.close(index);
            });
        });
        $("#inner_ip_port_search").click(function () {
            reloadInnerIpPortList(1);
        });
        reloadInnerIpPortList(1);
    });
    function reloadInnerIpPortList(page) {
        var index = layer.load();
        setTimeout(function () {
            $.post("${ctx}/inner/ip/port/list", {
                page: page
                ,ip: $("#ip_search").val()
                ,port: $("#port_search").val()
            }, function (data) {
                if (data.code == code_success) {
                    $("#inner_ip_port_list_body").html(data.data);
                    renderPage(laypage, "inner_ip_port_page", data.count, data.curr, reloadInnerIpPortList);
                    table.init('tb_inner_ip_port', {
                        height: 'full-190'
                    });
                    $(".update-btn").click(function () {
                        var updateId = $(this).attr("update-id");
                        alertByFull(layer, "编辑", "${ctx}/inner/ip/port/forward?forwardType=update&innerId=" + updateId);
                    });
                }
                layer.close(index);
            }, "json");
        }, loadingTime);
    }
</script>