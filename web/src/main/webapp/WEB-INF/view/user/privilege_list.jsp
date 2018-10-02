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
            <td>${item.privilegeId}</td>
            <td><c:out value="${item.privilegeLabel}"/></td>
            <td><c:out value="${item.privilegeName}"/></td>
            <td>
                <button class="layui-btn layui-btn-xs update-btn" update-id="${item.privilegeId}"><i class="fa fa-edit"></i>编辑</button>
                <button class="layui-btn layui-btn-xs child-btn" parent-id="${item.privilegeId}"><i class="fa fa-level-down"></i>查看下级</button>
                <button class="layui-btn layui-btn-xs grant-role-btn" privilege-name="<c:out value="${item.privilegeName}"/>" privilege-id="${item.privilegeId}"><i class="fa fa-user-plus"></i>分配角色</button>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>