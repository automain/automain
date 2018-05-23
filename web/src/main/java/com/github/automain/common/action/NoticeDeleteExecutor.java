package com.github.automain.common.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.RequestUrl;
import com.github.automain.util.CookieUtil;
import com.github.automain.util.RedisUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/notice/delete")
public class NoticeDeleteExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (jedis != null) {
            jedis.del("notice_cache_key");
        } else {
            RedisUtil.LOCAL_CACHE.remove("notice_cache_key");
        }
        CookieUtil.deleteCookieByName(response, "hasReadNotice");
        setJsonResult(request, CODE_SUCCESS, "清除成功");
        return null;
    }
}