package com.github.automain.user.view;

import com.alibaba.fastjson.JSON;
import com.github.automain.common.BaseExecutor;
import com.github.automain.common.container.RolePrivilegeContainer;
import com.github.automain.common.vo.ChildMenuVO;
import com.github.automain.common.vo.MenuVO;
import com.github.automain.upload.bean.TbUploadRelation;
import com.github.automain.user.bean.TbUser;
import com.github.automain.util.UploadUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

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
            List<MenuVO> menuVOList = RolePrivilegeContainer.getMenuByUserId(userId);
            List<MenuVO> titleList = new ArrayList<MenuVO>();
            List<ChildMenuVO> childrenList = new ArrayList<ChildMenuVO>();
            MenuVO title = null;
            ChildMenuVO childMenuVO = null;
            if (menuVOList != null) {
                for (MenuVO vo : menuVOList) {
                    title = new MenuVO();
                    title.setId(vo.getId());
                    title.setLink(vo.getLink());
                    title.setIcon(vo.getIcon());
                    title.setName(vo.getName());
                    titleList.add(title);
                    childMenuVO = new ChildMenuVO();
                    childMenuVO.setParentId(vo.getId());
                    childMenuVO.setNodeString(JSON.toJSONString(vo.getChildren()));
                    childrenList.add(childMenuVO);
                }
            }
            TbUploadRelation uploadRelation = new TbUploadRelation();
            uploadRelation.setRecordTableName(user.tableName());
            uploadRelation.setRecordId(userId);
            String imgPath = UploadUtil.getLastFile(connection, request, uploadRelation);
            request.setAttribute("imgPath", imgPath);
            request.setAttribute("titleList", titleList);
            request.setAttribute("childrenList", childrenList);
            request.setAttribute("userName", user.getUserName());
        }
        return "/common/frame";
    }
}
