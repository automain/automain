package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.RequestUrl;
import com.github.automain.user.bean.TbUser;
import com.github.automain.util.UploadUtil;
import com.github.automain.util.mail.MailUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestUrl("/user/update")
public class UserUpdateExecutor extends BaseExecutor {

    @Override
    protected boolean checkAuthority(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbUser user = getSessionUser(jedis, request, response);
        Long userId = getLong("userId", request, 0L);
        return user != null && (userId.equals(user.getUserId()) || super.checkAuthority(jedis, request, response));
    }

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbUser bean = new TbUser();
        bean = bean.beanFromRequest(request);
        TbUser user = TB_USER_SERVICE.selectTableByUserName(connection, bean.getUserName());
        if (user == null || user.getUserId().equals(bean.getUserId())) {
            if (MailUtil.checkEmailExist(bean.getEmail())) {
                TB_USER_SERVICE.updateTable(connection, bean, false);
                Long uploadFileId = getLong("uploadFileId", request, 0L);
                UploadUtil.saveFileRelation(connection, uploadFileId, bean.getUserId(), bean.tableName(), null, 0);
                setJsonResult(request, CODE_SUCCESS, "编辑成功");
            } else {
                setJsonResult(request, CODE_FAIL, "邮箱不存在");
            }
        } else {
            setJsonResult(request, CODE_FAIL, "用户名已存在");
        }
        return null;
    }
}