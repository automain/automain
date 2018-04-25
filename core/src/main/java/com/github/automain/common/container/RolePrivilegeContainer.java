package com.github.automain.common.container;

import com.github.automain.common.vo.MenuVO;
import com.github.automain.user.bean.TbMenu;
import com.github.automain.user.bean.TbRole;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.ConnectionPool;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public class RolePrivilegeContainer implements ServiceContainer {

    // 用户->用户访问权限
    private static Map<Long, Set<String>> USER_PRIVILEGE_MAP = null;
    // 用户->用户可访问菜单
    private static Map<Long, List<MenuVO>> USER_MENU_MAP = null;
    // 用户->用户所有角色标识
    private static Map<Long, Set<String>> USER_ROLE_LABEL_MAP = null;
    // 角色标识->该角色所有用户
    private static Map<String, Set<Long>> ROLE_LABEL_USER_MAP = null;

    static {
        reloadRolePrivilege();
    }

    public static void reloadRolePrivilege() {
        ConcurrentHashMap<Long, Set<String>> userPrivilegeMap = new ConcurrentHashMap<Long, Set<String>>();
        ConcurrentHashMap<Long, List<MenuVO>> userMenuMap = new ConcurrentHashMap<Long, List<MenuVO>>();
        ConcurrentHashMap<Long, Set<String>> userRoleLabelMap = new ConcurrentHashMap<Long, Set<String>>();
        ConcurrentHashMap<String, Set<Long>> roleLabelUserMap = new ConcurrentHashMap<String, Set<Long>>();
        Map<Long, Set<String>> rolePrivilegeMap = new HashMap<Long, Set<String>>();
        Map<Long, List<TbMenu>> roleMenuMap = new HashMap<Long, List<TbMenu>>();
        Map<Long, String> roleLabelMap = new HashMap<Long, String>();
        Map<Long, Set<Long>> roleUserMap = new HashMap<Long, Set<Long>>();
        Map<Long, Set<Long>> userRoleMap = new HashMap<Long, Set<Long>>();
        ConnectionBean connection = null;
        try {
            connection = ConnectionPool.getConnectionBean(null);
            TbRole roleParam = new TbRole();
            roleParam.setIsDelete(0);
            List<TbRole> roleList = TB_ROLE_SERVICE.selectTableByBean(connection, roleParam);
            // 角色和权限、菜单、用户关系
            for (TbRole role : roleList) {
                Long roleId = role.getRoleId();
                String roleLabel = role.getRoleLabel();
                roleLabelMap.put(roleId, roleLabel);

                Set<String> requestUrlSet = TB_ROLE_REQUEST_MAPPING_SERVICE.selectRequestUrlByRoleId(connection, roleId);
                rolePrivilegeMap.put(roleId, requestUrlSet);

                List<TbMenu> menuList = TB_MENU_SERVICE.selectTableByRoleId(connection, roleId);
                roleMenuMap.put(roleId, menuList);

                Set<Long> userSet = TB_USER_ROLE_SERVICE.selectUserIdByRoleId(connection, roleId);
                roleUserMap.put(roleId, userSet);
                roleLabelUserMap.put(roleLabel, userSet);
            }
            // 反转角色->用户为用户->角色
            for (Map.Entry<Long, Set<Long>> entry : roleUserMap.entrySet()) {
                Set<Long> userIdSet = entry.getValue();
                Long roleId = entry.getKey();
                String roleLabel = roleLabelMap.get(roleId);
                for (Long userId : userIdSet) {
                    Set<Long> roleSet = userRoleMap.get(userId);
                    Set<String> roleLabelSet = userRoleLabelMap.get(userId);
                    if (roleSet == null) {
                        roleSet = new HashSet<Long>();
                        roleLabelSet = new HashSet<String>();
                    }
                    roleSet.add(roleId);
                    roleLabelSet.add(roleLabel);
                    userRoleMap.put(userId, roleSet);
                    userRoleLabelMap.put(userId, roleLabelSet);
                }
            }
            // 循环用户，匹配该用户所有权限、菜单
            Set<String> requestUrlSet = null;
            List<TbMenu> menuList = null;
            for (Map.Entry<Long, Set<Long>> entry : userRoleMap.entrySet()) {
                Long userId = entry.getKey();
                Set<Long> roleSet = entry.getValue();
                requestUrlSet = new HashSet<String>();
                menuList = new ArrayList<TbMenu>();
                for (Long roleId : roleSet) {
                    requestUrlSet.addAll(rolePrivilegeMap.get(roleId));
                    menuList.addAll(roleMenuMap.get(roleId));
                }
                userPrivilegeMap.put(userId, requestUrlSet);
                userMenuMap.put(userId, getMenuVOList(menuList));
            }
            USER_PRIVILEGE_MAP = userPrivilegeMap;
            USER_MENU_MAP = userMenuMap;
            USER_ROLE_LABEL_MAP = userRoleLabelMap;
            ROLE_LABEL_USER_MAP = roleLabelUserMap;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ConnectionPool.closeConnectionBean(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<MenuVO> getMenuVOList(List<TbMenu> menuList) {
        Map<Long, Set<TbMenu>> parentIdMap = new HashMap<Long, Set<TbMenu>>();
        for (TbMenu menu : menuList) {
            Long parentId = menu.getParentId();
            Set<TbMenu> childrenMenuSet = parentIdMap.get(parentId);
            if (childrenMenuSet == null) {
                childrenMenuSet = new TreeSet<TbMenu>();
            }
            childrenMenuSet.add(menu);
            parentIdMap.put(parentId, childrenMenuSet);
        }
        MenuVO topVO = new MenuVO();
        topVO.setId(0L);
        combineMenuVO(topVO, parentIdMap);
        return topVO.getChildren() == null ? new ArrayList<MenuVO>(1) : topVO.getChildren();
    }

    private static void combineMenuVO(MenuVO parentVO, Map<Long, Set<TbMenu>> parentIdMap) {
        Set<TbMenu> childrenSet = parentIdMap.get(parentVO.getId());
        if (childrenSet != null && !childrenSet.isEmpty()){
            List<MenuVO> children = new ArrayList<MenuVO>();
            for (TbMenu menu : childrenSet) {
                MenuVO childVO = changeMenuToMenuVO(menu);
                children.add(childVO);
                combineMenuVO(childVO,parentIdMap);
            }
            parentVO.setChildren(children);
        }
    }

    private static MenuVO changeMenuToMenuVO(TbMenu menu) {
        MenuVO vo = new MenuVO();
        vo.setId(menu.getMenuId());
        vo.setName(menu.getMenuName());
        vo.setIcon(menu.getMenuIcon());
        vo.setLink(menu.getRequestUrl());
        vo.setIsSpread(menu.getIsSpread());
        return vo;
    }

    /**
     * 根据角色标识获取该角色所有用户ID
     *
     * @param roleLabel
     * @return
     */
    public static Set<Long> getUserIdByRoleLabel(String roleLabel) {
        return ROLE_LABEL_USER_MAP.get(roleLabel);
    }

    /**
     * 根据用户ID获取该用户所有角色标识
     *
     * @param userId
     * @return
     */
    public static Set<String> getRoleLabelByUserId(Long userId) {
        return USER_ROLE_LABEL_MAP.get(userId);
    }

    /**
     * 根据用户ID获取该用户所有可访问的路径
     *
     * @param userId
     * @return
     */
    public static Set<String> getRequestUrlSetByUserId(Long userId) {
        return USER_PRIVILEGE_MAP.get(userId);
    }

    /**
     * 根据用户ID获取该用户所有可访问菜单
     *
     * @param userId
     * @return
     */
    public static List<MenuVO> getMenuByUserId(Long userId) {
        return USER_MENU_MAP.get(userId);
    }

}
