<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<table>
    <c:forEach items="${pageBean.data}" var="item">
        <tr><td><c:if test="${item.hasRole == 0}">
                    <button class="layui-btn layui-btn-xs grant-role-btn" role-id="${item.roleId}"><i class="fa fa-user-plus"></i>分配角色</button>
                </c:if>
                <c:if test="${item.hasRole == 1}">
                    <button class="layui-btn layui-btn-xs layui-btn-danger revoke-role-btn" role-id="${item.roleId}"><i class="fa fa-user-times"></i>取消角色</button>
                </c:if>
                </td>
            <td><c:out value="${item.roleName}"/></td>
            <td><c:out value="${item.roleLabel}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>