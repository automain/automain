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

public class SysMenuService implements ServiceDaoContainer {

    public List<MenuVO> authorityMenu(Connection connection) throws SQLException {
        List<SysMenu> menuList = SYS_MENU_DAO.selectTableByBean(connection, new SysMenu().setIsValid(1));
        Map<Integer, List<SysMenu>> menuMap = new HashMap<Integer, List<SysMenu>>();
        for (SysMenu menu : menuList) {
            Integer parentId = menu.getParentId();
            List<SysMenu> list = menuMap.containsKey(parentId) ? menuMap.get(parentId) : new ArrayList<SysMenu>();
            list.add(menu);
            menuMap.put(parentId, list);
        }
        return getChildrenMenu(menuMap, 0);
    }

    private List<MenuVO> getChildrenMenu(Map<Integer, List<SysMenu>> menuMap, Integer id) {
        List<SysMenu> menuList = menuMap.get(id);
        if (CollectionUtils.isNotEmpty(menuList)) {
            List<MenuVO> result = new ArrayList<MenuVO>(menuList.size());
            MenuVO vo = null;
            for (SysMenu menu : menuList) {
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