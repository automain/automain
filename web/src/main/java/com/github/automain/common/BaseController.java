package com.github.automain.common;

import com.github.automain.common.container.ServiceContainer;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.SystemUtil;
import com.github.fastjdbc.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

public class BaseController extends RequestUtil implements ServiceContainer {

    public static final int SESSION_EXPIRE_SECONDS = PropertiesUtil.getIntProperty("app.sessionExpireSeconds", "1800");
    public static final int CACHE_EXPIRE_SECONDS = SESSION_EXPIRE_SECONDS + 300;
    public static final String AES_PASSWORD = PropertiesUtil.getStringProperty("app.AESPassword");
    public static final String CODE_SUCCESS = "0";// 返回成功
    public static final String CODE_FAIL = "1";// 返回失败
    public static final String PAGE_BEAN_PARAM = "pageBean";// 分页对象参数名
    public static final Logger LOGGER = SystemUtil.getLogger();

    public static void setJsonResult(HttpServletRequest request, String code, String msg) {
        request.setAttribute("code", code);
        request.setAttribute("msg", msg);
    }

    public static void setSuccessJsonResult(HttpServletRequest request) {
        setJsonResult(request, CODE_SUCCESS, "");
    }
}
