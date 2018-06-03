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
                            <input type="text" class="layui-input" id="qps_time">
                        </div>
                        <div class="layui-input-inline">
                            <button class="layui-btn" onclick="initQPS()">查询</button>
                        </div>
                    </div>
                    <div id="qps_panel" style="height: 480px"></div>
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
            elem: '#qps_time'
            , value: new Date()
            , btns: ['now', 'confirm']
        });
        initQPS();
    });

    function initQPS() {
        var dom = document.getElementById("qps_panel");
        var myChart = echarts.init(dom);

        $.post("${ctx}/monitor/dbstatus", {
            dataType: 'sql',
            startTime: $("#qps_time").val()
        }, function (data) {
            if (data.code == code_success) {
                var dbSqlVOList = data.dbSqlVOList;
                var masterName = data.masterName;
                var slaveNames = data.slaveNames;
                var titles = [masterName + 'Insert', masterName + 'Update', masterName + 'Delete', masterName + 'Select'];
                slaveNames.forEach(function (v) {
                    titles.push(v + "Select");
                });
                var valueObject = {};
                var categoryData = [];
                for (var i = 0, length = dbSqlVOList.length; i < length; i++) {
                    var vo = dbSqlVOList[i];
                    var poolName = vo.poolName;
                    var createTime = vo.createTime;
                    var inner = {};
                    if (typeof valueObject[createTime] === 'undefined') {
                        categoryData.push(echarts.format.formatTime('yyyy-MM-dd\nhh:mm:ss', createTime));
                    } else {
                        inner = valueObject[createTime];
                    }
                    if (masterName === poolName) {
                        inner[poolName + 'Insert'] = vo.comInsert;
                        inner[poolName + 'Update'] = vo.comUpdate;
                        inner[poolName + 'Delete'] = vo.comDelete;
                    }
                    inner[poolName + 'Select'] = vo.comSelect;
                    valueObject[createTime] = inner;
                }
                categoryData.splice(categoryData.length - 1, 1);
                var masterInsert = [];
                var masterUpdate = [];
                var masterDelete = [];
                var masterSelect = [];
                var slaveSelect = [];
                for (var index in valueObject) {
                    var thisObj = valueObject[index];
                    var nextObj = valueObject[index * 1 + 5000];
                    if (typeof nextObj === 'undefined') {
                        break;
                    }
                    masterInsert.push(nextObj[masterName + 'Insert'] - thisObj[masterName + 'Insert']);
                    masterUpdate.push(nextObj[masterName + 'Update'] - thisObj[masterName + 'Update']);
                    masterDelete.push(nextObj[masterName + 'Delete'] - thisObj[masterName + 'Delete']);
                    masterSelect.push(nextObj[masterName + 'Select'] - thisObj[masterName + 'Select']);
                    slaveNames.forEach(function (v) {
                        if (typeof slaveSelect[v] === 'undefined') {
                            slaveSelect[v] = [];
                        }
                        slaveSelect[v].push(nextObj[v + "Select"] - thisObj[v + 'Select']);
                    });
                }
                var series = [{
                    name: masterName + 'Insert',
                    stack: 'master',
                    type: 'bar',
                    data: masterInsert,
                    large: true
                }, {
                    name: masterName + 'Update',
                    stack: 'master',
                    type: 'bar',
                    data: masterUpdate,
                    large: true
                }, {
                    name: masterName + 'Delete',
                    stack: 'master',
                    type: 'bar',
                    data: masterDelete,
                    large: true
                }, {
                    name: masterName + 'Select',
                    stack: 'master',
                    type: 'bar',
                    data: masterSelect,
                    large: true
                }];
                slaveNames.forEach(function (v) {
                    var o = {
                        name: v + 'Select',
                        type: 'bar',
                        data: slaveSelect[v],
                        large: true
                    };
                    series.push(o);
                });
                var option = {
                    title: {
                        text: 'sql 统计',
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
