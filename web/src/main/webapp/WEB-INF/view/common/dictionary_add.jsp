<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<form class="layui-form layui-form-pane" action="">
    <input type="hidden" name="parentId" value="${parentId}">
    <div class="layui-form-item">
        <label class="layui-form-label">表名</label>
        <div class="layui-input-block">
            <select class="layui-select" id="dict-table-name" name="dictTableName" lay-filter="dict-table-name" <c:if test="${parent == null}">lay-search</c:if>
            <c:if test="${parent != null}">disabled</c:if>>
                <c:forEach items="${tableNameList}" var="tableName">
                    <option value="${tableName}"<c:if test="${parent.dictTableName == tableName}">selected="selected"</c:if>>${tableName}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">字段名</label>
        <div class="layui-input-block">
            <select class="layui-select" id="dict-column-name" name="dictColumnName" <c:if test="${parent != null}">disabled</c:if> <c:if test="${parent == null}">lay-search</c:if>>
                <c:if test="${parent != null}">
                    <option value="${parent.dictColumnName}" selected="selected">${parent.dictColumnName}</option>
                </c:if>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">字典名</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="dictionaryName" lay-verify="dictionary_name">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">字典值</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="dictionaryValue" lay-verify="dictionary_value">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">排序标识</label>
        <div class="layui-input-block">
            <input type="number" class="layui-input" autocomplete="off" name="sequenceNumber" lay-verify="sequence_number">
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="dictionary_submit">立即提交</button>
        </div>
    </div>
</form>
</body>
</html>
<script>
    layui.use(['form', 'layer'], function () {
        var form = layui.form
            , layer = layui.layer;
        form.verify({
            dictionary_name: function (value) {
                if (value.length == 0) {
                    return '请输入字典名';
                }
            }
            , dictionary_value: function (value) {
                if (value.length == 0) {
                    return '请输入字典值';
                }
            }
            , sequence_number: function (value) {
                if (value.length == 0) {
                    return '请输入排序标识';
                }
            }
        });
        form.on('select(dict-table-name)',function(data){
            if (data.value != ''){
                $.post("${ctx}/dictionary/column/list",{
                    tableName: data.value
                }, function(d){
                    if(d.code == code_success){
                        var columnList = d.columnList;
                        $("#dict-column-name").empty();
                        for (var i = 0; i < columnList.length; i++) {
                            $("#dict-column-name").append('<option value="' + columnList[i] + '">' + columnList[i] + '</option>');
                        }
                        form.render('select');
                    }
                },"json");
            }
        });
        form.on('submit(dictionary_submit)', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                var index = parent.layer.getFrameIndex(window.name);
                $.post("${ctx}/dictionary/add", data.field, function (data) {
                    layer.msg(data.msg);
                    if (data.code == code_success) {
                        parent.layer.close(index);
                        parent.reloadDictionaryList(1);
                    } else {
                        submitBtn.removeClass("layui-btn-disabled");
                    }
                }, "json");
            }
            return false;
        });
    });
</script>