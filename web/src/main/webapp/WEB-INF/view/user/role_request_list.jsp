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
            <td><c:out value="${item.requestUrl}"/></td>
            <td><c:if test="${item.hasRole == 0}">
                <button class="layui-btn layui-btn-xs grant-request-btn" request-url="${item.requestUrl}"><i class="fa fa-mail-forward"></i>分配权限</button>
                </c:if>
                <c:if test="${item.hasRole == 1}">
                    <button class="layui-btn layui-btn-xs layui-btn-danger revoke-request-btn" request-url="${item.requestUrl}"><i class="fa fa-mail-reply"></i>取消权限</button>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>