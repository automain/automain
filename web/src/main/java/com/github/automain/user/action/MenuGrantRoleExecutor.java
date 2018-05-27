package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.user.bean.TbMenu;
import com.github.automain.user.bean.TbRoleMenu;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequestUrl("/menu/grant/role")
public class MenuGrantRoleExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long menuId = getLong("menuId", request, 0L);
        Long roleId = getLong("roleId", request, 0L);
        connection.closeReadUseWrite();
        // 处理父级
        grantMenuRole(connection, menuId, roleId);
        // 处理子集
        List<Long> menuIdList = new ArrayList<Long>();
        getAllChildMenuId(connection, menuId, menuIdList);
        for (Long id : menuIdList) {
            grantMenu(connection, id, roleId);
        }
        setJsonResult(request, CODE_SUCCESS, "分配成功");
        return null;
    }

    private void getAllChildMenuId(ConnectionBean connection, Long menuId, List<Long> menuIdList) throws SQLException {
        List<Long> menuIds = TB_MENU_SERVICE.selectMenuIdByParentId(connection, menuId);
        if (!menuIds.isEmpty()) {
            menuIdList.addAll(menuIds);
            for (Long id : menuIds) {
                getAllChildMenuId(connection, id, menuIdList);
            }
        }
    }

    private void grantMenuRole(ConnectionBean connection, Long menuId, Long roleId) throws SQLException {
        TbMenu menu = TB_MENU_SERVICE.selectTableById(connection, menuId);
        if (menu != null) {
            grantMenu(connection, menuId, roleId);
            Long parentId = menu.getParentId();
            if (!parentId.equals(0L)) {
                grantMenuRole(connection, parentId, roleId);
            }
        }
    }

    private void grantMenu(ConnectionBean connection, Long menuId, Long roleId) throws SQLException {
        TbRoleMenu paramBean = new TbRoleMenu();
        paramBean.setMenuId(menuId);
        paramBean.setRoleId(roleId);
        TbRoleMenu newBean = new TbRoleMenu();
        newBean.setMenuId(menuId);
        newBean.setRoleId(roleId);
        newBean.setIsDelete(0);
        TB_ROLE_MENU_SERVICE.updateTable(connection, paramBean, newBean, true, false, false);
    }
}
