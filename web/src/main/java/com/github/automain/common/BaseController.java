package com.github.automain.common;

import com.github.automain.common.container.ServiceContainer;
import com.github.automain.util.SystemUtil;
import com.github.fastjdbc.util.RequestUtil;

import java.util.logging.Logger;

public class BaseController extends RequestUtil implements ServiceContainer {

    public static final String CODE_SUCCESS = "0";// 返回成功
    public static final String CODE_FAIL = "1";// 返回失败
    public static final String PAGE_BEAN_PARAM = "pageBean";// 分页对象参数名
    public static final Logger LOGGER = SystemUtil.getLogger();

}
