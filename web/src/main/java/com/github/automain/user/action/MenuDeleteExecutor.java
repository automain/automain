package com.github.automain.user.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.RequestUrl;
import com.github.automain.user.bean.TbMenu;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestUrl("/menu/delete")
public class MenuDeleteExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Long> deleteCheck = getLongValues("deleteCheck", request);
        if (deleteCheck != null && !deleteCheck.isEmpty()){
            TB_MENU_SERVICE.softDeleteTableByIdList(connection, deleteCheck);
            connection.closeReadUseWrite();
            Long menuId = deleteCheck.get(0);
            TbMenu menu = TB_MENU_SERVICE.selectTableById(connection, menuId);
            if (menu != null) {
                Long parentId = menu.getParentId();
                if (!parentId.equals(0L)) {
                    List<TbMenu> parentChildren = TB_MENU_SERVICE.selectMenuByParentId(connection, parentId);
                    if (parentChildren.isEmpty()) {
                        TbMenu parent = new TbMenu();
                        parent.setMenuId(parentId);
                        parent.setIsLeaf(1);
                        TB_MENU_SERVICE.updateTable(connection, parent, false);
                    }
                }
            }
        }
        setJsonResult(request, CODE_SUCCESS, "删除成功");
        return null;
    }
}