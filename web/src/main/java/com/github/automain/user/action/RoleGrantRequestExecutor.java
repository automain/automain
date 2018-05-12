package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.user.bean.TbRequestMapping;
import com.github.automain.user.bean.TbRole;
import com.github.automain.user.bean.TbRoleRequestMapping;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RoleGrantRequestExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long roleId = getLong("roleId", request, 0L);
        Long requestMappingId = getLong("requestMappingId", request, 0L);
        TbRequestMapping requestMapping = TB_REQUEST_MAPPING_SERVICE.selectTableById(connection, requestMappingId);
        if (requestMapping != null) {
            TbRole role = TB_ROLE_SERVICE.selectTableById(connection, roleId);
            if (role != null) {
                TbRoleRequestMapping roleRequestMappingParam = new TbRoleRequestMapping();
                roleRequestMappingParam.setRequestMappingId(requestMappingId);
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
                setJsonResult(request, CODE_FAIL, "未找到权限");
            }
        } else {
            setJsonResult(request, CODE_FAIL, "未找到用户");
        }
        return null;
    }
}
