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
            <td>${item.menuId}</td>
            <td><c:out value="${item.requestUrl}"/></td>
            <td><c:out value="${item.menuName}"/></td>
            <td><i style="padding-top: 7px;" class="fa fa-<c:out value="${item.menuIcon}"/>"></i></td>
            <td><c:out value="${item.sequenceNumber}"/></td>
            <td><c:out value="${item.parentName}"/></td>
            <td><c:if test="${item.isSpread == 0}">否</c:if><c:if test="${item.isSpread == 1}">是</c:if></td>
            <td><c:if test="${item.isLeaf == 0}">否</c:if><c:if test="${item.isLeaf == 1}">是</c:if></td>
            <td>
                <button class="layui-btn layui-btn-xs update-btn" update-id="${item.menuId}"><i class="fa fa-edit"></i>编辑</button>
                <button class="layui-btn layui-btn-xs child-btn" parent-id="${item.menuId}"><i class="fa fa-level-down"></i>查看下级</button>
                <button class="layui-btn layui-btn-xs grant-role-btn" menu-name="<c:out value="${item.menuName}"/>" menu-id="${item.menuId}"><i class="fa fa-user-plus"></i>分配角色</button>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>