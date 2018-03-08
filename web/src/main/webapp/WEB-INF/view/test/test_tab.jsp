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
            <button class="layui-btn layui-btn-sm layui-btn-normal" id="test_add">
                <i class="fa fa-plus"></i> 添加
            </button>
        </div>
        <div class="layui-inline">
            <button class="layui-btn layui-btn-sm layui-btn-danger" id="test_delete">
                <i class="fa fa-remove"></i> 删除
            </button>
        </div>
        <div class="layui-inline">
            <button class="layui-btn layui-btn-sm layui-btn-warm" id="test_refresh">
                <i class="fa fa-refresh"></i> 刷新
            </button>
        </div>
    </div>
</blockquote>
<div class="layui-form">
    <table class="layui-table">
        <thead>
        <tr>
            <th style="width: 18px;"><input type="checkbox" name="allDeleteTest" lay-skin="primary" lay-filter="all_delete_test"></th>
            <th style="min-width: 120px;">操作</th>
            <th style="min-width: 150px;">测试名称</th>
            <th style="min-width: 150px;">测试textarea</th>
            <th style="min-width: 150px;">创建时间</th>
            <th style="min-width: 150px;">测试数量</th>
            <th style="min-width: 150px;">测试类型(1:普通,2文艺)</th>
        </tr>
        </thead>
        <tbody id="test_list_body">
        </tbody>
    </table>
</div>
<div id="test_page"></div>
</body>
</html>
<script>
    var form, laypage, layer;
    layui.use(['form', 'layer', 'laypage'], function () {
        form = layui.form;
        layer = layui.layer;
        laypage = layui.laypage;
        $("#test_add").click(function () {
            alertByFull(layer, "添加", "${ctx}/test/forward?forwardType=add");
        });
        $("#test_delete").click(function () {
            layer.confirm('确认删除?', {icon: 3, title:'提示'}, function(index) {
                doDelete(layer, "deleteTest", "${ctx}/test/delete", reloadTestList(1));
                layer.close(index);
            });
        });
        checkIsAllCheck(form, "all_delete_test", "delete_test", "allDeleteTest", "deleteTest");
        $("#test_refresh").click(function () {
            reloadTestList(1);
        });
        reloadTestList(1);
    });
    function reloadTestList(page) {
        var index = layer.load();
        setTimeout(function () {
            $.post("${ctx}/test/list", {
                page: page
            }, function (data) {
                if (data.code == code_success) {
                    $("#test_list_body").html(data.data);
                    renderPage(laypage, "test_page", data.count, data.curr, reloadTestList);
                    $('thead input[name="allDeleteTest"]')[0].checked = false;
                    form.render();
                    $(".update-btn").click(function () {
                        var updateId = $(this).attr("update-id");
                        alertByFull(layer, "编辑", "${ctx}/test/forward?forwardType=update&testId=" + updateId);
                    });
                    $(".detail-btn").click(function () {
                        var detailId = $(this).attr("detail-id");
                        alertByFull(layer, "详情", "${ctx}/test/forward?forwardType=detail&testId=" + detailId);
                    });
                }
                layer.close(index);
            }, "json");
        }, loadingTime);
    }
</script>