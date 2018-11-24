<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="common.jsp" %>
    <title>错误页面</title>
    <style type="text/css">
        * {
            margin: 0;
            padding: 0;
        }

        html {
            height: 100%
        }

        body {
            font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
            font-size: 13px;
            color: #676a6c;
            overflow-x: hidden;
            height: 100%;
            background: #eee;
        }

        .page-error {
            max-width: 400px;
            z-index: 100;
            margin: 0 auto;
            padding-top: 40px;
            text-align: center;
        }

        .page-error h1 {
            font-size: 170px;
            margin: 0;
            font-weight: 100;
        }

        .page-error h3 {
            font-size: 17px;
            margin-top: 5px;
            margin-bottom: 10px;
            font-weight: 600;
        }
    </style>
</head>
<body class="gray-bg">
<div class="page-error">
    <h1>${code}</h1>
    <h3 class="font-bold">${msg}</h3>
    <div id="jumpDiv" style="display: none;">
        <h3 class="font-bold"><span id="intervalTime">3</span>秒后跳转到登录页面</h3>
    </div>
</div>
</body>
</html>
<script>
    var i;
    if (${code == 403 || (code == 503 && !fromLogin)} ) {
        $("#jumpDiv").show();
        i = setInterval(function(){shrink()}, 1000);
    }
    function shrink(){
        var val = $("#intervalTime").html();
        console.log(val);
        var sub = val - 1;
        if (sub > 0) {
            $("#intervalTime").html(sub);
        } else {
            window.clearInterval(i);
            top.location.href = '${ctx}/';
        }
    }
</script>