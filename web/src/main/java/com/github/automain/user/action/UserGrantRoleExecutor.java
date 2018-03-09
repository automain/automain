package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.user.bean.TbUser;
import com.github.automain.user.bean.TbUserRole;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class UserGrantRoleExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long userId = getLong("userId", request, 0L);
        List<Long> roleIdList = getLongValues("roleCheck", request);
        TbUser user = TB_USER_SERVICE.selectTableById(connection, userId);
        if (user != null) {
            TB_USER_ROLE_SERVICE.clearUserRoleByUserId(connection, userId);
            if (roleIdList != null){
                TbUserRole paramBean = new TbUserRole();
                paramBean.setUserId(userId);
                TbUserRole newBean = new TbUserRole();
                newBean.setUserId(userId);
                newBean.setIsDelete(0);
                for (Long roleId : roleIdList) {
                    paramBean.setRoleId(roleId);
                    newBean.setRoleId(roleId);
                    TB_USER_ROLE_SERVICE.updateTable(connection, paramBean, newBean, true, false);
                }
            }
            setJsonResult(request, CODE_SUCCESS, "修改成功");
        } else {
            setJsonResult(request, CODE_FAIL, "未找到用户");
        }
        return null;
    }
}
