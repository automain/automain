<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<span class="layui-breadcrumb">
    <c:forEach items="${parentList}" var="parent">
        <a href="${ctx}/dictionary/forward?parentId=${parent.parentId}">${parent.dictionaryName}</a>
    </c:forEach>
    <a><cite><c:out value="${cite}"/></cite></a>
</span>
<blockquote class="layui-elem-quote" style="margin-top: 5px;">
    <div class="layui-form">
        <div class="layui-inline">
            <button class="layui-btn layui-btn-sm layui-btn-normal" id="dictionary_add">
                <i class="fa fa-plus"></i> 添加
            </button>
        </div>
        <div class="layui-inline">
            <button class="layui-btn layui-btn-sm layui-btn-danger" id="dictionary_delete">
                <i class="fa fa-remove"></i> 删除
            </button>
        </div>
        <div class="layui-inline">
            <select class="layui-select" id="table-name-search" lay-filter="table-name-search" lay-search>
                <option value="">请选择表名</option>
                <c:forEach items="${tableNameList}" var="tableName">
                    <option value="${tableName}">${tableName}</option>
                </c:forEach>
            </select>
        </div>
        <div class="layui-inline">
            <select class="layui-select" id="column-name-search">
                <option value="">请选择字段名</option>
            </select>
        </div>
        <div class="layui-inline">
            <button class="layui-btn layui-btn-sm layui-btn-warm" id="dictionary_refresh">
                <i class="fa fa-search"></i> 搜索
            </button>
        </div>
    </div>
</blockquote>
<table class="layui-table" lay-skin="line" lay-filter="tb_dictionary" lay-data="{id: 'tb_dictionary'}">
    <thead>
    <tr>
        <th lay-data="{field:'dictionary_id',checkbox:true, fixed:'left'}"></th>
        <th lay-data="{field:'operation', width:180, fixed:'left'}">操作</th>
        <th lay-data="{field:'dict_table_name', width:150}">表名</th>
        <th lay-data="{field:'dict_column_name', width:150}">字段名</th>
        <th lay-data="{field:'dictionary_name', width:150}">字典名</th>
        <th lay-data="{field:'dictionary_value', width:150}">字典值</th>
        <th lay-data="{field:'sequence_number', width:150}">排序标识</th>
        <th lay-data="{field:'parent_id', width:150}">父级</th>
        <th lay-data="{field:'is_leaf', width:150}">是否是叶子节点</th>
    </tr>
    </thead>
    <tbody id="dictionary_list_body">
    </tbody>
</table>
<div id="dictionary_page"></div>
</body>
</html>
<script>
    var form, laypage, layer, table;
    layui.use(['form', 'layer', 'laypage', 'element', 'table'], function () {
        form = layui.form;
        layer = layui.layer;
        laypage = layui.laypage;
        table = layui.table;
        form.on('select(table-name-search)',function(data){
            if (data.value != ''){
                $.post("${ctx}/dictionary/column/list",{
                    tableName: data.value
                }, function(d){
                    if(d.code == code_success){
                        var columnList = d.columnList;
                        $("#column-name-search").empty();
                        $("#column-name-search").append('<option value="">请选择字段名</option>');
                        for (var i = 0; i < columnList.length; i++) {
                            $("#column-name-search").append('<option value="' + columnList[i] + '">' + columnList[i] + '</option>');
                        }
                        form.render('select');
                    }
                },"json");
            }
        });
        $("#dictionary_add").click(function () {
            alertByFull(layer, "添加", "${ctx}/dictionary/forward?forwardType=add&parentId=${parentId}");
        });
        $("#dictionary_delete").click(function () {
            layer.confirm('确认删除?', {icon: 3, title:'提示'}, function(index) {
                var checkStatusData = table.checkStatus('tb_dictionary').data;
                var deleteCheck = new Array();
                checkStatusData.forEach(function(val, index){
                    deleteCheck.push(val.dictionary_id);
                });
                doDelete(layer, deleteCheck, "${ctx}/dictionary/delete", reloadDictionaryList(1));
                layer.close(index);
            });
        });
        $("#dictionary_refresh").click(function () {
            reloadDictionaryList(1);
        });
        reloadDictionaryList(1);
    });
    function reloadDictionaryList(page) {
        var index = layer.load();
        setTimeout(function () {
            $.post("${ctx}/dictionary/list", {
                page: page,
                parentId: '${parentId}',
                dictTableName: $("#table-name-search").val(),
                dictColumnName: $("#column-name-search").val()
            }, function (data) {
                if (data.code == code_success) {
                    $("#dictionary_list_body").html(data.data);
                    renderPage(laypage, "dictionary_page", data.count, data.curr, reloadDictionaryList);
                    table.init('tb_dictionary', {
                        height: 'full-190'
                    });
                    $(".update-btn").click(function () {
                        var updateId = $(this).attr("update-id");
                        alertByFull(layer, "编辑", "${ctx}/dictionary/forward?forwardType=update&dictionaryId=" + updateId);
                    });
                    $(".child-btn").click(function () {
                        var parentId = $(this).attr("parent-id");
                        window.location.href = '${ctx}/dictionary/forward?parentId=' + parentId;
                    });
                }
                layer.close(index);
            }, "json");
        }, loadingTime);
    }
</script>