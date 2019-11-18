package com.github.automain.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.automain.bean.SysUser;
import com.github.automain.common.container.ServiceContainer;
import com.github.automain.dao.SysPrivilegeDao;
import com.github.automain.dao.SysUserDao;
import com.github.automain.util.DateUtil;
import com.github.automain.util.EncryptUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BaseController implements ServiceContainer {

    private static final int SESSION_EXPIRE_SECONDS = PropertiesUtil.getIntProperty("app.sessionExpireSeconds", "1800");
    private static final int CACHE_EXPIRE_SECONDS = SESSION_EXPIRE_SECONDS + 600;
    private static final String AES_PASSWORD = PropertiesUtil.getStringProperty("app.AESPassword");
    private static final String AUTHORIZATION = "Authorization";

    public static <T> T getRequestParam(HttpServletRequest request, Class<T> clazz) {
        try (InputStreamReader isr = new InputStreamReader(request.getInputStream(), PropertiesUtil.DEFAULT_CHARSET);
             BufferedReader br = new BufferedReader(isr)) {
            StringBuilder sb = new StringBuilder();
            String temp = null;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            return JSONObject.parseObject(sb.toString(), clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SysUser getSessionUser(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String authorization = request.getHeader(AUTHORIZATION);
        if (StringUtils.isNotBlank(authorization)) {
            String decrypt = EncryptUtil.AESDecrypt(authorization.getBytes(PropertiesUtil.DEFAULT_CHARSET), AES_PASSWORD);
            String[] arr = decrypt.split("_");
            if (arr.length == 2) {
                String userGid = arr[0];
                String userCacheKey = "user:" + userGid;
                String userPrivilegeKey = "userPrivilege:" + userGid;
                int expireTime = Integer.parseInt(arr[1]);
                Map<String, String> userCacheMap = null;
                int now = DateUtil.getNow();
                int newExpireTime = now + SESSION_EXPIRE_SECONDS;
                int newCacheExpireTime = now + CACHE_EXPIRE_SECONDS;
                if (jedis != null) {
                    userCacheMap = jedis.hgetAll(userCacheKey);
                } else {
                    userCacheMap = RedisUtil.getLocalCache(userCacheKey);
                }
                boolean isRefresh = false;
                if (userCacheMap == null) {
                    if (expireTime < now) {
                        return null;
                    } else {
                        SysUser user = SysUserDao.selectTableByGid(connection, new SysUser().setGid(userGid));
                        userCacheMap = new HashMap<String, String>();
                        userCacheMap.put("userName", user.getUserName());
                        userCacheMap.put("phone", user.getPhone());
                        userCacheMap.put("email", user.getEmail());
                        userCacheMap.put("gid", user.getGid());
                        userCacheMap.put("realName", user.getRealName());
                        isRefresh = true;
                    }
                } else {
                    int cacheExpire = Integer.parseInt(userCacheMap.get("expireTime"));
                    if (cacheExpire < now) {
                        RedisUtil.delLocalCache(userCacheKey);
                        return null;
                    } else if (expireTime < now) {
                        isRefresh = true;
                    }
                }
                if (isRefresh) {
                    userCacheMap.put("expireTime", String.valueOf(newCacheExpireTime));
                    Set<String> privilegeSet = SysPrivilegeDao.selectUserPrivilege(connection, userGid);
                    if (jedis != null) {
                        jedis.del(userCacheKey);
                        jedis.hmset(userCacheKey, userCacheMap);
                        jedis.expire(userCacheKey, CACHE_EXPIRE_SECONDS);
                        String[] privilegeArr = new String[privilegeSet.size()];
                        privilegeArr = privilegeSet.toArray(privilegeArr);
                        jedis.del(userPrivilegeKey);
                        jedis.sadd(userPrivilegeKey, privilegeArr);
                        jedis.expire(userPrivilegeKey, CACHE_EXPIRE_SECONDS);
                    } else {
                        RedisUtil.delLocalCache(userCacheKey);
                        RedisUtil.setLocalCache(userCacheKey, userCacheMap);
                        RedisUtil.delLocalCache(userPrivilegeKey);
                        RedisUtil.setLocalCache(userPrivilegeKey, privilegeSet);
                    }
                    String value = userGid + "_" + newExpireTime;
                    authorization = EncryptUtil.AESEncrypt(value.getBytes(PropertiesUtil.DEFAULT_CHARSET), AES_PASSWORD);
                    response.setHeader(AUTHORIZATION, authorization);
                }
                return new SysUser().setGid(userGid).setUserName(userCacheMap.get("userName")).setPhone(userCacheMap.get("phone")).setEmail(userCacheMap.get("email")).setGid(userCacheMap.get("gid")).setRealName(userCacheMap.get("realName"));
            }
        }
        return null;
    }
}
