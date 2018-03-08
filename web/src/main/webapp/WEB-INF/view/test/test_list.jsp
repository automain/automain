<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<table>
    <c:forEach items="${pageBean.data}" var="item">
        <tr>
            <td><input type="checkbox" name="deleteTest" lay-skin="primary" lay-filter="delete_test" value="${item.testId}"></td>
            <td><button class="layui-btn layui-btn-xs update-btn" update-id="${item.testId}"><i class="fa fa-edit"></i>编辑</button>
                <button class="layui-btn layui-btn-xs detail-btn" detail-id="${item.testId}"><i class="fa fa-list"></i>详情</button></td>
            <td><c:out value="${item.testName}"/></td>
            <td><c:out value="${item.testArea}"/></td>
            <td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><fmt:formatNumber type="number" value="${item.testAmount}" pattern="0.00" maxFractionDigits="2"/></td>
            <td>${testTypeMap[fn:trim(item.testType)]}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>