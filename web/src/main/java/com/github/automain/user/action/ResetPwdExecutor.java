package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.user.bean.TbUser;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResetPwdExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long userId = getLong("userId", request, 0L);
        TbUser user = TB_USER_SERVICE.selectTableById(connection, userId);
        if (user != null) {
            TbUser tbUser = new TbUser();
            tbUser.setUserId(userId);
            tbUser.setPasswordMd5("e10adc3949ba59abbe56e057f20f883e");// 123456
            TB_USER_SERVICE.updateTable(connection, tbUser, false);
            setJsonResult(request, CODE_SUCCESS, "更新成功");
        } else {
            setJsonResult(request, CODE_FAIL, "未找到用户");
        }
        return null;
    }
}
