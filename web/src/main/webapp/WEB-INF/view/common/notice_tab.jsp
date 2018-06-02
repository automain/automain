<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="common.jsp" %>
    <title></title>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row">
        <div class="layui-col-md12">
            <div class="layui-card">
                <blockquote class="layui-elem-quote">
                    <div class="layui-form">
                        <div class="layui-inline">
                            <button class="layui-btn layui-btn-sm layui-btn-normal" id="notice_add">
                                <i class="fa fa-plus"></i> 添加
                            </button>
                        </div>
                        <div class="layui-inline">
                            <button class="layui-btn layui-btn-sm layui-btn-warm" id="notice_refresh">
                                <i class="fa fa-refresh"></i> 刷新
                            </button>
                        </div>
                        <div class="layui-inline">
                            <button class="layui-btn layui-btn-sm layui-btn-danger" id="notice_delete">
                                <i class="fa fa-remove"></i> 清除
                            </button>
                        </div>
                    </div>
                </blockquote>
                <div class="layui-form">
                    <table class="layui-table">
                        <thead>
                        <tr>
                            <th style="min-width: 50px;width: 50px;">操作</th>
                            <th style="min-width: 150px;">升级开始时间</th>
                            <th style="min-width: 150px;">升级结束时间</th>
                            <th style="min-width: 150px;">公告标题</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${noticeMap != null}">
                            <tr>
                                <td>
                                    <button class="layui-btn layui-btn-xs detail-btn"><i class="fa fa-edit"></i>详情</button>
                                </td>
                                <td>${noticeMap['noticeStartTime']}</td>
                                <td>${noticeMap['noticeEndTime']}</td>
                                <td><c:out value="${noticeMap['noticeTitle']}"/></td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<script>
    var form, laypage, layer;
    layui.use(['form', 'layer', 'laypage'], function () {
        form = layui.form;
        layer = layui.layer;
        laypage = layui.laypage;
        $("#notice_add").click(function () {
            alertByFull(layer, "添加", "${ctx}/notice/forward?forwardType=add");
        });
        $("#notice_refresh").click(function () {
            reloadNoticeList();
        });
        $("#notice_delete").click(function () {
            $.post("${ctx}/notice/delete", {}, function (data) {
                if (data.code == code_success) {
                    layer.msg(data.msg);
                    reloadNoticeList();
                }
            }, "json");
        });
        $(".detail-btn").click(function () {
            var noticeStartTime = '${noticeMap['noticeStartTime']}';
            var noticeEndTime = '${noticeMap['noticeEndTime']}';
            var noticeTitle = '<c:out value="${noticeMap['noticeTitle']}"/>';
            var noticeContent = '${fn:replace(noticeMap['noticeContent'],vEnter, '<br/>')}';
            alertNotice(layer, noticeStartTime, noticeEndTime, noticeTitle, noticeContent);
        });
    });

    function reloadNoticeList() {
        setTimeout(function () {
            window.location.reload();
        }, loadingTime);
    }
</script>