package com.github.automain.controller;

import com.github.automain.bean.SysFile;
import com.github.automain.bean.SysMenu;
import com.github.automain.bean.SysPrivilege;
import com.github.automain.bean.SysRole;
import com.github.automain.bean.SysUser;
import com.github.automain.bean.SysUserRole;
import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.controller.BaseController;
import com.github.automain.dao.SysFileDao;
import com.github.automain.dao.SysMenuDao;
import com.github.automain.dao.SysPrivilegeDao;
import com.github.automain.dao.SysRoleDao;
import com.github.automain.dao.SysUserDao;
import com.github.automain.dao.SysUserRoleDao;
import com.github.automain.util.CaptchaUtil;
import com.github.automain.util.DateUtil;
import com.github.automain.util.EncryptUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.RedisUtil;
import com.github.automain.vo.IdNameVO;
import com.github.automain.vo.LoginUserVO;
import com.github.automain.vo.MenuVO;
import com.github.automain.vo.RoleVO;
import com.github.automain.vo.SysMenuVO;
import com.github.automain.vo.SysPrivilegeVO;
import com.github.automain.vo.SysRoleVO;
import com.github.automain.vo.SysUserVO;
import com.github.automain.vo.SysUserAddVO;
import com.github.automain.vo.TreeVO;
import com.github.fastjdbc.PageBean;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class UserController extends BaseController {

    private static final int SESSION_EXPIRE_SECONDS = PropertiesUtil.getIntProperty("app.sessionExpireSeconds", "1800");
    private static final int CACHE_EXPIRE_SECONDS = SESSION_EXPIRE_SECONDS + 600;
    private static final String AES_PASSWORD = PropertiesUtil.getStringProperty("app.AESPassword");

    @RequestUri(value = "/getCaptcha", slave = "slave1")
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

    @RequestUri(value = "/login", slave = "slave1")
    public JsonResponse login(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginUserVO user = getRequestParam(request, LoginUserVO.class);
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
                    SysUser sysUser = SysUserDao.selectOneTableByBean(connection, new SysUser().setUserName(userName));
                    if (sysUser != null) {
                        String pwd = EncryptUtil.MD5((sysUser.getPasswordMd5() + captcha).getBytes(PropertiesUtil.DEFAULT_CHARSET));
                        if (password.equalsIgnoreCase(pwd)) {
                            String userGid = sysUser.getGid();
                            int now = DateUtil.getNow();
                            int expireTime = now + SESSION_EXPIRE_SECONDS;
                            int cacheExpireTime = now + CACHE_EXPIRE_SECONDS;
                            String userCacheKey = "user:" + userGid;
                            Map<String, String> userCacheMap = new HashMap<String, String>();
                            userCacheMap.put("userName", sysUser.getUserName());
                            userCacheMap.put("phone", sysUser.getPhone());
                            userCacheMap.put("email", sysUser.getEmail());
                            userCacheMap.put("gid", sysUser.getGid());
                            userCacheMap.put("realName", sysUser.getRealName());
                            userCacheMap.put("headImgGid", sysUser.getHeadImgGid());
                            userCacheMap.put("expireTime", String.valueOf(cacheExpireTime));
                            String userPrivilegeKey = "userPrivilege:" + userGid;
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
                            String authorization = EncryptUtil.AESEncrypt((userGid + "_" + expireTime).getBytes(PropertiesUtil.DEFAULT_CHARSET), AES_PASSWORD);
                            response.setHeader("Authorization", authorization);
                            List<MenuVO> menuData = SYS_USER_SERVICE.selectAuthorityMenu(connection, userGid);
                            Map<String, Object> map = new HashMap<String, Object>(2);
                            map.put("menuData", menuData);
                            map.put("privilege", privilegeSet);
                            map.put("realName", sysUser.getRealName());
                            return JsonResponse.getSuccessJson("登录成功", map);
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

    @RequestUri(value = "/logout", slave = "slave1")
    public JsonResponse logout(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUser user = getSessionUser(connection, jedis, request, response);
        if (user != null) {
            Integer userId = user.getId();
            String userCacheKey = "user:" + userId;
            String userPrivilegeKey = "userPrivilege:" + userId;
            if (jedis != null) {
                jedis.del(userCacheKey);
                jedis.del(userPrivilegeKey);
            } else {
                RedisUtil.delLocalCache(userCacheKey);
                RedisUtil.delLocalCache(userPrivilegeKey);
            }
        }
        return JsonResponse.getSuccessJson();
    }

    // user
    @RequestUri(value = "/checkUserExist", slave = "slave1")
    public JsonResponse checkUserExist(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
         String userName = request.getParameter("userName");
         if (StringUtils.isNotBlank(userName)) {
             int count = SysUserDao.countTableByBean(connection, new SysUser().setUserName(userName).setIsValid(1));
             if (count == 0) {
                 return JsonResponse.getSuccessJson();
             }
         }
         return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/userList", slave = "slave1")
    public JsonResponse userList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUserVO vo = getRequestParam(request, SysUserVO.class);
        if (vo != null) {
            PageBean<SysUserAddVO> pageBean = SysUserDao.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/userAdd")
    public JsonResponse userAdd(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUserAddVO bean = getRequestParam(request, SysUserAddVO.class);
        if (checkValid(bean) && bean.getPassword() != null && bean.getPassword().equals(bean.getPassword2())) {
            String gid = UUID.randomUUID().toString();
            bean.setPasswordMd5(EncryptUtil.MD5(bean.getPassword().getBytes(PropertiesUtil.DEFAULT_CHARSET)));
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            bean.setGid(gid);
            SysUserDao.insertIntoTable(connection, bean);
            List<Integer> idList = SysRoleDao.selectRoleIdByLabelList(connection, bean.getUserRoleList());
            List<SysUserRole> userRoleList = new ArrayList<SysUserRole>(idList.size());
            int now = DateUtil.getNow();
            for (Integer id : idList) {
                userRoleList.add(new SysUserRole().setUserGid(gid).setRoleId(id).setIsValid(1).setCreateTime(now).setUpdateTime(now));
            }
            SysUserRoleDao.batchInsertIntoTable(connection, userRoleList);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysUser bean) {
        return bean != null && bean.getUserName() != null && bean.getRealName() != null && bean.getPhone() != null && bean.getEmail() != null;
    }

    @RequestUri("/userUpdate")
    public JsonResponse userUpdate(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUserAddVO bean = getRequestParam(request, SysUserAddVO.class);
        if (checkValid(bean) && bean.getGid() != null) {
            bean.setUpdateTime(DateUtil.getNow());
            SysUser sysUser = SysUserDao.selectTableByGid(connection, bean);
            if (sysUser != null) {
                bean.setId(sysUser.getId()).setCreateTime(sysUser.getCreateTime()).setPasswordMd5(sysUser.getPasswordMd5()).setIsValid(sysUser.getIsValid());
                SysUserDao.updateTableByGid(connection, bean, true);
                String oldHeadImgGid = sysUser.getHeadImgGid();
                if (oldHeadImgGid != null && !oldHeadImgGid.equals(bean.getHeadImgGid())) {
                    SysFileDao.softDeleteTableByGid(connection, new SysFile().setGid(oldHeadImgGid));
                }

                String gid = sysUser.getGid();
                SysUserRoleDao.softDeleteRoleByUserGid(connection, gid);
                List<Integer> idList = SysRoleDao.selectRoleIdByLabelList(connection, bean.getUserRoleList());
                List<SysUserRole> userRoleList = new ArrayList<SysUserRole>(idList.size());
                int now = DateUtil.getNow();
                for (Integer id : idList) {
                    userRoleList.add(new SysUserRole().setUserGid(gid).setRoleId(id).setIsValid(1).setCreateTime(now).setUpdateTime(now));
                }
                SysUserRoleDao.batchInsertIntoTable(connection, userRoleList);
                return JsonResponse.getSuccessJson();
            }
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/userDetail", slave = "slave1")
    public JsonResponse userDetail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUser bean = getRequestParam(request, SysUser.class);
        if (bean != null && bean.getGid() != null) {
            String gid = bean.getGid();
            SysUserAddVO detail = SysUserDao.selectUserVOByGid(connection, gid);
            if (detail != null) {
                detail.setUserRoleList(SysUserRoleDao.selectUserRoleLabelList(connection, gid));
            }
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/userDelete")
    public JsonResponse userDelete(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUserVO vo = getRequestParam(request, SysUserVO.class);
        if (vo != null && CollectionUtils.isNotEmpty(vo.getGidList())) {
            SysUserDao.softDeleteTableByGidList(connection, vo.getGidList());
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    // menu
    @RequestUri(value = "/menuList", slave = "slave1")
    public JsonResponse menuList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysMenuVO vo = getRequestParam(request, SysMenuVO.class);
        if (vo != null) {
            PageBean<SysMenu> pageBean = SysMenuDao.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/menuAdd")
    public JsonResponse menuAdd(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysMenu bean = getRequestParam(request, SysMenu.class);
        if (checkValid(bean)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            SysMenuDao.insertIntoTable(connection, bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysMenu bean) {
        return bean != null && bean.getMenuName() != null && bean.getMenuIcon() != null && bean.getParentId() != null && bean.getSequenceNumber() != null;
    }

    @RequestUri("/menuUpdate")
    public JsonResponse menuUpdate(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysMenu bean = getRequestParam(request, SysMenu.class);
        if (checkValid(bean) && bean.getId() != null) {
            bean.setUpdateTime(DateUtil.getNow());
            SysMenuDao.updateTableById(connection, bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/menuDetail", slave = "slave1")
    public JsonResponse menuDetail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysMenu bean = getRequestParam(request, SysMenu.class);
        if (bean != null && bean.getId() != null) {
            SysMenu detail = SysMenuDao.selectTableById(connection, bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/allValidMenu", slave = "slave1")
    public JsonResponse allValidMenu(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<IdNameVO> menuVOList = SysMenuDao.allValidMenu(connection);
        return JsonResponse.getSuccessJson(menuVOList);
    }

    // role
    @RequestUri(value = "/checkRoleExist", slave = "slave1")
    public JsonResponse checkRoleExist(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String roleLabel = request.getParameter("roleLabel");
        if (StringUtils.isNotBlank(roleLabel)) {
            int count = SysRoleDao.countTableByBean(connection, new SysRole().setRoleLabel(roleLabel));
            if (count == 0) {
                return JsonResponse.getSuccessJson();
            }
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/allRoleList", slave = "slave1")
    public JsonResponse allRoleList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<RoleVO> allRoleList = SysRoleDao.selectAllRoleVO(connection);
        return JsonResponse.getSuccessJson(allRoleList);
    }

    @RequestUri(value = "/roleList", slave = "slave1")
    public JsonResponse roleList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysRoleVO vo = getRequestParam(request, SysRoleVO.class);
        if (vo != null) {
            PageBean<SysRole> pageBean = SysRoleDao.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/roleAdd")
    public JsonResponse roleAdd(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysRole bean = getRequestParam(request, SysRole.class);
        if (checkValid(bean)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            SysRoleDao.insertIntoTable(connection, bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysRole bean) {
        return bean != null && bean.getRoleName() != null && bean.getRoleLabel() != null;
    }

    @RequestUri("/roleUpdate")
    public JsonResponse roleUpdate(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysRole bean = getRequestParam(request, SysRole.class);
        if (checkValid(bean) && bean.getId() != null) {
            bean.setUpdateTime(DateUtil.getNow());
            SysRoleDao.updateTableById(connection, bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/roleDetail", slave = "slave1")
    public JsonResponse roleDetail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysRole bean = getRequestParam(request, SysRole.class);
        if (bean != null && bean.getId() != null) {
            SysRole detail = SysRoleDao.selectTableById(connection, bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    // privilege
    @RequestUri(value = "/privilegeList", slave = "slave1")
    public JsonResponse privilegeList(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysPrivilegeVO vo = getRequestParam(request, SysPrivilegeVO.class);
        if (vo != null) {
            PageBean<SysPrivilege> pageBean = SysPrivilegeDao.selectTableForCustomPage(connection, vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/privilegeAdd")
    public JsonResponse privilegeAdd(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysPrivilege bean = getRequestParam(request, SysPrivilege.class);
        if (checkValid(bean)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            SysPrivilegeDao.insertIntoTable(connection, bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysPrivilege bean) {
        return bean != null && bean.getPrivilegeLabel() != null && bean.getPrivilegeName() != null && bean.getParentId() != null;
    }

    @RequestUri("/privilegeUpdate")
    public JsonResponse privilegeUpdate(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysPrivilege bean = getRequestParam(request, SysPrivilege.class);
        if (checkValid(bean) && bean.getId() != null) {
            bean.setUpdateTime(DateUtil.getNow());
            SysPrivilegeDao.updateTableById(connection, bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/privilegeDetail", slave = "slave1")
    public JsonResponse privilegeDetail(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysPrivilege bean = getRequestParam(request, SysPrivilege.class);
        if (bean != null && bean.getId() != null) {
            SysPrivilege detail = SysPrivilegeDao.selectTableById(connection, bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/allPrivilege", slave = "slave1")
    public JsonResponse allPrivilege(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<IdNameVO> privilegeVOList = SysPrivilegeDao.selectAllPrivilege(connection);
        return JsonResponse.getSuccessJson(privilegeVOList);
    }

    @RequestUri(value = "/getPrivilegeTree", slave = "slave1")
    public JsonResponse getPrivilegeTree(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUser user = getSessionUser(connection, jedis, request, response);
        if (user != null) {
            List<TreeVO> privilegeTree = SYS_USER_SERVICE.selectAuthorityPrivilegeTree(connection, user.getGid());
            return JsonResponse.getSuccessJson(privilegeTree);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/getMenuTree", slave = "slave1")
    public JsonResponse getMenuTree(Connection connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUser user = getSessionUser(connection, jedis, request, response);
        if (user != null) {
            List<TreeVO> menuTree = SYS_USER_SERVICE.selectAuthorityMenuTree(connection, user.getGid());
            return JsonResponse.getSuccessJson(menuTree);
        }
        return JsonResponse.getFailedJson();
    }
}