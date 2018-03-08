package com.github.automain.user.service;

import com.github.automain.user.bean.TbRoleRequestMapping;
import com.github.automain.user.dao.TbRoleRequestMappingDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Set;

public class TbRoleRequestMappingService extends BaseService<TbRoleRequestMapping, TbRoleRequestMappingDao> {

    public TbRoleRequestMappingService(TbRoleRequestMapping bean, TbRoleRequestMappingDao dao) {
        super(bean, dao);
    }

    public PageBean<TbRoleRequestMapping> selectTableForCustomPage(ConnectionBean connection, TbRoleRequestMapping bean, HttpServletRequest request) throws Exception {
        int page = getInt("page", request, 1);
        int limit = getInt("limit", request, 1);
        return getDao().selectTableForCustomPage(connection, bean, page, limit);
    }

    public Set<String> selectRequestUrlByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        return getDao().selectRequestUrlByRoleId(connection, roleId);
    }
}