package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.user.bean.TbPrivilege;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/privilege/update")
public class PrivilegeUpdateExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbPrivilege bean = new TbPrivilege();
        bean = bean.beanFromRequest(request);
        TbPrivilege privilege = TB_PRIVILEGE_SERVICE.selectTableById(connection, bean.getPrivilegeId());
        if (privilege != null) {
            bean.setParentId(privilege.getParentId());
            bean.setTopId(privilege.getTopId());
            bean.setIsLeaf(privilege.getIsLeaf());
            TB_PRIVILEGE_SERVICE.updateTable(connection, bean, false);
            setJsonResult(request, CODE_SUCCESS, "编辑成功");
        } else {
            setJsonResult(request, CODE_FAIL, "未找到记录");
        }
        return null;
    }
}