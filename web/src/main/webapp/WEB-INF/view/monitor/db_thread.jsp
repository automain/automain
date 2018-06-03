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
                            <input type="text" class="layui-input" id="thread_time">
                        </div>
                        <div class="layui-input-inline">
                            <button class="layui-btn" onclick="initThread()">查询</button>
                        </div>
                    </div>
                    <div id="thread_panel" style="height: 480px"></div>
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
            elem: '#thread_time'
            , value: new Date()
            , btns: ['now', 'confirm']
        });
        initThread();
    });

    function initThread() {
        var dom = document.getElementById("thread_panel");
        var myChart = echarts.init(dom);

        $.post("${ctx}/monitor/dbstatus", {
            dataType: 'thread',
            startTime: $("#thread_time").val()
        }, function (data) {
            if (data.code == code_success) {
                var dbThreadVOList = data.dbThreadVOList;
                var masterName = data.masterName;
                var slaveNames = data.slaveNames;
                var titles = [masterName + '空闲', masterName + '运行中'];
                slaveNames.forEach(function (v) {
                    titles.push(v + "空闲");
                    titles.push(v + "运行中");
                });
                var valueObject = {};
                var categoryData = [];
                for (var i = 0, length = dbThreadVOList.length; i < length; i++) {
                    var vo = dbThreadVOList[i];
                    var poolName = vo.poolName;
                    var createTime = vo.createTime;
                    var inner = {};
                    if (typeof valueObject[createTime] === 'undefined') {
                        categoryData.push(echarts.format.formatTime('yyyy-MM-dd\nhh:mm:ss', createTime));
                    } else {
                        inner = valueObject[createTime];
                    }
                    inner[poolName + 'ThreadsFree'] = vo.threadsFree;
                    inner[poolName + 'ThreadsRunning'] = vo.threadsRunning;
                    valueObject[createTime] = inner;
                }
                var masterThreadsFree = [];
                var masterThreadsRunning = [];
                var slaveThreadsFree = [];
                var slaveThreadsRunning = [];
                for (var index in valueObject) {
                    var thisObj = valueObject[index];
                    masterThreadsFree.push(thisObj[masterName + 'ThreadsFree']);
                    masterThreadsRunning.push(thisObj[masterName + 'ThreadsRunning']);
                    slaveNames.forEach(function (v) {
                        if (typeof slaveThreadsFree[v] === 'undefined') {
                            slaveThreadsFree[v] = [];
                        }
                        if (typeof slaveThreadsRunning[v] === 'undefined') {
                            slaveThreadsRunning[v] = [];
                        }
                        slaveThreadsFree[v].push(thisObj[v + 'ThreadsFree']);
                        slaveThreadsRunning[v].push(thisObj[v + 'ThreadsRunning']);
                    });
                }
                var series = [{
                    name: masterName + '空闲',
                    stack: 'master',
                    type: 'bar',
                    data: masterThreadsFree,
                    large: true
                }, {
                    name: masterName + '运行中',
                    stack: 'master',
                    type: 'bar',
                    data: masterThreadsRunning,
                    large: true
                }];
                slaveNames.forEach(function (v) {
                    var free = {
                        name: v + '空闲',
                        stack: v,
                        type: 'bar',
                        data: slaveThreadsFree[v],
                        large: true
                    };
                    series.push(free);
                    var running = {
                        name: v + '运行中',
                        stack: v,
                        type: 'bar',
                        data: slaveThreadsRunning[v],
                        large: true
                    };
                    series.push(running);
                });
                var option = {
                    title: {
                        text: '线程统计',
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
                    legend: {
                        data: titles
                    },
                    series: series
                };
                if (option && typeof option === "object") {
                    myChart.setOption(option, true);
                }
            }
        }, "json");
    }
</script>
