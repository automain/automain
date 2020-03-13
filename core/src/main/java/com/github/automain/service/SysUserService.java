package com.github.automain.service;

import com.github.automain.bean.SysMenu;
import com.github.automain.bean.SysPrivilege;
import com.github.automain.dao.SysMenuDao;
import com.github.automain.dao.SysPrivilegeDao;
import com.github.automain.util.ServiceContainer;
import com.github.automain.vo.MenuVO;
import com.github.automain.vo.TreeVO;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SysUserService implements ServiceContainer {

    public List<MenuVO> selectAuthorityMenu(String userGid) throws SQLException {
        List<SysMenu> menuList = SysMenuDao.selectAuthorityMenu(userGid);
        Map<Integer, Set<SysMenu>> menuMap = new HashMap<Integer, Set<SysMenu>>();
        for (SysMenu menu : menuList) {
            Integer parentId = menu.getParentId();
            Set<SysMenu> set = menuMap.containsKey(parentId) ? menuMap.get(parentId) : new TreeSet<SysMenu>();
            set.add(menu);
            menuMap.put(parentId, set);
        }
        return getChildrenMenu(menuMap, 0);
    }

    private List<MenuVO> getChildrenMenu(Map<Integer, Set<SysMenu>> menuMap, Integer id) {
        Set<SysMenu> menuSet = menuMap.get(id);
        if (CollectionUtils.isNotEmpty(menuSet)) {
            List<MenuVO> result = new ArrayList<MenuVO>(menuSet.size());
            MenuVO vo = null;
            for (SysMenu menu : menuSet) {
                vo = new MenuVO();
                vo.setMenuName(menu.getMenuName());
                vo.setMenuIcon(menu.getMenuIcon());
                vo.setMenuPath(menu.getMenuPath());
                vo.setChildren(getChildrenMenu(menuMap, menu.getId()));
                result.add(vo);
            }
            return result;
        }
        return null;
    }

    public List<TreeVO> selectMenuTree() throws SQLException {
        List<SysMenu> allMenuList = SysMenuDao.selectAllTable();
        Map<Integer, Set<SysMenu>> menuMap = new HashMap<Integer, Set<SysMenu>>();
        for (SysMenu menu : allMenuList) {
            Integer parentId = menu.getParentId();
            Set<SysMenu> set = menuMap.containsKey(parentId) ? menuMap.get(parentId) : new TreeSet<SysMenu>();
            set.add(menu);
            menuMap.put(parentId, set);
        }
        return getChildrenMenuTree(menuMap, 0);
    }

    private List<TreeVO> getChildrenMenuTree(Map<Integer, Set<SysMenu>> menuMap, Integer id) {
        Set<SysMenu> menuSet = menuMap.get(id);
        if (CollectionUtils.isNotEmpty(menuSet)) {
            List<TreeVO> result = new ArrayList<TreeVO>(menuSet.size());
            TreeVO vo = null;
            for (SysMenu menu : menuSet) {
                vo = new TreeVO();
                vo.setId(menu.getId());
                vo.setLabel(menu.getMenuName());
                vo.setChildren(getChildrenMenuTree(menuMap, menu.getId()));
                result.add(vo);
            }
            return result;
        }
        return null;
    }

    public List<TreeVO> selectPrivilegeTree() throws SQLException {
        List<SysPrivilege> allPrivilegeList = SysPrivilegeDao.selectAllTable();
        Map<Integer, Set<SysPrivilege>> privilegeMap = new HashMap<Integer, Set<SysPrivilege>>();
        for (SysPrivilege privilege : allPrivilegeList) {
            Integer parentId = privilege.getParentId();
            Set<SysPrivilege> set = privilegeMap.containsKey(parentId) ? privilegeMap.get(parentId) : new TreeSet<SysPrivilege>();
            set.add(privilege);
            privilegeMap.put(parentId, set);
        }
        return getChildrenPrivilegeTree(privilegeMap, 0);
    }

    private List<TreeVO> getChildrenPrivilegeTree(Map<Integer, Set<SysPrivilege>> privilegeMap, Integer id) {
        Set<SysPrivilege> privilegeSet = privilegeMap.get(id);
        if (CollectionUtils.isNotEmpty(privilegeSet)) {
            List<TreeVO> result = new ArrayList<TreeVO>(privilegeSet.size());
            TreeVO vo = null;
            for (SysPrivilege privilege : privilegeSet) {
                vo = new TreeVO();
                vo.setId(privilege.getId());
                vo.setLabel(privilege.getPrivilegeName());
                vo.setChildren(getChildrenPrivilegeTree(privilegeMap, privilege.getId()));
                result.add(vo);
            }
            return result;
        }
        return null;
    }

}