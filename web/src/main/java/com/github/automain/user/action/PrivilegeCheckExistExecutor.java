package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.user.bean.TbPrivilege;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/privilege/check/exist")
public class PrivilegeCheckExistExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String privilegeLabel = getString("privilegeLabel", request);
        Long privilegeId = getLong("privilegeId", request, 0L);
        TbPrivilege bean = new TbPrivilege();
        bean.setPrivilegeLabel(privilegeLabel);
        TbPrivilege privilege = TB_PRIVILEGE_SERVICE.selectOneTableByBean(connection, bean);
        if (privilege != null && !privilegeId.equals(privilege.getPrivilegeId())) {
            setJsonResult(request, CODE_FAIL, "权限标识已存在");
        }
        return null;
    }
}
