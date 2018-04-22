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
            <td>${item.configId}</td>
            <td><c:out value="${item.configKey}"/></td>
            <td><c:out value="${item.configValue}"/></td>
            <td><c:out value="${item.configComment}"/></td>
            <td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><fmt:formatDate value="${item.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><button class="layui-btn layui-btn-xs update-btn" update-id="${item.configId}"><i class="fa fa-edit"></i>编辑</button>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>