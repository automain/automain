<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<form class="layui-form layui-form-pane" action="">
    <div class="layui-form-item">
        <label class="layui-form-label">升级开始时间</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" id="noticeStartTime" name="noticeStartTime" lay-verify="notice_start_time">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">升级结束时间</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" id="noticeEndTime" name="noticeEndTime" lay-verify="notice_end_time">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">公告标题</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="noticeTitle" lay-verify="notice_title">
        </div>
    </div>
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">公告内容</label>
        <div class="layui-input-block">
            <textarea class="layui-textarea" name="noticeContent" lay-verify="notice_content"></textarea>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="notice_submit">立即提交</button>
        </div>
    </div>
</form>
</body>
</html>
<script>
    layui.use(['form', 'layer', 'laydate'], function () {
        var form = layui.form
            , layer = layui.layer
            , laydate = layui.laydate;
        form.verify({
            notice_start_time: function (value) {
                if (value.length == 0) {
                    return '请输入升级开始时间';
                }
            }
            , notice_end_time: function (value) {
                if (value.length == 0) {
                    return '请输入升级结束时间';
                }
            }
            , notice_title: function (value) {
                if (value.length == 0) {
                    return '请输入公告标题';
                }
            }
            , notice_content: function (value) {
                if (value.length == 0) {
                    return '请输入公告内容';
                }
            }
        });
        laydate.render({
            elem: '#noticeStartTime'
            , type: 'datetime'
        });
        laydate.render({
            elem: '#noticeEndTime'
            , type: 'datetime'
        });
        form.on('submit(notice_submit)', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                var index = parent.layer.getFrameIndex(window.name);
                $.post("${ctx}/notice/add", data.field, function (data) {
                    layer.msg(data.msg);
                    if (data.code == code_success) {
                        parent.layer.close(index);
                        parent.reloadNoticeList();
                    } else {
                        submitBtn.removeClass("layui-btn-disabled");
                    }
                }, "json");
            }
            return false;
        });
    });
</script>