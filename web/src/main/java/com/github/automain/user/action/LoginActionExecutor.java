package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.user.bean.TbUser;
import com.github.automain.util.CookieUtil;
import com.github.automain.util.EncryptUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.RedisUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class LoginActionExecutor extends BaseExecutor {

    private static final String CAPTCHA_RANDOM_KEY = "captchaRandomKey";

    @Override
    protected boolean checkAuthority(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return true;
    }

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Integer captchaPosition = getInt("captchaPosition", request);
        String key = getString(CAPTCHA_RANDOM_KEY, request);
        Object captcha = null;
        if (jedis != null) {
            captcha = jedis.get(key);
        } else {
            captcha = RedisUtil.LOCAL_CACHE.get(key);
        }
        if (captcha != null){
            Integer sessionCaptcha = Integer.valueOf(String.valueOf(captcha));
            if (checkCaptcha(sessionCaptcha, captchaPosition)){
                String username = getString("username", request);
                String token = getString("token", request);
                TbUser user = TB_USER_SERVICE.selectTableByUserName(connection, username);
                if (user != null) {
                    String toMD5 = user.getPasswordMd5() + captchaPosition;
                    String serverToken = EncryptUtil.MD5(toMD5.getBytes(PropertiesUtil.DEFAULT_CHARSET));
                    if (serverToken.equals(token)) {
                        long expireTime = System.currentTimeMillis() + PropertiesUtil.SESSION_EXPIRE_MILLISECOND;
                        long cacheExpireTime = System.currentTimeMillis() + PropertiesUtil.CACHE_EXPIRE_MILLISECOND;
                        String userKey = "user:" + user.getUserId();
                        Map<String, String> userMap = new HashMap<String, String>();
                        userMap.put("userName", user.getUserName());
                        userMap.put("cellphone", user.getCellphone());
                        userMap.put("email", user.getEmail());
                        userMap.put("expireTime", String.valueOf(cacheExpireTime));
                        if (jedis != null) {
                            jedis.del(key);
                            jedis.hmset(userKey, userMap);
                            jedis.expire(userKey, PropertiesUtil.CACHE_EXPIRE_SECONDS);
                        } else {
                            RedisUtil.LOCAL_CACHE.remove(key);
                            RedisUtil.LOCAL_CACHE.put(userKey, userMap);
                        }
                        String value = user.getUserId() + "_" + expireTime;
                        String accessToken = EncryptUtil.AESEncrypt(value.getBytes(PropertiesUtil.DEFAULT_CHARSET), PropertiesUtil.SECURITY_KEY);
                        CookieUtil.addCookie(response, "accessToken", accessToken, -1);
                        setJsonResult(request, CODE_SUCCESS, "登录成功");
                        return null;
                    }
                }
            }
        }
        setJsonResult(request, CODE_FAIL, "登录失败");
        return null;
    }

    private boolean checkCaptcha(Integer sessionCaptcha, Integer captchaPosition){
        if (sessionCaptcha != null && captchaPosition != null){
            int minPosition = sessionCaptcha - 5;
            int maxPosition = sessionCaptcha + 5;
            return captchaPosition > minPosition && captchaPosition < maxPosition;
        }
        return false;
    }
}
