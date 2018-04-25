<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="common.jsp" %>
    <link rel="stylesheet" href="${ctx}/static/css/common/frame.css?v=${staticVersion}" media="all"/>
    <title>CMS</title>
</head>
<body class="layui-layout-body">
<!-- 布局容器 -->
<div class="layui-layout layui-layout-admin">
    <!-- 头部 -->
    <div class="layui-header">
        <!-- logo -->
        <div class="layui-logo">automain</div>
        <!-- 隐藏按钮 -->
        <div class="layui-menu-hide-btn" onselectstart="return false;" onclick="toggleSide()">
            <i class="fa fa-bars"></i>
        </div>
        <!-- 水平导航左 -->
        <ul class="layui-nav layui-layout-left">
            <c:forEach items="${titleList}" var="title" varStatus="a">
                <li class="layui-nav-item <c:if test="${a.index == 0}">layui-this</c:if>">
                    <a href="javascript:;" onclick="showChildMenu('${title.id}')">
                        <i class="fa fa-${title.icon}"></i><c:out value="${title.name}"/>
                    </a>
                </li>
            </c:forEach>
        </ul>
        <!-- 水平导航右 -->
        <ul class="layui-nav layui-layout-right">
            <li class="layui-nav-item" lay-unselect><a href="javascript:;" onclick="closeOthers()"><i
                    class="fa fa-close"></i>关闭其他</a></li>
            <li class="layui-nav-item">
                <a href="javascript:;">
                    <c:if test="${imgPath != null}">
                        <img src="${imgPath}" class="layui-nav-img">
                    </c:if>
                    <c:out value="${userName}"/>&nbsp;
                </a>
                <dl class="layui-nav-child">
                    <dd><a href="javascript:;" onclick="addTab('-1', '基本资料', 'user-circle', '/user/forward?forwardType=info')">基本资料</a></dd>
                    <dd><a href="javascript:;" onclick="addTab('-2', '修改密码', 'key', '/user/forward?forwardType=pwd')">修改密码</a></dd>
                </dl>
            </li>
            <li class="layui-nav-item"><a href="javascript:;" onclick="logout()"><i class="fa fa-power-off"></i>退出</a>
            </li>
        </ul>
    </div>

    <!-- 侧边栏 -->
    <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
            <c:forEach items="${childrenList}" var="child" varStatus="a">
                <ul class="layui-nav layui-nav-tree" id="menu-${child.parentId}"
                    <c:if test="${a.index > 0}">style="display: none;"</c:if> ></ul>
            </c:forEach>
        </div>
    </div>

    <!-- 主体 -->
    <div class="layui-body">
        <!-- 顶部切换卡 -->
        <div class="layui-tab" style="margin-top: 0px;" lay-allowClose="true" lay-filter="body-tab">
            <ul class="layui-tab-title">
                <li class="layui-this" lay-id="0"><i class="layui-menu-title fa fa-home"></i>主页</li>
            </ul>
            <div class="layui-tab-content">
                <div class="layui-tab-item layui-show">欢迎您，${userName}</div>
            </div>
        </div>
    </div>

    <!-- 底部 -->
    <div class="layui-footer" style="text-align: center; line-height: 44px;">
        &copy; 2017 Powered by <a href="https://automain.github.io/desc" target="_blank">automain</a>
    </div>
</div>
</body>
</html>
<script type="text/javascript">
    var element, layer;
    layui.use(['element', 'layer'], function () {
        element = layui.element
            , layer = layui.layer;
        <c:forEach items="${childrenList}" var="child">
        var tree = $.parseJSON('${child.nodeString}');
        var html = parseTree(tree, false);
        $("#menu-${child.parentId}").html(html);
        </c:forEach>
        element.on('tab(body-tab)', function (data) {
            reloadFrame();
        });
        element.render('nav');
    });

    function parseTree(tree, isChild) {
        var html = '';
        if (isChild) {
            html += '<ul class="layui-nav-child">';
        }
        for (var i = 0; i < tree.length; i++) {
            var node = tree[i];
            var icon = 'envira';
            if (node.icon) {
                icon = node.icon;
            }
            var link = node.link;
            var jumpFun = '';
            if (link) {
                jumpFun = 'onclick="addTab(\'' + node.id + '\',\'' + node.name + '\',\'' + icon + '\',\'' + link + '\')"';
            }
            var spread = '';
            if (node.isSpread === 1) {
                spread = ' layui-nav-itemed';
            }
            html += '<li class="layui-nav-item' + spread + '"><a href="javascript:;" ' + jumpFun + '>'
                + '<i class="fa fa-' + icon + '"></i><cite>' + node.name + '</cite></a>';
            if (node.children) {
                html += parseTree(node.children, true);
            }
            html += '</li>';
        }
        if (isChild) {
            html += '</ul>';
        }
        return html;
    }

    function addTab(id, name, icon, link){
        if (link) {
            if (!$(".layui-tab-title li[lay-id=" + id + "]")[0]) {
                var title = '<i class="fa fa-' + icon + '"></i>' + name;
                var iframe = '<iframe id="frame-' + id + '" onload="changeFrameHeight(\'frame-' + id + '\')" src="${ctx}' + link + '" style="width: 100%; border: 0px;"></iframe>';
                element.tabAdd('body-tab', {
                    title: title
                    , content: iframe
                    , id: id
                });
            }
            element.tabChange('body-tab', id);
        }
    }

    function closeOthers() {
        $(".layui-tab-title li").each(function (obj) {
            var that = $(this);
            if (!that.hasClass("layui-this")) {
                element.tabDelete('body-tab', that.attr("lay-id"));
            }
        });
    }

    function reloadFrame() {
        $(".layui-tab-title li").on("selectstart", function (e) {
            return false;
        });
        $(".layui-tab-title li").dblclick(function () {
            var id = "frame-" + $(this).attr("lay-id");
            document.getElementById(id).contentWindow.location.reload(true);
        });
    }

    function toggleSide() {
        var sideWidth = $(".layui-side").width();
        if (sideWidth === 200) {
            $(".layui-body").animate({
                left: '0'
            });
            $(".layui-footer").animate({
                left: '0'
            });
            $(".layui-side").animate({
                width: '0'
            });
        } else {
            $(".layui-body").animate({
                left: '200px'
            });
            $(".layui-footer").animate({
                left: '200px'
            });
            $(".layui-side").animate({
                width: '200px'
            });
        }
    }

    function showChildMenu(parentId) {
        $(".layui-nav-tree").hide();
        $("#menu-" + parentId).show();
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

    function changeFrameHeight(id) {
        var iframe = $("#" + id);
        iframe.height(iframe[0].attributes.style.ownerDocument.documentElement.clientHeight - 175);
    }
</script>
