<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<form class="layui-form layui-form-pane" action="">
    <input type="hidden" name="dictionaryId" value="${tbDictionary.dictionaryId}">
    <div class="layui-form-item">
        <label class="layui-form-label">表名</label>
        <div class="layui-input-block">
            <select class="layui-select" disabled>
                <option value="${tbDictionary.dictTableName}"><c:out value="${tbDictionary.dictTableName}"/></option>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">字段名</label>
        <div class="layui-input-block">
            <select class="layui-select" disabled>
                <option value="${tbDictionary.dictColumnName}"><c:out value="${tbDictionary.dictColumnName}"/></option>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">字典名</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="dictionaryName" lay-verify="dictionary_name" value="<c:out value="${tbDictionary.dictionaryName}"/>">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">字典值</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="dictionaryValue" lay-verify="dictionary_value" value="<c:out value="${tbDictionary.dictionaryValue}"/>">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">排序标识</label>
        <div class="layui-input-block">
            <input type="number" class="layui-input" autocomplete="off" name="sequenceNumber" lay-verify="sequence_number" value="${tbDictionary.sequenceNumber}">
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
        form.on('submit(dictionary_submit)', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                var index = parent.layer.getFrameIndex(window.name);
                $.post("${ctx}/dictionary/update", data.field, function (data) {
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