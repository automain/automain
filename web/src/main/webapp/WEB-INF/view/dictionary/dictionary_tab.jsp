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
<div class="layui-form">
    <table class="layui-table">
        <thead>
        <tr>
            <th style="width: 18px;"><input type="checkbox" name="allDeleteDictionary" lay-skin="primary" lay-filter="all_delete_dictionary"></th>
            <th style="min-width: 140px; width: 140px;">操作</th>
            <th style="min-width: 150px;">表名</th>
            <th style="min-width: 150px;">字段名</th>
            <th style="min-width: 150px;">字典名</th>
            <th style="min-width: 150px;">字典值</th>
            <th style="min-width: 150px;">排序标识</th>
            <th style="min-width: 150px;">父级</th>
            <th style="min-width: 150px;">是否是叶子节点</th>
        </tr>
        </thead>
        <tbody id="dictionary_list_body">
        </tbody>
    </table>
</div>
<div id="dictionary_page"></div>
</body>
</html>
<script>
    var form, laypage, layer;
    layui.use(['form', 'layer', 'laypage', 'element'], function () {
        form = layui.form;
        layer = layui.layer;
        laypage = layui.laypage;
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
                doDelete(layer, "deleteDictionary", "${ctx}/dictionary/delete", reloadDictionaryList(1));
                layer.close(index);
            });
        });
        checkIsAllCheck(form, "all_delete_dictionary", "delete_dictionary", "allDeleteDictionary", "deleteDictionary");
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
                    $('thead input[name="allDeleteDictionary"]')[0].checked = false;
                    form.render();
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