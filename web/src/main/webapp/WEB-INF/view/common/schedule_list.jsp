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
            <td><c:out value="${item.scheduleName}"/></td>
            <td><c:out value="${item.scheduleUrl}"/></td>
            <td><fmt:formatDate value="${item.startExecuteTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><c:out value="${item.delayTime}"/></td>
            <td><fmt:formatDate value="${item.lastExecuteTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><fmt:formatDate value="${item.updateTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><input type="checkbox" name="change" value="${item.scheduleId}" lay-skin="switch" lay-text="开启|关闭" lay-filter="change" <c:if test="${item.isDelete == 0}">checked="checked"</c:if>></td>
            <td>
                <button class="layui-btn layui-btn-xs update-btn" update-id="${item.scheduleId}"><i class="fa fa-edit"></i>编辑</button>
                <button class="layui-btn layui-btn-xs schedule-btn" schedule-url="${item.scheduleUrl}"><i class="fa fa-flash"></i>手动执行</button>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>