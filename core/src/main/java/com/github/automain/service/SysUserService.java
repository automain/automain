package com.github.automain.service;

import com.github.automain.bean.SysMenu;
import com.github.automain.common.container.ServiceDaoContainer;
import com.github.automain.vo.MenuVO;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SysUserService implements ServiceDaoContainer {

    public List<MenuVO> selectAuthorityMenu(Connection connection, String userGid) throws SQLException {
        List<SysMenu> menuList = SYS_MENU_DAO.selectAuthorityMenu(connection, userGid);
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

}