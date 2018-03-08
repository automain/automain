<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<form class="layui-form layui-form-pane" >
    <div class="layui-form-item">
        <label class="layui-form-label">测试名称</label>
        <div class="layui-input-block">
            <input type="text" disabled="disabled" class="layui-input" value="<c:out value='${tbTest.testName}'/>">
        </div>
    </div>
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">测试textarea</label>
        <div class="layui-input-block">
            <textarea class="layui-textarea" disabled="disabled"><c:out value="${tbTest.testArea}"/></textarea>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">创建时间</label>
        <div class="layui-input-block">
            <input type="text" disabled="disabled" class="layui-input" value="<fmt:formatDate value='${tbTest.createTime}' pattern='yyyy-MM-dd HH:mm:ss'/>">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">测试数量</label>
        <div class="layui-input-block">
            <input type="number" disabled="disabled" class="layui-input" value="${tbTest.testAmount}">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">测试类型(1:普通,2文艺)</label>
        <div class="layui-input-block">
            <c:forEach items="${testTypeVOList}" var="item">
                <c:if test="${tbTest.testType == item.dictionaryValue}"><input type="text" disabled="disabled" class="layui-input" value="${item.dictionaryName}"/></c:if>
            </c:forEach>
        </div>
    </div>
</form>
</body>
</html>
