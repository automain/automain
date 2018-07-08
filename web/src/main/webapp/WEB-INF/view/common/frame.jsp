<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="common.jsp" %>
    <link rel="stylesheet" href="${ctx}/static/css/common/admin.css?v=${staticVersion}" media="all"/>
    <title>CMS</title>
</head>
<body class="layui-layout-body">
<div id="LAY_app">
    <div class="layui-layout layui-layout-admin">
        <div class="layui-header">
            <!-- 头部区域 -->
            <ul class="layui-nav layui-layout-left">
                <li class="layui-nav-item layadmin-flexible" lay-unselect>
                    <a href="javascript:;" layadmin-event="flexible" title="侧边伸缩">
                        <i class="layui-icon layui-icon-shrink-right" id="LAY_app_flexible"></i>
                    </a>
                </li>
                <li class="layui-nav-item" lay-unselect>
                    <a href="javascript:;" layadmin-event="refresh" title="刷新">
                        <i class="layui-icon layui-icon-refresh-3"></i>
                    </a>
                </li>
            </ul>
            <ul class="layui-nav layui-layout-right" lay-filter="layadmin-layout-right">
                <li class="layui-nav-item" lay-unselect>
                    <a href="javascript:;">
                        <c:if test="${imgPath != null}">
                            <img src="${imgPath}" class="layui-nav-img">
                        </c:if>
                        <cite><c:out value="${userName}"/></cite>
                    </a>
                    <dl class="layui-nav-child">
                        <dd><a lay-href="${ctx}/user/forward?forwardType=info"><i class="layui-menu-icon fa fa-user-circle"></i>基本资料</a></dd>
                        <dd><a lay-href="${ctx}/user/forward?forwardType=pwd"><i class="layui-menu-icon fa fa-key"></i>修改密码</a></dd>
                    </dl>
                </li>
                <li class="layui-nav-item" lay-unselect>
                    <a href="javascript:;" onclick="logout()"><i class="fa fa-power-off"></i>退出</a>
                </li>
            </ul>
        </div>
        <!-- 侧边菜单 -->
        <div class="layui-side layui-side-menu">
            <div class="layui-side-scroll">
                <div class="layui-logo">
                    <span>CMS</span>
                </div>
                <ul class="layui-nav layui-nav-tree" lay-shrink="all" id="LAY-system-side-menu" lay-filter="layadmin-system-side-menu">
                </ul>
            </div>
        </div>
        <!-- 页面标签 -->
        <div class="layadmin-pagetabs" id="LAY_app_tabs">
            <div class="layui-icon layadmin-tabs-control layui-icon-prev" layadmin-event="leftPage"></div>
            <div class="layui-icon layadmin-tabs-control layui-icon-next" layadmin-event="rightPage"></div>
            <div class="layui-icon layadmin-tabs-control layui-icon-down">
                <ul class="layui-nav layadmin-tabs-select" lay-filter="layadmin-pagetabs-nav">
                    <li class="layui-nav-item" lay-unselect>
                        <a href="javascript:;"></a>
                        <dl class="layui-nav-child layui-anim-fadein">
                            <dd layadmin-event="closeThisTabs"><a href="javascript:;">关闭当前标签页</a></dd>
                            <dd layadmin-event="closeOtherTabs"><a href="javascript:;">关闭其它标签页</a></dd>
                            <dd layadmin-event="closeAllTabs"><a href="javascript:;">关闭全部标签页</a></dd>
                        </dl>
                    </li>
                </ul>
            </div>
            <div class="layui-tab" lay-unauto lay-allowClose="true" lay-filter="layadmin-layout-tabs">
                <ul class="layui-tab-title" id="LAY_app_tabsheader">
                    <li lay-id="${ctx}/home" lay-attr="${ctx}/home" class="layui-this"><i class="layui-icon layui-icon-home"></i></li>
                </ul>
            </div>
        </div>
        <!-- 主体内容 -->
        <div class="layui-body" id="LAY_app_body">
            <div class="layadmin-tabsbody-item layui-show">
                <iframe src="${ctx}/home" frameborder="0" class="layadmin-iframe"></iframe>
            </div>
        </div>
        <!-- 辅助元素，一般用于移动设备下遮罩 -->
        <div class="layadmin-body-shade" layadmin-event="shade"></div>
    </div>
</div>
</body>
</html>
<script type="text/javascript">
    var element, layer, admin;
    layui.extend({
        admin: '${ctx}/static/js/common/admin.js?v=${staticVersion}'
    }).use('admin', function () {
        element = layui.element
            , layer = layui.layer
            , admin = layui.admin;
        var tree = $.parseJSON('${menuVOList}');
        var html = parseTree(tree, false);
        $("#LAY-system-side-menu").html(html);
        element.on('tab(layadmin-layout-tabs)', function (data) {
            $(".layui-tab-title li").on("selectstart", function (e) {
                return false;
            });
        });
        element.render('nav');
    });

    function parseTree(tree, isChild) {
        var html = '';
        var tag = 'li';
        var tagClass = ' class="layui-nav-item"';
        if (isChild) {
            tag = 'dd';
            tagClass = '';
            html += '<dl class="layui-nav-child">';
        }
        for (var i = 0; i < tree.length; i++) {
            var node = tree[i];
            var icon = 'envira';
            if (node.icon) {
                icon = node.icon;
            }
            var link = node.link;
            var layHref = 'href="javascript:;" ';
            if (link) {
                layHref = 'lay-href="${ctx}' + link + '" ';
            }
            if (node.isSpread === 1) {
                if (isChild) {
                    tagClass = ' class="layui-nav-itemed"';
                } else {
                    tagClass = ' class="layui-nav-item layui-nav-itemed"';
                }
            }
            var tip = '';
            if (!isChild) {
                tip = 'lay-tips="' + node.name + '" lay-direction="2"';
            }
            html += '<' + tag + tagClass + '><a ' + layHref + tip + '>'
                + '<i class="layui-menu-icon fa fa-' + icon + '"></i><cite>' + node.name + '</cite></a>';
            if (node.children) {
                html += parseTree(node.children, true);
            }
            html += '</' + tag + '>';
        }
        if (isChild) {
            html += '</dl>';
        }
        return html;
    }

    function logout() {
        layer.confirm('确认退出登录?', {icon: 1, title: '确认退出'}, function (index) {
            $.post('${ctx}/user/logout/action', {}, function (data) {
                if (data.code == code_success) {
                    layer.msg(data.msg, {icon: 1, time: 300}, function () {
                        location.href = '${ctx}/';
                    });
                }
            });
            layer.close(index);
        });
    }
</script>
