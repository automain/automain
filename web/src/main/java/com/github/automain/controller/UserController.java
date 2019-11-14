package com.github.automain.controller;

import com.github.automain.bean.SysMenu;
import com.github.automain.bean.SysPrivilege;
import com.github.automain.bean.SysRole;
import com.github.automain.bean.SysUser;
import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.container.ServiceDaoContainer;
import com.github.automain.util.CaptchaUtil;
import com.github.automain.util.DateUtil;
import com.github.automain.util.EncryptUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.RedisUtil;
import com.github.automain.util.SystemUtil;
import com.github.automain.vo.IdNameVO;
import com.github.automain.vo.LoginUserVO;
import com.github.automain.vo.MenuVO;
import com.github.automain.vo.SysMenuVO;
import com.github.automain.vo.SysPrivilegeVO;
import com.github.automain.vo.SysRoleVO;
import com.github.automain.vo.SysUserVO;
import com.github.fastjdbc.PageBean;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserController implements ServiceDaoContainer {

    private static final int SESSION_EXPIRE_SECONDS = PropertiesUtil.getIntProperty("app.sessionExpireSeconds", "1800");
    private static final int CACHE_EXPIRE_SECONDS = SESSION_EXPIRE_SECONDS + 600;
    private static final String AES_PASSWORD = PropertiesUtil.getStringProperty("app.AESPassword");

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
                if (captcha.equalsIgnoreCase(cacheCaptcha)) {
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
                            response.setHeader("Authorization", authorization);
                            List<MenuVO> menuData = SYS_MENU_SERVICE.authorityMenu(connection, sysUser.getId());
                            return JsonResponse.getSuccessJson("登录成功", menuData);
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

    // user
    @RequestUri(value = "/userList", slave = "slave1")
    public JsonResponse userList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUserVO vo = SystemUtil.getRequestParam(request, SysUserVO.class);
        if (vo != null) {
            PageBean<SysUser> pageBean = SYS_USER_DAO.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/userAdd")
    public JsonResponse userAdd(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUser bean = SystemUtil.getRequestParam(request, SysUser.class);
        if (checkValid(bean, false)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            bean.setGid(UUID.randomUUID().toString());
            SYS_USER_DAO.insertIntoTable(connection, bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysUser bean, boolean isUpdate) {
        return bean != null && (!isUpdate || bean.getGid() != null);
    }

    @RequestUri("/userUpdate")
    public JsonResponse userUpdate(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUser bean = SystemUtil.getRequestParam(request, SysUser.class);
        if (checkValid(bean, true)) {
            bean.setUpdateTime(DateUtil.getNow());
            SYS_USER_DAO.updateTableByGid(connection, bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/userDetail", slave = "slave1")
    public JsonResponse userDetail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUser bean = SystemUtil.getRequestParam(request, SysUser.class);
        if (bean != null && bean.getGid() != null) {
            SysUser detail = SYS_USER_DAO.selectTableByGid(connection, bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/userDelete")
    public JsonResponse userDelete(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUserVO vo = SystemUtil.getRequestParam(request, SysUserVO.class);
        if (vo != null && CollectionUtils.isNotEmpty(vo.getGidList())) {
            SYS_USER_DAO.softDeleteTableByGidList(connection, vo.getGidList());
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    // menu
    @RequestUri(value = "/menuList", slave = "slave1")
    public JsonResponse menuList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysMenuVO vo = SystemUtil.getRequestParam(request, SysMenuVO.class);
        if (vo != null) {
            PageBean<SysMenu> pageBean = SYS_MENU_DAO.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/menuAdd")
    public JsonResponse menuAdd(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysMenu bean = SystemUtil.getRequestParam(request, SysMenu.class);
        if (checkValid(bean, false)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            SYS_MENU_DAO.insertIntoTable(connection, bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysMenu bean, boolean isUpdate) {
        return bean != null && (!isUpdate || bean.getId() != null);
    }

    @RequestUri("/menuUpdate")
    public JsonResponse menuUpdate(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysMenu bean = SystemUtil.getRequestParam(request, SysMenu.class);
        if (checkValid(bean, true)) {
            bean.setUpdateTime(DateUtil.getNow());
            SYS_MENU_DAO.updateTableById(connection, bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/menuDetail", slave = "slave1")
    public JsonResponse menuDetail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysMenu bean = SystemUtil.getRequestParam(request, SysMenu.class);
        if (bean != null && bean.getId() != null) {
            SysMenu detail = SYS_MENU_DAO.selectTableById(connection, bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/menuDelete")
    public JsonResponse menuDelete(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysMenuVO vo = SystemUtil.getRequestParam(request, SysMenuVO.class);
        if (vo != null && CollectionUtils.isNotEmpty(vo.getIdList())) {
            SYS_MENU_DAO.softDeleteTableByIdList(connection, vo.getIdList());
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/allValidMenu", slave = "slave1")
    public JsonResponse allValidMenu(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<IdNameVO> menuVOList = SYS_MENU_DAO.allValidMenu(connection);
        return JsonResponse.getSuccessJson(menuVOList);
    }

    // role
    @RequestUri(value = "/roleList", slave = "slave1")
    public JsonResponse roleList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysRoleVO vo = SystemUtil.getRequestParam(request, SysRoleVO.class);
        if (vo != null) {
            PageBean<SysRole> pageBean = SYS_ROLE_DAO.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/roleAdd")
    public JsonResponse roleAdd(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysRole bean = SystemUtil.getRequestParam(request, SysRole.class);
        if (checkValid(bean, false)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            SYS_ROLE_DAO.insertIntoTable(connection, bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysRole bean, boolean isUpdate) {
        return bean != null && (!isUpdate || bean.getId() != null);
    }

    @RequestUri("/roleUpdate")
    public JsonResponse roleUpdate(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysRole bean = SystemUtil.getRequestParam(request, SysRole.class);
        if (checkValid(bean, true)) {
            bean.setUpdateTime(DateUtil.getNow());
            SYS_ROLE_DAO.updateTableById(connection, bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/roleDetail", slave = "slave1")
    public JsonResponse roleDetail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysRole bean = SystemUtil.getRequestParam(request, SysRole.class);
        if (bean != null && bean.getId() != null) {
            SysRole detail = SYS_ROLE_DAO.selectTableById(connection, bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/roleDelete")
    public JsonResponse roleDelete(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysRoleVO vo = SystemUtil.getRequestParam(request, SysRoleVO.class);
        if (vo != null && CollectionUtils.isNotEmpty(vo.getIdList())) {
            SYS_ROLE_DAO.softDeleteTableByIdList(connection, vo.getIdList());
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }
    // privilege
    @RequestUri(value = "/privilegeList", slave = "slave1")
    public JsonResponse privilegeList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysPrivilegeVO vo = SystemUtil.getRequestParam(request, SysPrivilegeVO.class);
        if (vo != null) {
            PageBean<SysPrivilege> pageBean = SYS_PRIVILEGE_DAO.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/privilegeAdd")
    public JsonResponse privilegeAdd(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysPrivilege bean = SystemUtil.getRequestParam(request, SysPrivilege.class);
        if (checkValid(bean, false)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            SYS_PRIVILEGE_DAO.insertIntoTable(connection, bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysPrivilege bean, boolean isUpdate) {
        return bean != null && (!isUpdate || bean.getId() != null);
    }

    @RequestUri("/privilegeUpdate")
    public JsonResponse privilegeUpdate(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysPrivilege bean = SystemUtil.getRequestParam(request, SysPrivilege.class);
        if (checkValid(bean, true)) {
            bean.setUpdateTime(DateUtil.getNow());
            SYS_PRIVILEGE_DAO.updateTableById(connection, bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/privilegeDetail", slave = "slave1")
    public JsonResponse privilegeDetail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysPrivilege bean = SystemUtil.getRequestParam(request, SysPrivilege.class);
        if (bean != null && bean.getId() != null) {
            SysPrivilege detail = SYS_PRIVILEGE_DAO.selectTableById(connection, bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/privilegeDelete")
    public JsonResponse privilegeDelete(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysPrivilegeVO vo = SystemUtil.getRequestParam(request, SysPrivilegeVO.class);
        if (vo != null && CollectionUtils.isNotEmpty(vo.getIdList())) {
            SYS_PRIVILEGE_DAO.softDeleteTableByIdList(connection, vo.getIdList());
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }
}