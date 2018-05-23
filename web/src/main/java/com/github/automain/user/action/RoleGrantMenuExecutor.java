package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.RequestUrl;
import com.github.automain.user.bean.TbRole;
import com.github.automain.user.bean.TbRoleMenu;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestUrl("/role/grant/menu")
public class RoleGrantMenuExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long roleId = getLong("roleId", request, 0L);
        List<Long> menuCheck = getLongValues("menuCheck", request);
        TbRole role = TB_ROLE_SERVICE.selectTableById(connection, roleId);
        if (role != null) {
            TB_ROLE_MENU_SERVICE.clearRoleByRoleId(connection, roleId);
            if (menuCheck != null){
                TbRoleMenu paramBean = new TbRoleMenu();
                paramBean.setRoleId(roleId);
                TbRoleMenu newBean = new TbRoleMenu();
                newBean.setRoleId(roleId);
                newBean.setIsDelete(0);
                for (Long menuId : menuCheck) {
                    paramBean.setMenuId(menuId);
                    newBean.setMenuId(menuId);
                    TB_ROLE_MENU_SERVICE.updateTable(connection, paramBean, newBean, true, false, false);
                }
            }
            setJsonResult(request, CODE_SUCCESS, "修改成功");
        } else {
            setJsonResult(request, CODE_FAIL, "未找到用户");
        }
        return null;
    }
}
