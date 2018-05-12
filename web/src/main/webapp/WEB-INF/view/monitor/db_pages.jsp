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
        <div class="layui-input-inline">
            <input type="text" class="layui-input" id="pages_time">
        </div>
        <div class="layui-input-inline">
            <button class="layui-btn" onclick="initPages()">查询</button>
        </div>
    </div>
    <div id="pages_panel" style="height: 480px"></div>
</div>
</body>
</html>
<script>
    layui.use('laydate', function(){
        var laydate = layui.laydate;
        laydate.render({
            elem: '#pages_time'
            ,value: new Date()
            , btns: ['now', 'confirm']
        });
        initPages();
    });
    function initPages() {
        var dom = document.getElementById("pages_panel");
        var myChart = echarts.init(dom);

        $.post("${ctx}/monitor/dbstatus", {
            dataType: 'pages',
            startTime: $("#pages_time").val()
        }, function(data){
            if (data.code == code_success) {
                var dbPagesVOList = data.dbPagesVOList;
                var masterName = data.masterName;
                var slaveNames = data.slaveNames;
                var titles = [masterName + '空闲', masterName + '已缓存', masterName + '已用'];
                slaveNames.forEach(function(v) {
                    titles.push(v + "空闲");
                    titles.push(v + "已缓存");
                    titles.push(v + "已用");
                });
                var valueObject = {};
                var categoryData = [];
                for (var i = 0, length = dbPagesVOList.length; i < length; i++) {
                    var vo = dbPagesVOList[i];
                    var poolName = vo.poolName;
                    var createTime = vo.createTime;
                    var inner = {};
                    if (typeof valueObject[createTime] === 'undefined'){
                        categoryData.push(echarts.format.formatTime('yyyy-MM-dd\nhh:mm:ss', createTime));
                    } else {
                        inner = valueObject[createTime];
                    }
                    inner[poolName + 'PagesFree'] = vo.pagesFree;
                    inner[poolName + 'PagesMisc'] = vo.pagesMisc;
                    inner[poolName + 'PagesData'] = vo.pagesData;
                    valueObject[createTime] = inner;
                }
                var masterPagesFree = [];
                var masterPagesMisc = [];
                var masterPagesData = [];
                var slavePagesFree = [];
                var slavePagesMisc = [];
                var slavePagesData = [];
                for (var index in valueObject) {
                    var thisObj = valueObject[index];
                    masterPagesFree.push(thisObj[masterName + 'PagesFree']);
                    masterPagesMisc.push(thisObj[masterName + 'PagesMisc']);
                    masterPagesData.push(thisObj[masterName + 'PagesData']);
                    slaveNames.forEach(function(v) {
                        if (typeof slavePagesFree[v] === 'undefined'){
                            slavePagesFree[v] = [];
                        }
                        if (typeof slavePagesMisc[v] === 'undefined'){
                            slavePagesMisc[v] = [];
                        }
                        if (typeof slavePagesData[v] === 'undefined'){
                            slavePagesData[v] = [];
                        }
                        slavePagesFree[v].push(thisObj[v + 'PagesFree']);
                        slavePagesMisc[v].push(thisObj[v + 'PagesMisc']);
                        slavePagesData[v].push(thisObj[v + 'PagesData']);
                    });
                }
                var series = [{
                    name: masterName + '空闲',
                    stack: 'master',
                    type: 'bar',
                    data: masterPagesFree,
                    large: true
                },{
                    name: masterName + '已缓存',
                    stack: 'master',
                    type: 'bar',
                    data: masterPagesMisc,
                    large: true
                },{
                    name: masterName + '已用',
                    stack: 'master',
                    type: 'bar',
                    data: masterPagesData,
                    large: true
                }];
                slaveNames.forEach(function(v) {
                    var free = {
                        name: v + '空闲',
                        stack: v,
                        type: 'bar',
                        data: slavePagesFree[v],
                        large: true
                    };
                    series.push(free);
                    var misc = {
                        name: v + '已缓存',
                        stack: v,
                        type: 'bar',
                        data: slavePagesMisc[v],
                        large: true
                    };
                    series.push(misc);
                    var data = {
                        name: v + '已用',
                        stack: v,
                        type: 'bar',
                        data: slavePagesData[v],
                        large: true
                    };
                    series.push(data);
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
        },"json");
    }
</script>
