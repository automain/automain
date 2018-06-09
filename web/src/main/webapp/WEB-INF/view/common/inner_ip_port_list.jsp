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
            <td>${item.innerId}</td>
            <td><c:out value="${item.ip}"/></td>
            <td><c:out value="${item.port}"/></td>
            <td>
                <button class="layui-btn layui-btn-xs update-btn" update-id="${item.innerId}"><i class="fa fa-edit"></i>编辑</button>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>