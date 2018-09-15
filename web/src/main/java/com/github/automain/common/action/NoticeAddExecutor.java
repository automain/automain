package com.github.automain.common.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.util.DateUtil;
import com.github.automain.util.RedisUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RequestUrl("/notice/add")
public class NoticeAddExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String noticeEndTime = getString("noticeEndTime", request);
        Map<String, String> noticeMap = new HashMap<String, String>();
        noticeMap.put("noticeStartTime", getString("noticeStartTime", request));
        noticeMap.put("noticeEndTime", noticeEndTime);
        noticeMap.put("noticeTitle", getString("noticeTitle", request));
        noticeMap.put("noticeContent", getString("noticeContent", request));
        if (jedis != null) {
            jedis.hmset("notice_cache_key", noticeMap);
            jedis.expireAt("notice_cache_key", DateUtil.convertStringToLong(noticeEndTime, DateUtil.SIMPLE_DATE_TIME_PATTERN) / 1000);
        } else {
            RedisUtil.setLocalCache("notice_cache_key", noticeMap);
        }
        setJsonResult(request, CODE_SUCCESS, "添加成功");
        return null;
    }
}