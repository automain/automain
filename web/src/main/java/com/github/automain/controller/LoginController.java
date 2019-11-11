package com.github.automain.controller;

import com.github.automain.bean.SysUser;
import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.container.ServiceDaoContainer;
import com.github.automain.util.CaptchaUtil;
import com.github.automain.util.CookieUtil;
import com.github.automain.util.DateUtil;
import com.github.automain.util.EncryptUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.RedisUtil;
import com.github.automain.util.SystemUtil;
import com.github.automain.vo.LoginUserVO;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoginController implements ServiceDaoContainer {

    private static final int SESSION_EXPIRE_SECONDS = PropertiesUtil.getIntProperty("app.sessionExpireSeconds", "1800");
    private static final int CACHE_EXPIRE_SECONDS = SESSION_EXPIRE_SECONDS + 600;
    private static final String AES_PASSWORD = PropertiesUtil.getStringProperty("app.AESPassword");
    private static final String AUTHORIZATION = "Authorization";

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

    @RequestUri("/login")
    public JsonResponse login(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginUserVO user = SystemUtil.getRequestParam(request, LoginUserVO.class);
        if (user != null) {
            String userName = user.getUserName();
            String password = user.getPassword();
            String captcha = user.getCaptcha();
            String captchaKey = user.getCaptchaKey();
            if (StringUtils.isNoneBlank(userName, password, captcha, captchaKey)) {
                String cacheCaptcha = null;
                if (jedis != null) {
                    cacheCaptcha = jedis.get(user.getCaptchaKey());
                } else {
                    cacheCaptcha = RedisUtil.getLocalCache(user.getCaptchaKey());
                }
                if (captcha.equals(cacheCaptcha)) {
                    SysUser sysUser = SYS_USER_DAO.selectOneTableByBean(connection, new SysUser().setUserName(userName));
                    if (sysUser != null) {
                        String pwd = EncryptUtil.MD5((sysUser.getPasswordMd5() + captcha).getBytes(PropertiesUtil.DEFAULT_CHARSET));
                        if (password.equalsIgnoreCase(pwd)) {
                            int now = DateUtil.getNow();
                            int expireTime = now + SESSION_EXPIRE_SECONDS;
                            int cacheExpireTime = now + CACHE_EXPIRE_SECONDS;
                            String userCacheKey = "user:" + sysUser.getId();
                            Map<String, String> userCacheMap = new HashMap<String, String>();
                            userCacheMap.put("userName", sysUser.getUserName());
                            userCacheMap.put("phone", sysUser.getPhone());
                            userCacheMap.put("email", sysUser.getEmail());
                            userCacheMap.put("expireTime", String.valueOf(cacheExpireTime));
                            if (jedis != null) {
                                jedis.del(userCacheKey);
                                jedis.hmset(userCacheKey, userCacheMap);
                                jedis.expire(userCacheKey, CACHE_EXPIRE_SECONDS);
                            } else {
                                RedisUtil.delLocalCache(userCacheKey);
                                RedisUtil.setLocalCache(userCacheKey, userCacheMap);
                            }
                            String authorization = EncryptUtil.AESEncrypt((sysUser.getId() + "_" + expireTime).getBytes(PropertiesUtil.DEFAULT_CHARSET), AES_PASSWORD);
                            CookieUtil.addCookie(response, AUTHORIZATION, authorization, CACHE_EXPIRE_SECONDS);
                            response.addHeader(AUTHORIZATION, authorization);
                            return JsonResponse.getSuccessJson("登录成功");
                        } else {
                            return JsonResponse.getFailedJson("用户名或密码错误");
                        }
                    } else {
                        return JsonResponse.getFailedJson("用户不存在");
                    }
                } else {
                    return JsonResponse.getFailedJson("验证码错误");
                }
            }
        }
        return JsonResponse.getFailedJson("参数错误");
    }
}
