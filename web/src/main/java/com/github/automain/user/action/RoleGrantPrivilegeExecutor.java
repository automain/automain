package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.user.bean.TbRole;
import com.github.automain.user.bean.TbRolePrivilege;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestUrl("/role/grant/privilege")
public class RoleGrantPrivilegeExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long roleId = getLong("roleId", request, 0L);
        List<Long> privilegeCheck = getLongValues("privilegeCheck", request);
        TbRole role = TB_ROLE_SERVICE.selectTableById(connection, roleId);
        if (role != null) {
            TB_ROLE_PRIVILEGE_SERVICE.clearRoleByRoleId(connection, roleId);
            if (privilegeCheck != null){
                TbRolePrivilege paramBean = new TbRolePrivilege();
                paramBean.setRoleId(roleId);
                TbRolePrivilege newBean = new TbRolePrivilege();
                newBean.setRoleId(roleId);
                newBean.setIsDelete(0);
                for (Long privilegeId : privilegeCheck) {
                    paramBean.setPrivilegeId(privilegeId);
                    newBean.setPrivilegeId(privilegeId);
                    TB_ROLE_PRIVILEGE_SERVICE.updateTable(connection, paramBean, newBean, true, false, false);
                }
            }
            setJsonResult(request, CODE_SUCCESS, "修改成功");
        } else {
            setJsonResult(request, CODE_FAIL, "未找到用户");
        }
        return null;
    }
}
