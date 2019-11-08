package com.github.automain.controller;

import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.container.ServiceDaoContainer;
import com.github.automain.util.CaptchaUtil;
import com.github.automain.util.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoginController implements ServiceDaoContainer {

    @RequestUri("/getCaptcha")
    public JsonResponse getCaptcha(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        CaptchaUtil captcha = new CaptchaUtil(140, 40);
        String captchaCode = captcha.getCaptcha();
        String captchaKey = UUID.randomUUID().toString();
        if (jedis != null) {
            jedis.set(captchaKey, captchaCode, SetParams.setParams().ex(60));
        } else {
            RedisUtil.setLocalCache(captchaKey, captchaCode);
        }
        Map<String, String> result = new HashMap<String, String>();
        result.put("captchaKey", captchaKey);
        result.put("captchaImage", captcha.getBase64Image());
        return JsonResponse.getSuccessJson(result);
    }
}
