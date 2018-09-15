package com.github.automain.common.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.util.RedisUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/notice/forward")
public class NoticeForwardExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forwardType = getString("forwardType", request, "");
        String jspPath = null;
        switch (forwardType) {
            case "add":
                jspPath = "common/notice_add";
                break;
            default:
                if (jedis != null) {
                    request.setAttribute("noticeMap", jedis.hgetAll("notice_cache_key"));
                } else {
                    request.setAttribute("noticeMap", RedisUtil.getLocalCache("notice_cache_key"));
                }
                request.setAttribute("vEnter", "\n");
                jspPath = "common/notice_tab";
        }
        return jspPath;
    }
}