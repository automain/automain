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
            <td>${item.dictionaryId}</td>
            <td><c:out value="${item.dictTableName}"/></td>
            <td><c:out value="${item.dictColumnName}"/></td>
            <td><c:out value="${item.dictionaryName}"/></td>
            <td><c:out value="${item.dictionaryValue}"/></td>
            <td><c:out value="${item.sequenceNumber}"/></td>
            <td><c:out value="${item.parentName}"/></td>
            <td><c:if test="${item.isLeaf == 0}">否</c:if><c:if test="${item.isLeaf == 1}">是</c:if></td>
            <td>
                <button class="layui-btn layui-btn-xs update-btn" update-id="${item.dictionaryId}"><i class="fa fa-edit"></i>编辑</button>
                <button class="layui-btn layui-btn-xs child-btn" parent-id="${item.dictionaryId}"><i class="fa fa-level-down"></i>查看下级</button>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>