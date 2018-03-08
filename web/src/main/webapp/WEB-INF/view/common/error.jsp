<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
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
</div>
</body>
</html>
