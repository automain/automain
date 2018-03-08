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
            <td><input type="checkbox" name="deleteMenu" lay-skin="primary" lay-filter="delete_menu" value="${item.menuId}"></td>
            <td><button class="layui-btn layui-btn-xs update-btn" update-id="${item.menuId}"><i class="fa fa-edit"></i>编辑</button>
                <button class="layui-btn layui-btn-xs child-btn" parent-id="${item.menuId}"><i class="fa fa-level-down"></i>查看下级</button>
                <button class="layui-btn layui-btn-xs grant-role-btn" menu-name="<c:out value="${item.menuName}"/>" menu-id="${item.menuId}"><i class="fa fa-user-plus"></i>分配角色</button></td>
            <td><c:out value="${item.requestUrl}"/></td>
            <td><c:out value="${item.menuName}"/></td>
            <td><i class="fa fa-<c:out value="${item.menuIcon}"/>"></i></td>
            <td><c:out value="${item.sequenceNumber}"/></td>
            <td><c:out value="${item.parentName}"/></td>
            <td><c:if test="${item.isLeaf == 0}">否</c:if><c:if test="${item.isLeaf == 1}">是</c:if></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>