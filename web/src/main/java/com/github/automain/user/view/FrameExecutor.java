package com.github.automain.user.view;

import com.alibaba.fastjson.JSON;
import com.github.automain.common.BaseExecutor;
import com.github.automain.common.annotation.RequestUrl;
import com.github.automain.common.bean.TbUploadRelation;
import com.github.automain.common.container.RolePrivilegeContainer;
import com.github.automain.common.vo.MenuVO;
import com.github.automain.user.bean.TbUser;
import com.github.automain.util.UploadUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestUrl("/user/frame")
public class FrameExecutor extends BaseExecutor {

    @Override
    protected boolean checkAuthority(Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return checkUserLogin(jedis, request, response);
    }

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbUser user = getSessionUser(jedis, request, response);
        if (user != null) {
            Long userId = user.getUserId();
            List<MenuVO> menuVOList = RolePrivilegeContainer.getMenuByUserId(jedis, userId);
            TbUploadRelation uploadRelation = new TbUploadRelation();
            uploadRelation.setRecordTableName(user.tableName());
            uploadRelation.setRecordId(userId);
            String imgPath = UploadUtil.getLastFile(connection, request, uploadRelation);
            request.setAttribute("imgPath", imgPath);
            request.setAttribute("menuVOList", JSON.toJSONString(menuVOList));
            request.setAttribute("userName", user.getUserName());
        }
        return "common/frame";
    }
}
