package com.github.automain.user.view;

import com.alibaba.fastjson.JSON;
import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.common.container.RolePrivilegeContainer;
import com.github.automain.common.vo.MenuVO;
import com.github.automain.common.vo.PrivilegeVO;
import com.github.automain.user.bean.TbMenu;
import com.github.automain.user.bean.TbPrivilege;
import com.github.automain.user.bean.TbRole;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestUrl("/role/forward")
public class RoleForwardExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forwardType = getString("forwardType", request, "");
        String jspPath = null;
        Long roleId = getLong("roleId", request, 0L);
        request.setAttribute("roleId", roleId);
        switch (forwardType) {
            case "add":
                jspPath = "user/role_add";
                break;
            case "update":
                TbRole bean = TB_ROLE_SERVICE.selectTableById(connection, roleId);
                request.setAttribute("bean", bean);
                jspPath = "user/role_update";
                break;
            case "menu":
                request.setAttribute("roleId", roleId);
                List<TbMenu> menuList = TB_MENU_SERVICE.selectAllTable(connection);
                List<MenuVO> menuVOList = RolePrivilegeContainer.getMenuVOList(menuList);
                List<Long> menuIdList = TB_ROLE_MENU_SERVICE.selectMenuIdByRoleId(connection, roleId);
                setCheckMenuStatus(menuVOList, menuIdList);
                request.setAttribute("menuVOList", JSON.toJSONString(menuVOList));
                jspPath = "user/role_menu";
                break;
            case "privilege":
                request.setAttribute("roleId", roleId);
                List<TbPrivilege> privilegeList = TB_PRIVILEGE_SERVICE.selectAllTable(connection);
                List<PrivilegeVO> privilegeVOList = RolePrivilegeContainer.getPrivilegeVOList(privilegeList);
                List<Long> privilegeIdList = TB_ROLE_PRIVILEGE_SERVICE.selectPrivilegeIdByRoleId(connection, roleId);
                setCheckPrivilegeStatus(privilegeVOList, privilegeIdList);
                request.setAttribute("privilegeVOList", JSON.toJSONString(privilegeVOList));
                jspPath = "user/role_privilege";
                break;
            case "user":
                jspPath = "user/role_user";
                break;
            default:
                jspPath = "user/role_tab";
        }
        return jspPath;
    }

    private void setCheckMenuStatus(List<MenuVO> menuVOList, List<Long> menuIdList){
        for (MenuVO vo : menuVOList) {
            if (menuIdList.contains(vo.getId())) {
                vo.setChecked(true);
            } else {
                vo.setChecked(false);
            }
            List<MenuVO> children = vo.getChildren();
            if (children != null && !children.isEmpty()) {
                setCheckMenuStatus(children, menuIdList);
            }
        }
    }

    private void setCheckPrivilegeStatus(List<PrivilegeVO> privilegeVOList, List<Long> privilegeIdList){
        for (PrivilegeVO vo : privilegeVOList) {
            if (privilegeIdList.contains(vo.getId())) {
                vo.setChecked(true);
            } else {
                vo.setChecked(false);
            }
            List<PrivilegeVO> children = vo.getChildren();
            if (children != null && !children.isEmpty()) {
                setCheckPrivilegeStatus(children, privilegeIdList);
            }
        }
    }
}