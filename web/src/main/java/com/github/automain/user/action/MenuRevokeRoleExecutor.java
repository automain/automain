package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.user.bean.TbRoleMenu;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequestUrl("/menu/revoke/role")
public class MenuRevokeRoleExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long menuId = getLong("menuId", request, 0L);
        Long roleId = getLong("roleId", request, 0L);
        connection.closeReadUseWrite();
        // 处理本级
        revokeMenu(connection, menuId, roleId);
        // 处理子集
        List<Long> menuIdList = new ArrayList<Long>();
        getAllChildMenuId(connection, menuId, menuIdList);
        for (Long id : menuIdList) {
            revokeMenu(connection, id, roleId);
        }
        setJsonResult(request, CODE_SUCCESS, "取消成功");
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

    private void revokeMenu(ConnectionBean connection, Long menuId, Long roleId) throws SQLException {
        TbRoleMenu paramBean = new TbRoleMenu();
        paramBean.setMenuId(menuId);
        paramBean.setRoleId(roleId);
        TbRoleMenu newBean = new TbRoleMenu();
        newBean.setMenuId(menuId);
        newBean.setRoleId(roleId);
        newBean.setIsDelete(1);
        TB_ROLE_MENU_SERVICE.updateTable(connection, paramBean, newBean, false, false, false);
    }
}
