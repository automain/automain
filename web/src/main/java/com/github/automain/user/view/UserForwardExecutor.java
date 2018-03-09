package com.github.automain.user.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.upload.bean.TbUploadRelation;
import com.github.automain.user.bean.TbUser;
import com.github.automain.util.UploadUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserForwardExecutor extends BaseExecutor {

    @Override
    protected boolean checkAuthority(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forwardType = getString("forwardType", request, "");
        return checkUserLogin(jedis, request, response) && ("info".equals(forwardType) || "pwd".equals(forwardType) || super.checkAuthority(jedis, request, response));
    }

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forwardType = getString("forwardType", request, "");
        Long userId = getLong("userId", request, 0L);
        request.setAttribute("userId", userId);
        String jspPath = null;
        TbUser sessionUser = getSessionUser(jedis, request, response);
        TbUser user = null;
        if (sessionUser != null) {
            user = TB_USER_SERVICE.selectTableById(connection, sessionUser.getUserId());
        }
        request.setAttribute("user", user);
        switch (forwardType) {
            case "add":
                jspPath = "user/user_add";
                break;
            case "info":
                TbUploadRelation uploadRelation = new TbUploadRelation();
                if (sessionUser != null) {
                    uploadRelation.setRecordTableName(sessionUser.tableName());
                    uploadRelation.setRecordId(sessionUser.getUserId());
                }
                String imgPath = UploadUtil.getLastFile(connection, request, uploadRelation);
                request.setAttribute("imgPath", imgPath);
                jspPath = "user/user_info";
                break;
            case "pwd":
                jspPath = "user/user_pwd";
                break;
            case "role":
                jspPath = "user/user_role";
                break;
            case "update":
                TbUser bean = TB_USER_SERVICE.selectTableById(connection, userId);
                request.setAttribute("bean", bean);
                jspPath = "user/user_update";
                break;
            default:
                jspPath = "user/user_tab";
        }
        return jspPath;
    }
}