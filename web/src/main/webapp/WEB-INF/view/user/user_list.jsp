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
            <td>${item.userId}</td>
            <td><c:out value="${item.userName}"/></td>
            <td><c:out value="${item.cellphone}"/></td>
            <td><fmt:formatDate value="${item.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><c:out value="${item.email}"/></td>
            <td>
                <button class="layui-btn layui-btn-xs update-btn" update-id="${item.userId}"><i class="fa fa-edit"></i>编辑</button>
                <button class="layui-btn layui-btn-xs reset-pwd-btn" user-id="${item.userId}"><i class="fa fa-retweet"></i>重置密码</button>
                <button class="layui-btn layui-btn-xs grant-role-btn" user-name="<c:out value="${item.userName}"/>" user-id="${item.userId}"><i class="fa fa-user-plus"></i>分配角色</button>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>