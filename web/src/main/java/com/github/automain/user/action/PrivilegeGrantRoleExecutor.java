package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.user.bean.TbPrivilege;
import com.github.automain.user.bean.TbRolePrivilege;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequestUrl("/privilege/grant/role")
public class PrivilegeGrantRoleExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long privilegeId = getLong("privilegeId", request, 0L);
        Long roleId = getLong("roleId", request, 0L);
        connection.closeReadUseWrite();
        // 处理父级
        grantPrivilegeRole(connection, privilegeId, roleId);
        // 处理子集
        List<Long> privilegeIdList = new ArrayList<Long>();
        getAllChildPrivilegeId(connection, privilegeId, privilegeIdList);
        for (Long id : privilegeIdList) {
            grantPrivilege(connection, id, roleId);
        }
        setJsonResult(request, CODE_SUCCESS, "分配成功");
        return null;
    }

    private void getAllChildPrivilegeId(ConnectionBean connection, Long privilegeId, List<Long> privilegeIdList) throws SQLException {
        List<Long> privilegeIds = TB_PRIVILEGE_SERVICE.selectPrivilegeIdByParentId(connection, privilegeId);
        if (!privilegeIds.isEmpty()) {
            privilegeIdList.addAll(privilegeIds);
            for (Long id : privilegeIds) {
                getAllChildPrivilegeId(connection, id, privilegeIdList);
            }
        }
    }

    private void grantPrivilegeRole(ConnectionBean connection, Long privilegeId, Long roleId) throws SQLException {
        TbPrivilege privilege = TB_PRIVILEGE_SERVICE.selectTableById(connection, privilegeId);
        if (privilege != null) {
            grantPrivilege(connection, privilegeId, roleId);
            Long parentId = privilege.getParentId();
            if (!parentId.equals(0L)) {
                grantPrivilegeRole(connection, parentId, roleId);
            }
        }
    }

    private void grantPrivilege(ConnectionBean connection, Long privilegeId, Long roleId) throws SQLException {
        TbRolePrivilege paramBean = new TbRolePrivilege();
        paramBean.setPrivilegeId(privilegeId);
        paramBean.setRoleId(roleId);
        TbRolePrivilege newBean = new TbRolePrivilege();
        newBean.setPrivilegeId(privilegeId);
        newBean.setRoleId(roleId);
        newBean.setIsDelete(0);
        TB_ROLE_PRIVILEGE_SERVICE.updateTable(connection, paramBean, newBean, true, false, false);
    }
}
