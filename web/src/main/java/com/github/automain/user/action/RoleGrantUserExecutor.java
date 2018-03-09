package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.user.bean.TbRole;
import com.github.automain.user.bean.TbUser;
import com.github.automain.user.bean.TbUserRole;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RoleGrantUserExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long roleId = getLong("roleId", request, 0L);
        Long userId = getLong("userId", request, 0L);
        TbUser user = TB_USER_SERVICE.selectTableById(connection, userId);
        if (user != null) {
            TbRole role = TB_ROLE_SERVICE.selectTableById(connection, roleId);
            if (role != null) {
                TbUserRole userRoleParam = new TbUserRole();
                userRoleParam.setUserId(userId);
                userRoleParam.setRoleId(roleId);
                TbUserRole userRole = TB_USER_ROLE_SERVICE.selectOneTableByBean(connection, userRoleParam);
                if (userRole != null) {
                    if (userRole.getIsDelete().equals(1)){
                        userRole.setIsDelete(0);
                        TB_USER_ROLE_SERVICE.updateTable(connection, userRole);
                        setJsonResult(request, CODE_SUCCESS, "分配成功");
                    } else {
                        setJsonResult(request, CODE_FAIL, "已分配该角色");
                    }
                } else {
                    userRoleParam.setIsDelete(0);
                    TB_USER_ROLE_SERVICE.insertIntoTable(connection, userRoleParam);
                    setJsonResult(request, CODE_SUCCESS, "分配成功");
                }
            } else {
                setJsonResult(request, CODE_FAIL, "未找到角色");
            }
        } else {
            setJsonResult(request, CODE_FAIL, "未找到用户");
        }
        return null;
    }
}
