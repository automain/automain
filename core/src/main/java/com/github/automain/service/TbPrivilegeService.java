package com.github.automain.service;

import com.github.automain.bean.TbPrivilege;
import com.github.automain.dao.TbPrivilegeDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

public class TbPrivilegeService extends BaseService<TbPrivilege, TbPrivilegeDao> {

    public TbPrivilegeService(TbPrivilege bean, TbPrivilegeDao dao) {
        super(bean, dao);
    }

    public PageBean<TbPrivilege> selectTableForCustomPage(ConnectionBean connection, TbPrivilege bean, HttpServletRequest request) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean, pageFromRequest(request), limitFromRequest(request));
    }

    public int countPrivilegeByParentId(ConnectionBean connection, Long parentId) throws SQLException {
        TbPrivilege bean = new TbPrivilege();
        bean.setParentId(parentId);
        return getDao().countTableByBean(connection, bean);
    }

    public List<Long> selectPrivilegeIdByParentId(ConnectionBean connection, Long parentId) throws SQLException {
        return getDao().selectPrivilegeIdByParentId(connection, parentId);
    }
}