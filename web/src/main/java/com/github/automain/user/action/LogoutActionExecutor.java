package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.RequestUrl;
import com.github.automain.util.CookieUtil;
import com.github.automain.util.EncryptUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.RedisUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/user/logout/action")
public class LogoutActionExecutor extends BaseExecutor {

    @Override
    protected boolean checkAuthority(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return checkUserLogin(jedis, request, response);
    }

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String accessToken = CookieUtil.getCookieByName(request, "accessToken");
        if (accessToken != null) {
            String decrypt = EncryptUtil.AESDecrypt(accessToken.getBytes(PropertiesUtil.DEFAULT_CHARSET), PropertiesUtil.SECURITY_KEY);
            String[] arr = decrypt.split("_");
            if (arr.length == 2) {
                Integer userId = Integer.valueOf(arr[0]);
                String userKey = "user:" + userId;
                if (jedis != null) {
                    jedis.del(userKey);
                } else {
                    RedisUtil.LOCAL_CACHE.remove(userKey);
                }
            }
            CookieUtil.deleteCookieByName(response, "accessToken");
        }
        setJsonResult(request, CODE_SUCCESS, "退出成功");
        return null;
    }
}
