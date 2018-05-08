<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/common.jsp" %>
    <script src="${ctx}/static/js/plugin/echarts.min.js?v=${staticVersion}"></script>
    <title></title>
</head>
<body>
<div class="layui-container">
    <div class="layui-form-item">
        <label class="layui-form-label">时间:</label>
        <div class="layui-inline">
            <input type="text" class="layui-input" id="qps_time">
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" id="qps_search">查询</button>
        </div>
    </div>
    <div id="qps_panel" style="height: 480px"></div>
</div>
</body>
</html>
<script>
    layui.use('laydate', function(){
        var laydate = layui.laydate;
        laydate.render({
            elem: '#qps_time'
            ,value: new Date()
            , btns: ['now', 'confirm']
        });
        initQPS();
    });
    function initQPS() {
        var dom = document.getElementById("qps_panel");
        var myChart = echarts.init(dom);

        var sqldata;
        $.post("${ctx}/monitor/dbsql", {
            startTime: $("#qps_time").val()
        }, function(data){
            if (data.code == code_success) {
                var dbSqlVOList = data.dbSqlVOList;
                var length = dbSqlVOList.length;
                var timex = {};
                var categoryData = [];
                var valueData = [];
                for (var i = 0; i < length; i++) {
                    var vo = dbSqlVOList[i];
                    var poolName = vo.poolName;
                    var createTime = vo.createTime;
                    if (typeof timex[createTime] === 'undefined'){
                        categoryData.push(echarts.format.formatTime('yyyy-MM-dd\nhh:mm:ss', createTime));
                        timex[createTime] = true;
                    }
                }
            }
        },"json");
        var dataCount = 1440;
        var data = generateData(dataCount);

        var option = {
            title: {
                text: echarts.format.addCommas(dataCount) + ' Data',
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
                data: data.categoryData,
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
                data:['masterInsert', 'masterUpdate', 'masterDelete', 'masterSelect', 'slave1Select']
            },
            series: [{
                name: 'masterInsert',
                stack: 'master',
                type: 'bar',
                data: data.valueData,
                large: true
            },{
                name: 'masterUpdate',
                stack: 'master',
                type: 'bar',
                data: data.valueData,
                large: true
            },{
                name: 'masterDelete',
                stack: 'master',
                type: 'bar',
                data: data.valueData,
                large: true
            },{
                name: 'masterSelect',
                stack: 'master',
                type: 'bar',
                data: data.valueData,
                large: true
            },{
                name: 'slave1Select',
                type: 'bar',
                data: data.valueData,
                large: true
            }]
        };
        if (option && typeof option === "object") {
            myChart.setOption(option, true);
        }
    }
    function generateData(count) {
        var baseValue = Math.random() * 1000;
        var time = +new Date(2011, 0, 1);
        var smallBaseValue;

        function next(idx) {
            smallBaseValue = idx % 30 === 0
                ? Math.random() * 700
                : (smallBaseValue + Math.random() * 500 - 250);
            baseValue += Math.random() * 20 - 10;
            return Math.max(
                0,
                Math.round(baseValue + smallBaseValue) + 3000
            );
        }

        var categoryData = [];
        var valueData = [];

        for (var i = 0; i < count; i++) {
            categoryData.push(echarts.format.formatTime('yyyy-MM-dd\nhh:mm:ss', time));
            valueData.push(next(i).toFixed(2));
            time += 10000;
        }

        return {
            categoryData: categoryData,
            valueData: valueData
        };
    }
</script>
