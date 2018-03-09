package com.github.automain.user.view;

import com.github.automain.common.BaseExecutor;
import com.github.automain.common.vo.MenuBreadcrumbVO;
import com.github.automain.user.bean.TbMenu;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuForwardExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String forwardType = getString("forwardType", request, "");
        String jspPath = null;
        Long parentId = getLong("parentId", request, 0L);
        request.setAttribute("parentId", parentId);
        Long topId = getLong("topId", request, 0L);
        request.setAttribute("topId", topId);
        Long menuId = getLong("menuId", request);
        request.setAttribute("menuId", menuId);
        switch (forwardType) {
            case "add":
                jspPath = "request/menu_add";
                TbMenu parent = TB_MENU_SERVICE.selectTableById(connection, parentId);
                request.setAttribute("parent", parent);
                break;
            case "update":
                TbMenu bean = TB_MENU_SERVICE.selectTableById(connection, menuId);
                request.setAttribute("bean", bean);
                jspPath = "request/menu_update";
                break;
            case "role":
                jspPath = "request/menu_role";
                break;
            default:
                List<MenuBreadcrumbVO> parentList = new ArrayList<MenuBreadcrumbVO>();
                if (parentId.equals(0L)) {
                    request.setAttribute("cite", "顶级");
                } else {
                    TbMenu menu = TB_MENU_SERVICE.selectTableById(connection, parentId);
                    if (menu != null) {
                        request.setAttribute("cite", menu.getMenuName());
                        setBreadcrumb(connection, menu, parentList);
                        Collections.reverse(parentList);
                        if (parentList.size() > 1) {
                            request.setAttribute("topId", parentList.get(1).getParentId());
                        } else {
                            request.setAttribute("topId", menu.getMenuId());
                        }
                        request.setAttribute("parentList", parentList);
                    }
                }
                jspPath = "request/menu_tab";
        }
        return jspPath;
    }

    private void setBreadcrumb(ConnectionBean connection, TbMenu menu, List<MenuBreadcrumbVO> parentList) throws SQLException {
        MenuBreadcrumbVO vo = new MenuBreadcrumbVO();
        if (menu != null && !menu.getParentId().equals(0L)) {
            TbMenu parent = TB_MENU_SERVICE.selectTableById(connection, menu.getParentId());
            if (parent != null) {
                vo.setMenuName(parent.getMenuName());
                vo.setParentId(parent.getMenuId());
                parentList.add(vo);
            }
            setBreadcrumb(connection, parent, parentList);
        } else {
            vo.setMenuName("顶级");
            vo.setParentId(0L);
            parentList.add(vo);
        }
    }
}