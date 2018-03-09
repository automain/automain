<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%--网站编码--%>
<meta charset="utf-8"/>
<%--网站关键字--%>
<meta name="keywords"content="automain"/>
<%--网站描述--%>
<meta name="description"content="automain"/>
<%--网站应用技术--%>
<meta name="generator"content="servlet,fastjdbc,automain"/>
<%--忽略页面中的数字识别为电话号码,邮箱格式识别为邮箱--%>
<meta name="format-detection" content="telephone=no,email=no"/>
<%--优先使用 IE 最新版本和 Chrome--%>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<%--删除默认的苹果工具栏和菜单栏，开启全屏显示--%>
<meta name="apple-mobile-web-app-capable" content="yes"/>
<%--在苹果web app应用下状态条(屏幕顶部条)的颜色--%>
<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
<meta http-equiv="Cache-Control" content="no-siteapp"/>
<%--网页宽度及初始、最大缩放比例--%>
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
<%--网站icon--%>
<link rel="shortcut icon" href="${ctx}/static/images/common/favicon.ico" type="image/x-icon"/>
<link rel="stylesheet" href="${ctx}/static/layui/css/layui.css" media="all"/>
<link rel="stylesheet" href="${ctx}/static/font/css/font-awesome.min.css" media="all"/>
<link rel="stylesheet" href="${ctx}/static/css/common/common.css" media="all"/>
<script src="${ctx}/static/js/plugin/jquery-3.2.1.min.js"></script>
<script src="${ctx}/static/layui/layui.js"></script>
<script src="${ctx}/static/js/common/common.js"></script>
<script>
    /**
     * 对layui进行全局配置
     */
    layui.config({
        base: '${ctx}/static/js/common/'
    });
    /**
     * 延迟加载时间
     * @type {number}
     */
    var loadingTime = 50;
    /**
     * 成功返回码
     * @type {number}
     */
    var code_success = 0;
    /**
     * 失败返回码
     * @type {number}
     */
    var code_fail = 1;

    layui.use('layer', function () {
        var layer = layui.layer;
        var noticeStartTime = '${notice_cache_key['noticeStartTime']}';
        var noticeEndTime = '${notice_cache_key['noticeEndTime']}';
        var noticeTitle = '<c:out value="${notice_cache_key['noticeTitle']}"/>';
        var noticeContent = '${fn:replace(notice_cache_key['noticeContent'],vEnter, '<br/>')}';
        alertNotice(layer, noticeStartTime, noticeEndTime, noticeTitle, noticeContent);
    });
</script>