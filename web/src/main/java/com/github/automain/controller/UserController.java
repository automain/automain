package com.github.automain.controller;

import com.github.automain.bean.SysFile;
import com.github.automain.bean.SysMenu;
import com.github.automain.bean.SysPrivilege;
import com.github.automain.bean.SysRole;
import com.github.automain.bean.SysRoleMenu;
import com.github.automain.bean.SysRolePrivilege;
import com.github.automain.bean.SysUser;
import com.github.automain.bean.SysUserRole;
import com.github.automain.common.annotation.RequestUri;
import com.github.automain.common.bean.JsonResponse;
import com.github.automain.common.controller.BaseController;
import com.github.automain.dao.SysFileDao;
import com.github.automain.dao.SysMenuDao;
import com.github.automain.dao.SysPrivilegeDao;
import com.github.automain.dao.SysRoleDao;
import com.github.automain.dao.SysRoleMenuDao;
import com.github.automain.dao.SysRolePrivilegeDao;
import com.github.automain.dao.SysUserDao;
import com.github.automain.dao.SysUserRoleDao;
import com.github.automain.util.CaptchaUtil;
import com.github.automain.util.DateUtil;
import com.github.automain.util.EncryptUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.RedisUtil;
import com.github.automain.vo.ChangePasswordVO;
import com.github.automain.vo.IdNameVO;
import com.github.automain.vo.LoginUserVO;
import com.github.automain.vo.MenuVO;
import com.github.automain.vo.RoleDistributeVO;
import com.github.automain.vo.RoleVO;
import com.github.automain.vo.SysMenuVO;
import com.github.automain.vo.SysPrivilegeVO;
import com.github.automain.vo.SysRoleVO;
import com.github.automain.vo.SysUserAddVO;
import com.github.automain.vo.SysUserVO;
import com.github.automain.vo.TreeVO;
import com.github.fastjdbc.PageBean;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public JsonResponse getCaptcha(Jedis jedis) throws Exception {
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
    public JsonResponse login(Jedis jedis, HttpServletResponse response, LoginUserVO user) throws Exception {
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
                    SysUser sysUser = SysUserDao.selectOneTableByBean(new SysUser().setUserName(userName));
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
                            userCacheMap.put("headImgGid", sysUser.getHeadImgGid() == null ? "" : sysUser.getHeadImgGid());
                            userCacheMap.put("expireTime", String.valueOf(cacheExpireTime));
                            String userPrivilegeKey = "userPrivilege:" + userGid;
                            Set<String> privilegeSet = SysPrivilegeDao.selectUserPrivilege(userGid);
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
                            List<MenuVO> menuData = SYS_USER_SERVICE.selectAuthorityMenu(userGid);
                            Map<String, Object> map = new HashMap<String, Object>(2);
                            map.put("menuData", menuData);
                            map.put("privilege", privilegeSet);
                            map.put("realName", sysUser.getRealName());
                            if (sysUser.getHeadImgGid() != null) {
                                SysFile file = SysFileDao.selectTableByGid(new SysFile().setGid(sysUser.getHeadImgGid()));
                                if (file != null) {
                                    map.put("headImg", "/uploads" + file.getFilePath());
                                }
                            }
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
    public JsonResponse logout(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUser user = getSessionUser(jedis, request, response);
        if (user != null) {
            String userGid = user.getGid();
            String userCacheKey = "user:" + userGid;
            String userPrivilegeKey = "userPrivilege:" + userGid;
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
    public JsonResponse checkUserExist(HttpServletRequest request) throws Exception {
         String userName = request.getParameter("userName");
         String gid = request.getParameter("gid");
         if (StringUtils.isNotBlank(userName)) {
             if (SysUserDao.checkUserNameUseable(userName, gid)) {
                 return JsonResponse.getSuccessJson();
             }
         }
         return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/userList", slave = "slave1")
    public JsonResponse userList(SysUserVO vo) throws Exception {
        if (vo != null) {
            PageBean<SysUserAddVO> pageBean = SysUserDao.selectTableForCustomPage(vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/userAdd")
    public JsonResponse userAdd(SysUserAddVO bean) throws Exception {
        if (checkValid(bean) && bean.getUserName() != null && bean.getPassword() != null && bean.getPassword().equals(bean.getPassword2())) {
            String gid = UUID.randomUUID().toString();
            int now = DateUtil.getNow();
            SysUser user = new SysUser()
                    .setGid(gid)
                    .setPasswordMd5(EncryptUtil.MD5(bean.getPassword().getBytes(PropertiesUtil.DEFAULT_CHARSET)))
                    .setUpdateTime(now)
                    .setCreateTime(now)
                    .setIsValid(1)
                    .setUserName(bean.getUserName())
                    .setEmail(bean.getEmail())
                    .setPhone(bean.getPhone())
                    .setRealName(bean.getRealName())
                    .setHeadImgGid(bean.getHeadImgGid());
            SysUserDao.insertIntoTable(user);
            List<Integer> idList = SysRoleDao.selectRoleIdByLabelList(bean.getUserRoleList());
            List<SysUserRole> userRoleList = new ArrayList<SysUserRole>(idList.size());
            for (Integer id : idList) {
                userRoleList.add(new SysUserRole().setUserGid(gid).setRoleId(id).setIsValid(1).setCreateTime(now).setUpdateTime(now));
            }
            SysUserRoleDao.batchInsertIntoTable(userRoleList);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysUserAddVO bean) {
        return bean != null && bean.getRealName() != null && bean.getPhone() != null && bean.getEmail() != null;
    }

    @RequestUri("/userUpdate")
    public JsonResponse userUpdate(SysUserAddVO bean) throws Exception {
        if (checkValid(bean) && bean.getGid() != null) {
            String gid = bean.getGid();
            SysUser sysUser = SysUserDao.selectTableByGid(new SysUser().setGid(gid));
            if (sysUser != null) {
                String oldHeadImgGid = sysUser.getHeadImgGid();
                sysUser.setUpdateTime(DateUtil.getNow())
                        .setUserName(bean.getUserName())
                        .setEmail(bean.getEmail())
                        .setPhone(bean.getPhone())
                        .setRealName(bean.getRealName())
                        .setHeadImgGid(bean.getHeadImgGid());
                SysUserDao.updateTableByGid(sysUser, true);
                if (oldHeadImgGid != null && !oldHeadImgGid.equals(bean.getHeadImgGid())) {
                    SysFileDao.softDeleteTableByGid(new SysFile().setGid(oldHeadImgGid));
                }

                SysUserRoleDao.softDeleteRoleByUserGid(gid);
                List<Integer> idList = SysRoleDao.selectRoleIdByLabelList(bean.getUserRoleList());
                List<SysUserRole> userRoleList = new ArrayList<SysUserRole>(idList.size());
                int now = DateUtil.getNow();
                for (Integer id : idList) {
                    userRoleList.add(new SysUserRole().setUserGid(gid).setRoleId(id).setIsValid(1).setCreateTime(now).setUpdateTime(now));
                }
                SysUserRoleDao.batchInsertIntoTable(userRoleList);
                return JsonResponse.getSuccessJson();
            }
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/userDetail", slave = "slave1")
    public JsonResponse userDetail(SysUser bean) throws Exception {
        if (bean != null && bean.getGid() != null) {
            String gid = bean.getGid();
            SysUserAddVO detail = SysUserDao.selectUserVOByGid(gid);
            if (detail != null) {
                detail.setUserRoleList(SysUserRoleDao.selectUserRoleLabelList(gid));
            }
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/userBaseInfo", slave = "slave1")
    public JsonResponse userBaseInfo(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SysUser user = getSessionUser(jedis, request, response);
        if (user != null) {
            SysUserAddVO detail = SysUserDao.selectUserVOByGid(user.getGid());
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/userBaseInfoUpdate")
    public JsonResponse userBaseInfoUpdate(Jedis jedis, HttpServletRequest request, HttpServletResponse response, SysUserAddVO bean) throws Exception {
        SysUser user = getSessionUser(jedis, request, response);
        if (checkValid(bean) && bean.getGid() != null && user != null) {
            SysUser sysUser = SysUserDao.selectTableByGid(user);
            if (sysUser != null) {
                String oldHeadImgGid = sysUser.getHeadImgGid();
                sysUser.setUpdateTime(DateUtil.getNow())
                        .setUserName(bean.getUserName())
                        .setEmail(bean.getEmail())
                        .setPhone(bean.getPhone())
                        .setRealName(bean.getRealName())
                        .setHeadImgGid(bean.getHeadImgGid());
                SysUserDao.updateTableByGid(sysUser, true);
                if (oldHeadImgGid != null && !oldHeadImgGid.equals(bean.getHeadImgGid())) {
                    SysFileDao.softDeleteTableByGid(new SysFile().setGid(oldHeadImgGid));
                }
                return JsonResponse.getSuccessJson();
            }
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/userChangePassword")
    public JsonResponse userChangePassword(Jedis jedis, HttpServletRequest request, HttpServletResponse response, ChangePasswordVO vo) throws Exception {
        SysUser user = getSessionUser(jedis, request, response);
        if (vo != null && vo.getOriPassword() != null && vo.getPassword() != null && vo.getPassword().equals(vo.getPassword2()) && user != null) {
            SysUser sysUser = SysUserDao.selectTableByGid(user);
            if (sysUser != null) {
                if (sysUser.getPasswordMd5().equals(EncryptUtil.MD5(vo.getOriPassword().getBytes(PropertiesUtil.DEFAULT_CHARSET)))) {
                    SysUserDao.updateTableByGid(new SysUser().setGid(sysUser.getGid()).setPasswordMd5(EncryptUtil.MD5(vo.getPassword().getBytes(PropertiesUtil.DEFAULT_CHARSET))), false);
                    return JsonResponse.getSuccessJson();
                } else {
                    return JsonResponse.getFailedJson("原密码错误");
                }
            } else {
                return JsonResponse.getFailedJson("未找到用户");
            }
        }
        return JsonResponse.getFailedJson("操作失败");
    }

    @RequestUri("/userDelete")
    public JsonResponse userDelete(SysUserVO vo) throws Exception {
        if (vo != null && CollectionUtils.isNotEmpty(vo.getGidList())) {
            SysUserDao.softDeleteTableByGidList(vo.getGidList());
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    // menu
    @RequestUri(value = "/menuList", slave = "slave1")
    public JsonResponse menuList(SysMenuVO vo) throws Exception {
        if (vo != null) {
            PageBean<SysMenu> pageBean = SysMenuDao.selectTableForCustomPage(vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/menuAdd")
    public JsonResponse menuAdd(SysMenu bean) throws Exception {
        if (checkValid(bean)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            SysMenuDao.insertIntoTable(bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysMenu bean) {
        return bean != null && bean.getMenuName() != null && bean.getMenuIcon() != null && bean.getParentId() != null && bean.getSequenceNumber() != null;
    }

    @RequestUri("/menuUpdate")
    public JsonResponse menuUpdate(SysMenu bean) throws Exception {
        if (checkValid(bean) && bean.getId() != null) {
            bean.setUpdateTime(DateUtil.getNow());
            SysMenuDao.updateTableById(bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/menuDetail", slave = "slave1")
    public JsonResponse menuDetail(SysMenu bean) throws Exception {
        if (bean != null && bean.getId() != null) {
            SysMenu detail = SysMenuDao.selectTableById(bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/allValidMenu", slave = "slave1")
    public JsonResponse allValidMenu() throws Exception {
        List<IdNameVO> menuVOList = SysMenuDao.allValidMenu();
        return JsonResponse.getSuccessJson(menuVOList);
    }

    // role
    @RequestUri(value = "/checkRoleExist", slave = "slave1")
    public JsonResponse checkRoleExist(HttpServletRequest request) throws Exception {
        String roleLabel = request.getParameter("roleLabel");
        String idStr = request.getParameter("id");
        Integer id = Integer.valueOf("null".equals(idStr) ? "0" : idStr);
        if (StringUtils.isNotBlank(roleLabel)) {
            if (SysRoleDao.checkRoleLabelUseable(roleLabel, id)) {
                return JsonResponse.getSuccessJson();
            }
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/allRoleList", slave = "slave1")
    public JsonResponse allRoleList() throws Exception {
        List<RoleVO> allRoleList = SysRoleDao.selectAllRoleVO();
        return JsonResponse.getSuccessJson(allRoleList);
    }

    @RequestUri(value = "/roleList", slave = "slave1")
    public JsonResponse roleList(SysRoleVO vo) throws Exception {
        if (vo != null) {
            PageBean<SysRole> pageBean = SysRoleDao.selectTableForCustomPage(vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/roleAdd")
    public JsonResponse roleAdd(SysRole bean) throws Exception {
        if (checkValid(bean)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            SysRoleDao.insertIntoTable(bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysRole bean) {
        return bean != null && bean.getRoleName() != null && bean.getRoleLabel() != null;
    }

    @RequestUri("/roleUpdate")
    public JsonResponse roleUpdate(SysRole bean) throws Exception {
        if (checkValid(bean) && bean.getId() != null) {
            bean.setUpdateTime(DateUtil.getNow());
            SysRoleDao.updateTableById(bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/roleDetail", slave = "slave1")
    public JsonResponse roleDetail(SysRole bean) throws Exception {
        if (bean != null && bean.getId() != null) {
            SysRole detail = SysRoleDao.selectTableById(bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    // privilege
    @RequestUri(value = "/privilegeList", slave = "slave1")
    public JsonResponse privilegeList(SysPrivilegeVO vo) throws Exception {
        if (vo != null) {
            PageBean<SysPrivilege> pageBean = SysPrivilegeDao.selectTableForCustomPage(vo);
            return JsonResponse.getSuccessJson(pageBean);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/privilegeAdd")
    public JsonResponse privilegeAdd(SysPrivilege bean) throws Exception {
        if (checkValid(bean)) {
            bean.setUpdateTime(DateUtil.getNow());
            bean.setCreateTime(bean.getUpdateTime());
            SysPrivilegeDao.insertIntoTable(bean);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    private boolean checkValid(SysPrivilege bean) {
        return bean != null && bean.getPrivilegeLabel() != null && bean.getPrivilegeName() != null && bean.getParentId() != null;
    }

    @RequestUri("/privilegeUpdate")
    public JsonResponse privilegeUpdate(SysPrivilege bean) throws Exception {
        if (checkValid(bean) && bean.getId() != null) {
            bean.setUpdateTime(DateUtil.getNow());
            SysPrivilegeDao.updateTableById(bean, false);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/privilegeDetail", slave = "slave1")
    public JsonResponse privilegeDetail(SysPrivilege bean) throws Exception {
        if (bean != null && bean.getId() != null) {
            SysPrivilege detail = SysPrivilegeDao.selectTableById(bean);
            return JsonResponse.getSuccessJson(detail);
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri(value = "/allPrivilege", slave = "slave1")
    public JsonResponse allPrivilege() throws Exception {
        List<IdNameVO> privilegeVOList = SysPrivilegeDao.selectAllPrivilege();
        return JsonResponse.getSuccessJson(privilegeVOList);
    }

    @RequestUri(value = "/getPrivilegeTree", label = "admin", slave = "slave1")
    public JsonResponse getPrivilegeTree(HttpServletRequest request) throws Exception {
        Integer id = Integer.valueOf(request.getParameter("id"));
        List<TreeVO> privilegeTree = SYS_USER_SERVICE.selectPrivilegeTree();
        List<Integer> privilegeList = SysRolePrivilegeDao.selectCheckedPrivilegeList(id);
        Map<String, Object> result = new HashMap<String, Object>(2);
        result.put("privilegeTree", privilegeTree);
        result.put("privilegeList", privilegeList);
        return JsonResponse.getSuccessJson(result);
    }

    @RequestUri(value = "/getMenuTree", label = "admin", slave = "slave1")
    public JsonResponse getMenuTree(HttpServletRequest request) throws Exception {
        Integer id = Integer.valueOf(request.getParameter("id"));
        List<TreeVO> menuTree = SYS_USER_SERVICE.selectMenuTree();
        List<Integer> menuList = SysRoleMenuDao.selectCheckedMenuList(id);
        Map<String, Object> result = new HashMap<String, Object>(2);
        result.put("menuTree", menuTree);
        result.put("menuList", menuList);
        return JsonResponse.getSuccessJson(result);
    }

    @RequestUri("/setRolePrivilege")
    public JsonResponse setRolePrivilege(RoleDistributeVO vo) throws Exception {
        if (vo != null && vo.getRoleId() != null) {
            Integer roleId = vo.getRoleId();
            SysRolePrivilegeDao.softDeleteByRoleId(roleId);
            List<Integer> privilegeList = vo.getDistributeIdList();
            List<SysRolePrivilege> rolePrivilegeList = new ArrayList<SysRolePrivilege>(privilegeList.size());
            int now = DateUtil.getNow();
            for (Integer id : privilegeList) {
                rolePrivilegeList.add(new SysRolePrivilege().setPrivilegeId(id).setRoleId(roleId).setIsValid(1).setCreateTime(now).setUpdateTime(now));
            }
            SysRolePrivilegeDao.batchInsertIntoTable(rolePrivilegeList);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }

    @RequestUri("/setRoleMenu")
    public JsonResponse setRoleMenu(RoleDistributeVO vo) throws Exception {
        if (vo != null && vo.getRoleId() != null) {
            Integer roleId = vo.getRoleId();
            SysRoleMenuDao.softDeleteByRoleId(roleId);
            List<Integer> menuList = vo.getDistributeIdList();
            List<SysRoleMenu> roleMenuList = new ArrayList<SysRoleMenu>(menuList.size());
            int now = DateUtil.getNow();
            for (Integer id : menuList) {
                roleMenuList.add(new SysRoleMenu().setMenuId(id).setRoleId(roleId).setIsValid(1).setCreateTime(now).setUpdateTime(now));
            }
            SysRoleMenuDao.batchInsertIntoTable(roleMenuList);
            return JsonResponse.getSuccessJson();
        }
        return JsonResponse.getFailedJson();
    }
}