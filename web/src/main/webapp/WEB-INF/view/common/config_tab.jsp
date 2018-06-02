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
                            <button class="layui-btn layui-btn-sm layui-btn-normal" id="config_add">
                                <i class="fa fa-plus"></i> 添加
                            </button>
                        </div>
                        <div class="layui-inline">
                            <button class="layui-btn layui-btn-sm layui-btn-danger" id="config_delete">
                                <i class="fa fa-remove"></i> 删除
                            </button>
                        </div>
                        <div class="layui-inline">
                            <input type="text" class="layui-input" autocomplete="off" id="config_key_search" placeholder="请输入配置key">
                        </div>
                        <div class="layui-inline">
                            <button class="layui-btn layui-btn-sm layui-btn-warm" id="config_search">
                                <i class="fa fa-search"></i> 搜索
                            </button>
                        </div>
                    </div>
                </blockquote>
                <table class="layui-table" lay-skin="line" lay-filter="tb_config" lay-data="{id: 'tb_config'}">
                    <thead>
                    <tr>
                        <th lay-data="{field:'config_id',checkbox:true, fixed:'left'}"></th>
                        <th lay-data="{field:'config_key', width:160}">配置key</th>
                        <th lay-data="{field:'config_value', width:160}">配置value</th>
                        <th lay-data="{field:'config_comment', width:160}">配置描述</th>
                        <th lay-data="{field:'create_time', width:160}">创建时间</th>
                        <th lay-data="{field:'update_time', width:160}">更新时间</th>
                        <th lay-data="{field:'operation', width:90, fixed:'right'}">操作</th>
                    </tr>
                    </thead>
                    <tbody id="config_list_body">
                    </tbody>
                </table>
                <div id="config_page"></div>
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
        $("#config_add").click(function () {
            alertByFull(layer, "添加", "${ctx}/config/forward?forwardType=add");
        });
        $("#config_delete").click(function () {
            layer.confirm('确认删除?', {icon: 3, title: '提示'}, function (index) {
                var checkStatusData = table.checkStatus('tb_config').data;
                var deleteCheck = new Array();
                checkStatusData.forEach(function (val, index) {
                    deleteCheck.push(val.config_id);
                });
                doDelete(layer, deleteCheck, "${ctx}/config/delete", reloadConfigList(1));
                layer.close(index);
            });
        });
        $("#config_search").click(function () {
            reloadConfigList(1);
        });
        reloadConfigList(1);
    });

    function reloadConfigList(page) {
        var index = layer.load();
        setTimeout(function () {
            $.post("${ctx}/config/list", {
                page: page
                , configKey: $("#config_key_search").val()
            }, function (data) {
                if (data.code == code_success) {
                    $("#config_list_body").html(data.data);
                    renderPage(laypage, "config_page", data.count, data.curr, reloadConfigList);
                    table.init('tb_config', {
                        height: 'full-190'
                    });
                    $(".update-btn").click(function () {
                        var updateId = $(this).attr("update-id");
                        alertByFull(layer, "编辑", "${ctx}/config/forward?forwardType=update&configId=" + updateId);
                    });
                }
                layer.close(index);
            }, "json");
        }, loadingTime);
    }
</script>