package com.github.automain.common.container;

public class RolePrivilegeContainer implements ServiceContainer {

//    public static void reloadRolePrivilege(Jedis jedis, ConnectionBean connection) {
//        // 角色ID->角色权限标识
//        Map<Long, Set<String>> rolePrivilegeMap = new HashMap<Long, Set<String>>();
//        // 角色ID->角色菜单
//        Map<Long, List<TbMenu>> roleMenuMap = new HashMap<Long, List<TbMenu>>();
//        // 角色ID->角色标识
//        Map<Long, String> roleLabelMap = new HashMap<Long, String>();
//        // 角色ID->该角色所有用户ID
//        Map<Long, Set<Long>> roleUserMap = new HashMap<Long, Set<Long>>();
//        // 用户ID->该用户所有角色ID
//        Map<Long, Set<Long>> userRoleMap = new HashMap<Long, Set<Long>>();
//        // 用户ID->该用户所有角色标识
//        Map<Long, Set<String>> userRoleLabelMap = new HashMap<Long, Set<String>>();
//        try {
//            TbRole roleParam = new TbRole();
//            roleParam.setIsDelete(0);
//            List<TbRole> roleList = TB_ROLE_SERVICE.selectTableByBean(connection, roleParam);
//            // 角色和权限、菜单、用户关系
//            for (TbRole role : roleList) {
//                Long roleId = role.getRoleId();
//                String roleLabel = role.getRoleLabel();
//                roleLabelMap.put(roleId, roleLabel);
//
//                Set<String> privilegeLabelSet = TB_ROLE_PRIVILEGE_SERVICE.selectPrivilegeLabelByRoleId(connection, roleId);
//                rolePrivilegeMap.put(roleId, privilegeLabelSet);
//
//                List<TbMenu> menuList = TB_MENU_SERVICE.selectTableByRoleId(connection, roleId);
//                roleMenuMap.put(roleId, menuList);
//
//                Set<Long> userSet = TB_USER_ROLE_SERVICE.selectUserIdByRoleId(connection, roleId);
//                roleUserMap.put(roleId, userSet);
//                putRoleLabelUserCache(jedis, roleLabel, userSet);
//            }
//            // 反转角色->用户为用户->角色
//            for (Map.Entry<Long, Set<Long>> entry : roleUserMap.entrySet()) {
//                Set<Long> userIdSet = entry.getValue();
//                Long roleId = entry.getKey();
//                String roleLabel = roleLabelMap.get(roleId);
//                for (Long userId : userIdSet) {
//                    Set<Long> roleSet = userRoleMap.get(userId);
//                    Set<String> roleLabelSet = userRoleLabelMap.get(userId);
//                    if (roleSet == null) {
//                        roleSet = new HashSet<Long>();
//                        roleLabelSet = new HashSet<String>();
//                    }
//                    roleSet.add(roleId);
//                    roleLabelSet.add(roleLabel);
//                    userRoleMap.put(userId, roleSet);
//                    userRoleLabelMap.put(userId, roleLabelSet);
//                }
//            }
//            // 循环用户，匹配该用户所有菜单、权限
//            List<TbMenu> menuList = null;
//            Set<String> privilegeSet = null;
//            for (Map.Entry<Long, Set<Long>> entry : userRoleMap.entrySet()) {
//                Long userId = entry.getKey();
//                Set<Long> roleSet = entry.getValue();
//                menuList = new ArrayList<TbMenu>();
//                privilegeSet = new HashSet<String>();
//                for (Long roleId : roleSet) {
//                    menuList.addAll(roleMenuMap.get(roleId));
//                    privilegeSet.addAll(rolePrivilegeMap.get(roleId));
//                }
//                putUserMenuCache(jedis, userId, getMenuVOList(menuList));
//                putUserPrivilegeCache(jedis, userId, privilegeSet);
//            }
//            putUserRoleLabelCache(jedis, userRoleLabelMap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void putUserMenuCache(Jedis jedis, Long userId, List<MenuVO> menuVOList) {
//        if (!menuVOList.isEmpty()) {
//            String key = "userMenu:" + userId;
//            if (jedis != null) {
//                jedis.set(key, JSON.toJSONString(menuVOList));
//            } else {
//                RedisUtil.setLocalCache(key, menuVOList);
//            }
//        }
//    }
//
//    private static void putUserPrivilegeCache(Jedis jedis, Long userId, Set<String> privilegeSet) {
//        if (!privilegeSet.isEmpty()) {
//            String key = "userPrivilege:" + userId;
//            if (jedis != null) {
//                jedis.del(key);
//                jedis.sadd(key, privilegeSet.toArray(new String[0]));
//            } else {
//                RedisUtil.setLocalCache(key, privilegeSet);
//            }
//        }
//    }
//
//    private static void putRoleLabelUserCache(Jedis jedis, String roleLabel, Set<Long> userSet) {
//        if (!userSet.isEmpty()) {
//            String key = "roleLabelUser:" + roleLabel;
//            if (jedis != null) {
//                int size = userSet.size();
//                String[] value = new String[size];
//                int i = 0;
//                for (Long userId : userSet) {
//                    value[i] = String.valueOf(userId);
//                    i++;
//                }
//                jedis.del(key);
//                jedis.sadd(key, value);
//            } else {
//                RedisUtil.setLocalCache(key, userSet);
//            }
//        }
//    }
//
//    private static void putUserRoleLabelCache(Jedis jedis, Map<Long, Set<String>> userRoleLabelMap) {
//        if (!userRoleLabelMap.isEmpty()) {
//            if (jedis != null) {
//                for (Map.Entry<Long, Set<String>> entry : userRoleLabelMap.entrySet()) {
//                    Long userId = entry.getKey();
//                    Set<String> roleLabelSet = entry.getValue();
//                    String key = "userRoleLabel:" + userId;
//                    jedis.del(key);
//                    jedis.sadd(key, roleLabelSet.toArray(new String[0]));
//                }
//            } else {
//                for (Map.Entry<Long, Set<String>> entry : userRoleLabelMap.entrySet()) {
//                    RedisUtil.setLocalCache("userRoleLabel:" + entry.getKey(), entry.getValue());
//                }
//            }
//        }
//    }
//
//    public static List<MenuVO> getMenuVOList(List<TbMenu> menuList) {
//        Map<Long, Set<TbMenu>> parentIdMap = new HashMap<Long, Set<TbMenu>>();
//        for (TbMenu menu : menuList) {
//            Long parentId = menu.getParentId();
//            Set<TbMenu> childrenMenuSet = parentIdMap.get(parentId);
//            if (childrenMenuSet == null) {
//                childrenMenuSet = new TreeSet<TbMenu>();
//            }
//            childrenMenuSet.add(menu);
//            parentIdMap.put(parentId, childrenMenuSet);
//        }
//        MenuVO topVO = new MenuVO();
//        topVO.setId(0L);
//        combineMenuVO(topVO, parentIdMap);
//        return topVO.getChildren() == null ? new ArrayList<MenuVO>(1) : topVO.getChildren();
//    }
//
//    private static void combineMenuVO(MenuVO parentVO, Map<Long, Set<TbMenu>> parentIdMap) {
//        Set<TbMenu> childrenSet = parentIdMap.get(parentVO.getId());
//        if (childrenSet != null && !childrenSet.isEmpty()) {
//            List<MenuVO> children = new ArrayList<MenuVO>();
//            for (TbMenu menu : childrenSet) {
//                MenuVO childVO = changeMenuToMenuVO(menu);
//                children.add(childVO);
//                combineMenuVO(childVO, parentIdMap);
//            }
//            parentVO.setChildren(children);
//        }
//    }
//
//    private static MenuVO changeMenuToMenuVO(TbMenu menu) {
//        MenuVO vo = new MenuVO();
//        vo.setId(menu.getMenuId());
//        vo.setName(menu.getMenuName());
//        vo.setIcon(menu.getMenuIcon());
//        vo.setLink(menu.getRequestUrl());
//        vo.setIsSpread(menu.getIsSpread());
//        return vo;
//    }
//
//    public static List<PrivilegeVO> getPrivilegeVOList(List<TbPrivilege> privilegeList) {
//        Map<Long, Set<TbPrivilege>> parentIdMap = new HashMap<Long, Set<TbPrivilege>>();
//        for (TbPrivilege privilege : privilegeList) {
//            Long parentId = privilege.getParentId();
//            Set<TbPrivilege> childrenPrivilegeSet = parentIdMap.get(parentId);
//            if (childrenPrivilegeSet == null) {
//                childrenPrivilegeSet = new TreeSet<TbPrivilege>();
//            }
//            childrenPrivilegeSet.add(privilege);
//            parentIdMap.put(parentId, childrenPrivilegeSet);
//        }
//        PrivilegeVO topVO = new PrivilegeVO();
//        topVO.setId(0L);
//        combinePrivilegeVO(topVO, parentIdMap);
//        return topVO.getChildren() == null ? new ArrayList<PrivilegeVO>(1) : topVO.getChildren();
//    }
//
//    private static void combinePrivilegeVO(PrivilegeVO parentVO, Map<Long, Set<TbPrivilege>> parentIdMap) {
//        Set<TbPrivilege> childrenSet = parentIdMap.get(parentVO.getId());
//        if (childrenSet != null && !childrenSet.isEmpty()) {
//            List<PrivilegeVO> children = new ArrayList<PrivilegeVO>();
//            for (TbPrivilege privilege : childrenSet) {
//                PrivilegeVO childVO = changePrivilegeToPrivilegeVO(privilege);
//                children.add(childVO);
//                combinePrivilegeVO(childVO, parentIdMap);
//            }
//            parentVO.setChildren(children);
//        }
//    }
//
//    private static PrivilegeVO changePrivilegeToPrivilegeVO(TbPrivilege privilege) {
//        PrivilegeVO vo = new PrivilegeVO();
//        vo.setId(privilege.getPrivilegeId());
//        vo.setName(privilege.getPrivilegeName());
//        return vo;
//    }
//
//    /**
//     * 根据角色标识获取该角色所有用户ID
//     *
//     * @param jedis
//     * @param roleLabel
//     * @return
//     */
//    public static Set<Long> getUserIdByRoleLabel(Jedis jedis, String roleLabel) {
//        String key = "roleLabelUser:" + roleLabel;
//        if (jedis != null) {
//            Set<String> smembers = jedis.smembers(key);
//            Set<Long> result = new HashSet<Long>(smembers.size());
//            for (String userId : smembers) {
//                result.add(Long.valueOf(userId));
//            }
//            return result;
//        } else {
//            return RedisUtil.getLocalCache(key);
//        }
//    }
//
//    /**
//     * 根据用户ID获取该用户所有角色标识
//     *
//     * @param jedis
//     * @param userId
//     * @return
//     */
//    public static Set<String> getRoleLabelByUserId(Jedis jedis, Long userId) {
//        String key = "userRoleLabel:" + userId;
//        if (jedis != null) {
//            return jedis.smembers(key);
//        } else {
//            return RedisUtil.getLocalCache(key);
//        }
//    }
//
//    /**
//     * 根据用户ID获取该用户所有可访问的路径
//     *
//     * @param jedis
//     * @param userId
//     * @return
//     */
//    public static Set<String> getUrlSetByUserId(Jedis jedis, Long userId) {
//        String key = "userUrl:" + userId;
//        if (jedis != null) {
//            return jedis.smembers(key);
//        } else {
//            return RedisUtil.getLocalCache(key);
//        }
//    }
//
//    /**
//     * 根据用户ID获取该用户所有可访问菜单
//     *
//     * @param jedis
//     * @param userId
//     * @return
//     */
//    public static List<MenuVO> getMenuByUserId(Jedis jedis, Long userId) {
//        String key = "userMenu:" + userId;
//        if (jedis != null) {
//            return JSON.parseArray(jedis.get(key), MenuVO.class);
//        } else {
//            return RedisUtil.getLocalCache(key);
//        }
//    }
//
//    /**
//     * 根据用户ID获取该用户所有权限标识
//     *
//     * @param jedis
//     * @param userId
//     * @return
//     */
//    public static Set<String> getPrivilegeSetByUserId(Jedis jedis, Long userId) {
//        String key = "userPrivilege:" + userId;
//        if (jedis != null) {
//            return jedis.smembers(key);
//        } else {
//            return RedisUtil.getLocalCache(key);
//        }
//    }

}
