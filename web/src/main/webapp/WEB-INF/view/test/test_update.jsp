<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<form class="layui-form layui-form-pane" action="">
    <input type="hidden" name="testId" value="${tbTest.testId}">
    <div class="layui-form-item">
        <label class="layui-form-label">测试名称</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="testName" lay-verify="test_name" value="<c:out value='${tbTest.testName}'/>">
        </div>
    </div>
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">测试textarea</label>
        <div class="layui-input-block">
            <textarea class="layui-textarea" name="testArea" lay-verify="test_area"><c:out value="${tbTest.testArea}"/></textarea>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">创建时间</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" id="createTime" name="createTime" lay-verify="create_time"  value="<fmt:formatDate value='${tbTest.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">测试数量</label>
        <div class="layui-input-block">
            <input type="number" class="layui-input" autocomplete="off" name="testAmount" lay-verify="test_amount" value="${tbTest.testAmount}">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">测试类型(1:普通,2文艺)</label>
        <div class="layui-input-block">
            <select name="testType">
                <c:forEach items="${testTypeVOList}" var="item">
                    <option value="${item.dictionaryValue}" <c:if test="${tbTest.testType == item.dictionaryValue}">selected="selected"</c:if>>${item.dictionaryName}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="test_submit">立即提交</button>
        </div>
    </div>
</form>
</body>
</html>
<script>
    layui.use(['form', 'layer', 'laydate'], function () {
        var form = layui.form
            , layer = layui.layer
            , laydate = layui.laydate;
        form.verify({
            test_name: function (value) {
                if (value.length == 0) {
                    return '请输入测试名称';
                }
            }
            , test_area: function (value) {
                if (value.length == 0) {
                    return '请输入测试textarea';
                }
            }
            , create_time: function (value) {
                if (value.length == 0) {
                    return '请输入创建时间';
                }
            }
            , test_amount: function (value) {
                if (value.length == 0) {
                    return '请输入测试数量';
                }
            }
        });
        laydate.render({
            elem: '#createTime'
            , type: 'datetime'
            , value: '<fmt:formatDate value="${tbTest.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>'
        });
        form.on('submit(test_submit)', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                var index = parent.layer.getFrameIndex(window.name);
                $.post("${ctx}/test/update", data.field, function (data) {
                    layer.msg(data.msg);
                    if (data.code == code_success) {
                        parent.layer.close(index);
                        parent.reloadTestList(1);
                    } else {
                        submitBtn.removeClass("layui-btn-disabled");
                    }
                }, "json");
            }
            return false;
        });
    });
</script>