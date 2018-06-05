<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <script src="${ctx}/static/js/plugin/echarts.min.js?v=${staticVersion}"></script>
    <title></title>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row">
        <div class="layui-col-md12">
            <div class="layui-card">
                <div class="layui-container">
                    <div class="layui-form-item">
                        <label class="layui-form-label">时间:</label>
                        <div class="layui-input-inline">
                            <input type="text" class="layui-input" autocomplete="off" id="tps_time">
                        </div>
                        <div class="layui-input-inline">
                            <button class="layui-btn" onclick="initTPS()">查询</button>
                        </div>
                    </div>
                    <div id="tps_panel" style="height: 480px"></div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<script>
    layui.use('laydate', function () {
        var laydate = layui.laydate;
        laydate.render({
            elem: '#tps_time'
            , value: new Date()
            , btns: ['now', 'confirm']
        });
        initTPS();
    });

    function initTPS() {
        var dom = document.getElementById("tps_panel");
        var myChart = echarts.init(dom);

        $.post("${ctx}/monitor/dbstatus", {
            dataType: 'transaction',
            startTime: $("#tps_time").val()
        }, function (data) {
            if (data.code == code_success) {
                var dbTransactionVOList = data.dbTransactionVOList;
                var valueData = [];
                var categoryData = [];
                for (var i = 0, length = dbTransactionVOList.length; i < length; i++) {
                    var vo = dbTransactionVOList[i];
                    var createTime = vo.createTime;
                    categoryData.push(echarts.format.formatTime('yyyy-MM-dd\nhh:mm:ss', createTime));
                    valueData.push(vo.transaction);
                }
                var option = {
                    title: {
                        text: 'transaction 统计',
                        left: 10
                    },
                    toolbox: {
                        feature: {
                            dataZoom: {
                                yAxisIndex: false
                            },
                            saveAsImage: {
                                pixelRatio: 2
                            }
                        }
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    grid: {
                        bottom: 90
                    },
                    dataZoom: [{
                        type: 'inside'
                    }, {
                        type: 'slider'
                    }],
                    xAxis: {
                        data: categoryData,
                        silent: false,
                        splitLine: {
                            show: false
                        },
                        splitArea: {
                            show: false
                        }
                    },
                    yAxis: {
                        splitArea: {
                            show: false
                        }
                    },
                    series: [{
                        type: 'bar',
                        data: valueData,
                        large: true
                    }]
                };
                if (option && typeof option === "object") {
                    myChart.setOption(option, true);
                }
            }
        }, "json");
    }
</script>
