package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.user.bean.TbMenu;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MenuAddExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TbMenu bean = new TbMenu();
        bean = bean.beanFromRequest(request);
        bean.setIsLeaf(1);
        TB_MENU_SERVICE.insertIntoTable(connection, bean);
        Long parentId = bean.getParentId();
        TbMenu parent = TB_MENU_SERVICE.selectTableById(connection, parentId);
        if (parent != null) {
            TbMenu p = new TbMenu();
            p.setMenuId(parent.getMenuId());
            p.setIsLeaf(0);
            TB_MENU_SERVICE.updateTable(connection, p);
        }
        setJsonResult(request, CODE_SUCCESS, "添加成功");
        return null;
    }
}