package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.RequestUrl;
import com.github.automain.user.bean.TbRole;
import com.github.automain.user.bean.TbRoleRequestMapping;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/role/grant/request")
public class RoleGrantRequestExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long roleId = getLong("roleId", request, 0L);
        String requestUrl = getString("requestUrl", request);
        TbRole role = TB_ROLE_SERVICE.selectTableById(connection, roleId);
        if (role != null) {
            TbRoleRequestMapping roleRequestMappingParam = new TbRoleRequestMapping();
            roleRequestMappingParam.setRequestUrl(requestUrl);
            roleRequestMappingParam.setRoleId(roleId);
            TbRoleRequestMapping roleRequestMapping = TB_ROLE_REQUEST_MAPPING_SERVICE.selectOneTableByBean(connection, roleRequestMappingParam);
            if (roleRequestMapping != null) {
                if (roleRequestMapping.getIsDelete().equals(1)){
                    roleRequestMapping.setIsDelete(0);
                    TB_ROLE_REQUEST_MAPPING_SERVICE.updateTable(connection, roleRequestMapping, false);
                    setJsonResult(request, CODE_SUCCESS, "分配成功");
                } else {
                    setJsonResult(request, CODE_FAIL, "已分配该权限");
                }
            } else {
                roleRequestMappingParam.setIsDelete(0);
                TB_ROLE_REQUEST_MAPPING_SERVICE.insertIntoTable(connection, roleRequestMappingParam);
                setJsonResult(request, CODE_SUCCESS, "分配成功");
            }
        } else {
            setJsonResult(request, CODE_FAIL, "未找到角色");
        }
        return null;
    }
}
