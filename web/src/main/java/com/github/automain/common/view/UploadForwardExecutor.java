package com.github.automain.common.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.RequestUrl;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/upload/forward")
public class UploadForwardExecutor extends BaseExecutor {

    /**
     * 跳转到示例上传图片页面，需将页面相关元素移植到业务页面，上传其他类型文件参照layui上传组件文档
     * @param connection
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "common/upload";
    }
}
