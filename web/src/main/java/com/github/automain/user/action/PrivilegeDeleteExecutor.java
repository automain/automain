package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.user.bean.TbPrivilege;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestUrl("/privilege/delete")
public class PrivilegeDeleteExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Long> deleteCheck = getLongValues("deleteCheck", request);
        if (deleteCheck != null && !deleteCheck.isEmpty()) {
            TB_PRIVILEGE_SERVICE.softDeleteTableByIdList(connection, deleteCheck);
            connection.closeReadUseWrite();
            Long privilegeId = deleteCheck.get(0);
            TbPrivilege privilege = TB_PRIVILEGE_SERVICE.selectTableById(connection, privilegeId);
            if (privilege != null) {
                Long parentId = privilege.getParentId();
                if (!parentId.equals(0L)) {
                    int children = TB_PRIVILEGE_SERVICE.countPrivilegeByParentId(connection, parentId);
                    if (children == 0) {
                        TbPrivilege bean = new TbPrivilege();
                        bean.setPrivilegeId(parentId);
                        bean.setIsLeaf(1);
                        TB_PRIVILEGE_SERVICE.updateTable(connection, bean, false);
                    }
                }
            }
        }
        setJsonResult(request, CODE_SUCCESS, "删除成功");
        return null;
    }
}