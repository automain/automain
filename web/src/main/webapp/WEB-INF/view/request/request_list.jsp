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
            <td><button class="layui-btn layui-btn-xs update-btn" update-id="${item.requestMappingId}"><i class="fa fa-edit"></i>编辑</button>
                <button class="layui-btn layui-btn-xs grant-role-btn" request-url="<c:out value="${item.requestUrl}"/>" request-id="${item.requestMappingId}"><i class="fa fa-user-plus"></i>分配角色</button></td>
            <td><c:out value="${item.requestUrl}"/></td>
            <td><c:out value="${item.operationClass}"/></td>
            <td><c:out value="${item.urlComment}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>