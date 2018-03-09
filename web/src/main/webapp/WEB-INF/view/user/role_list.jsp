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
            <td><input type="checkbox" name="deleteRole" lay-skin="primary" lay-filter="delete_role" value="${item.roleId}"></td>
            <td><button class="layui-btn layui-btn-xs update-btn" update-id="${item.roleId}"><i class="fa fa-edit"></i>编辑</button>
                <button class="layui-btn layui-btn-xs grant-menu-btn" role-id="${item.roleId}"><i class="fa fa-sitemap"></i>分配菜单</button>
                <button class="layui-btn layui-btn-xs grant-request-btn" role-id="${item.roleId}"><i class="fa fa-road"></i>分配权限</button>
                <button class="layui-btn layui-btn-xs grant-user-btn" role-id="${item.roleId}"><i class="fa fa-users"></i>相关用户</button></td>
            <td><c:out value="${item.roleName}"/></td>
            <td><c:out value="${item.roleLabel}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>