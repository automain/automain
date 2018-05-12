package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.user.bean.TbUser;
import com.github.automain.util.EncryptUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserUpdatePwdExecutor extends BaseExecutor {

    @Override
    protected boolean checkAuthority(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return checkUserLogin(jedis, request, response);
    }

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long userId = getLong("userId", request, 0L);
        TbUser user = TB_USER_SERVICE.selectTableById(connection, userId);
        if (user != null) {
            String originPwd = getString("originPwd", request, "");
            String originMd5 = EncryptUtil.MD5(originPwd.getBytes(PropertiesUtil.DEFAULT_CHARSET));
            String newPwd = getString("newPwd", request, "");
            String newMd5 = EncryptUtil.MD5(newPwd.getBytes(PropertiesUtil.DEFAULT_CHARSET));
            if (user.getPasswordMd5().equals(originMd5)) {
                TbUser newUser = new TbUser();
                newUser.setUserId(userId);
                newUser.setPasswordMd5(newMd5);
                TB_USER_SERVICE.updateTable(connection, newUser, false);
                setJsonResult(request, CODE_SUCCESS, "修改成功");
            } else {
                setJsonResult(request, CODE_FAIL, "原密码不正确");
            }
        } else {
            setJsonResult(request, CODE_FAIL, "用户不存在");
        }
        return null;
    }
}
