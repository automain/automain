package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.user.bean.TbUser;
import com.github.automain.util.mail.MailUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

public class UserAddExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbUser bean = new TbUser();
        bean = bean.beanFromRequest(request);
        bean.setCreateTime(new Timestamp(System.currentTimeMillis()));
        bean.setPasswordMd5("e10adc3949ba59abbe56e057f20f883e");//123456
        TbUser user = TB_USER_SERVICE.selectTableByUserName(connection, bean.getUserName());
        if (user == null) {
            if (MailUtil.checkEmailExist(bean.getEmail())) {
                TB_USER_SERVICE.insertIntoTable(connection, bean);
                setJsonResult(request, CODE_SUCCESS, "添加成功");
            } else {
                setJsonResult(request, CODE_FAIL, "邮箱不存在");
            }
        } else {
            setJsonResult(request, CODE_FAIL, "用户名已存在");
        }
        return null;
    }
}