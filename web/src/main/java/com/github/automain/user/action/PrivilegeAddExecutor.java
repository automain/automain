package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.user.bean.TbPrivilege;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/privilege/add")
public class PrivilegeAddExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbPrivilege bean = new TbPrivilege();
        bean = bean.beanFromRequest(request);
        bean.setIsLeaf(1);
        TB_PRIVILEGE_SERVICE.insertIntoTable(connection, bean);
        Long parentId = bean.getParentId();
        TbPrivilege parent = TB_PRIVILEGE_SERVICE.selectTableById(connection, parentId);
        if (parent != null) {
            TbPrivilege p = new TbPrivilege();
            p.setPrivilegeId(parent.getPrivilegeId());
            p.setIsLeaf(0);
            TB_PRIVILEGE_SERVICE.updateTable(connection, p, false);
        }
        setJsonResult(request, CODE_SUCCESS, "添加成功");
        return null;
    }
}