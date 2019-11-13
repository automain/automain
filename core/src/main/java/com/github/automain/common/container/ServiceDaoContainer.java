package com.github.automain.common.container;

import com.github.automain.dao.SysDictionaryDao;
import com.github.automain.dao.SysMenuDao;
import com.github.automain.dao.SysPrivilegeDao;
import com.github.automain.dao.SysRoleDao;
import com.github.automain.dao.SysRoleMenuDao;
import com.github.automain.dao.SysRolePrivilegeDao;
import com.github.automain.dao.SysUserDao;
import com.github.automain.dao.SysUserRoleDao;
import com.github.automain.dao.TestDao;
import com.github.automain.service.SysDictionaryService;
import com.github.automain.service.SysMenuService;
import com.github.automain.service.SysPrivilegeService;
import com.github.automain.service.SysRoleService;
import com.github.automain.service.SysUserService;
import com.github.automain.service.TestService;

public interface ServiceDaoContainer {

    // ==================== frame start ==================
    SysDictionaryService SYS_DICTIONARY_SERVICE = new SysDictionaryService();
    SysDictionaryDao SYS_DICTIONARY_DAO = new SysDictionaryDao();
    TestService TEST_SERVICE = new TestService();
    TestDao TEST_DAO = new TestDao();
    SysUserService SYS_USER_SERVICE = new SysUserService();
    SysUserDao SYS_USER_DAO = new SysUserDao();
    SysRoleService SYS_ROLE_SERVICE = new SysRoleService();
    SysRoleDao SYS_ROLE_DAO = new SysRoleDao();
    SysUserRoleDao SYS_USER_ROLE_DAO = new SysUserRoleDao();
    SysMenuService SYS_MENU_SERVICE = new SysMenuService();
    SysMenuDao SYS_MENU_DAO = new SysMenuDao();
    SysRoleMenuDao SYS_ROLE_MENU_DAO = new SysRoleMenuDao();
    SysPrivilegeService SYS_PRIVILEGE_SERVICE = new SysPrivilegeService();
    SysPrivilegeDao SYS_PRIVILEGE_DAO = new SysPrivilegeDao();
    SysRolePrivilegeDao SYS_ROLE_PRIVILEGE_DAO = new SysRolePrivilegeDao();
    // ==================== frame end ====================

    // ==================== business start ==================


    // ==================== business end ====================
}
