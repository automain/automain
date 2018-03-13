package com.github.automain.user.service;

import com.github.automain.user.bean.TbMenu;
import com.github.automain.user.dao.TbMenuDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

public class TbMenuService extends BaseService<TbMenu, TbMenuDao> {

    public TbMenuService(TbMenu bean, TbMenuDao dao) {
        super(bean, dao);
    }

    public PageBean<TbMenu> selectTableForCustomPage(ConnectionBean connection, TbMenu bean, HttpServletRequest request) throws Exception {
        PageBean<TbMenu> pageBean = getDao().selectTableForCustomPage(connection, bean, pageFromRequest(request), limitFromRequest(request));
        List<TbMenu> data = pageBean.getData();
        for (TbMenu menu : data) {
            Long parentId = menu.getParentId();
            if (parentId != null && !parentId.equals(0L)) {
                TbMenu parent = selectTableById(connection, menu.getParentId());
                if (parent != null) {
                    menu.setParentName(parent.getMenuName());
                    continue;
                }
            }
            menu.setParentName("无");
        }
        return pageBean;
    }

    public List<TbMenu> selectTableByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        return getDao().selectTableByRoleId(connection, getBean(), roleId);
    }

    public List<Long> selectMenuIdByParentId(ConnectionBean connection, Long parentId) throws SQLException {
        return getDao().selectMenuIdByParentId(connection, parentId);
    }

    public List<TbMenu> selectMenuByParentId(ConnectionBean connection, Long parentId) throws SQLException {
        TbMenu bean = new TbMenu();
        bean.setParentId(parentId);
        return selectTableByBean(connection, bean);
    }
}