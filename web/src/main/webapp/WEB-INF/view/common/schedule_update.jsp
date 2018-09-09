<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <title></title>
</head>
<body>
<form class="layui-form layui-form-pane" action="">
    <input type="hidden" name="scheduleId" value="${bean.scheduleId}">
    <div class="layui-form-item">
        <label class="layui-form-label">任务名称</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" autocomplete="off" name="scheduleName" lay-verify="schedule_name" value="<c:out value='${bean.scheduleName}'/>">
        </div>
    </div>
    <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">任务请求url</label>
        <div class="layui-input-block">
            <textarea class="layui-textarea" name="scheduleUrl" lay-verify="schedule_url"><c:out value="${bean.scheduleUrl}"/></textarea>
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">开始执行时间</label>
        <div class="layui-input-block">
            <input type="text" class="layui-input" id="startExecuteTime" name="startExecuteTime" lay-verify="start_execute_time"  value="<fmt:formatDate value='${bean.startExecuteTime}' pattern='yyyy-MM-dd HH:mm:ss'/>">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">间隔时间长度(秒)</label>
        <div class="layui-input-block">
            <input type="number" class="layui-input" autocomplete="off" name="delayTime" lay-verify="delay_time" value="${bean.delayTime}">
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="schedule_submit">立即提交</button>
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
            schedule_name: function (value) {
                if (value.length == 0) {
                    return '请输入任务名称';
                }
            }
            , schedule_url: function (value) {
                if (value.length == 0) {
                    return '请输入任务请求url';
                }
            }
            , start_execute_time: function (value) {
                if (value.length == 0) {
                    return '请输入开始执行时间';
                }
            }
            , delay_time: function (value) {
                if (value.length == 0) {
                    return '请输入间隔时间长度';
                }
            }
        });
        laydate.render({
            elem: '#startExecuteTime'
            , type: 'datetime'
            , value: '<fmt:formatDate value="${tbSchedule.startExecuteTime}" pattern="yyyy-MM-dd HH:mm:ss"/>'
        });
        form.on('submit(schedule_submit)', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                var index = parent.layer.getFrameIndex(window.name);
                $.post("${ctx}/schedule/update", data.field, function (data) {
                    layer.msg(data.msg);
                    if (data.code == code_success) {
                        parent.layer.close(index);
                        parent.reloadScheduleList(1);
                    } else {
                        submitBtn.removeClass("layui-btn-disabled");
                    }
                }, "json");
            }
            return false;
        });
    });
</script>