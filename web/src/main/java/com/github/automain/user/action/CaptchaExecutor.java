package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.util.CaptchaUtil;
import com.github.automain.util.RedisUtil;
import com.github.automain.util.SystemUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CaptchaExecutor extends BaseExecutor {

    private static final String CAPTCHA_RANDOM_KEY = "captchaRandomKey";

    @Override
    protected boolean checkAuthority(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return true;
    }

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer width = getInt("width", request, 350);
        Integer height = getInt("height", request, 42);
        CaptchaUtil captcha = new CaptchaUtil(width, height);
        String position = String.valueOf(captcha.getPercentPosition());
        request.setAttribute("image", captcha.getBase64Image());
        String key = SystemUtil.randomUUID();
        if (jedis != null) {
            jedis.set(key, position);
            jedis.expire(key, 180);
        } else {
            RedisUtil.LOCAL_CACHE.put(key, position);
        }
        request.setAttribute(CAPTCHA_RANDOM_KEY, key);
        return null;
    }
}