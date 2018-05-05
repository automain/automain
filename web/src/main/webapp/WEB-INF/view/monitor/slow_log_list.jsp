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
            <td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><c:out value="${item.poolName}"/></td>
            <td><c:out value="${item.slowDb}"/></td>
            <td><c:out value="${item.slowState}"/></td>
            <td><c:out value="${item.slowTime}"/>s</td>
            <td><c:out value="${item.slowSql}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>