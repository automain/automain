package com.github.automain.user.service;

import com.github.automain.user.bean.TbRoleRequestMapping;
import com.github.automain.user.dao.TbRoleRequestMappingDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TbRoleRequestMappingService extends BaseService<TbRoleRequestMapping, TbRoleRequestMappingDao> {

    public TbRoleRequestMappingService(TbRoleRequestMapping bean, TbRoleRequestMappingDao dao) {
        super(bean, dao);
    }

    public PageBean<TbRoleRequestMapping> selectTableForCustomPage(ConnectionBean connection, TbRoleRequestMapping bean, HttpServletRequest request) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean, pageFromRequest(request), limitFromRequest(request));
    }

    public Set<String> selectRequestUrlByRoleId(ConnectionBean connection, Long roleId) throws SQLException {
        return getDao().selectRequestUrlByRoleId(connection, roleId);
    }

    public PageBean<TbRoleRequestMapping> selectTableForRole(ConnectionBean connection, List<String> requestUrlList, HttpServletRequest request, Long roleId) throws SQLException {
        int page = pageFromRequest(request);
        int limit = limitFromRequest(request);
        int start = page * limit;
        int end = (page + 1) * limit;
        Set<String> roleUrlSet = selectRequestUrlByRoleId(connection, roleId);
        TbRoleRequestMapping bean = null;
        List<TbRoleRequestMapping> data = new ArrayList<TbRoleRequestMapping>();
        for (int i = start; i < end; i++) {
            bean = new TbRoleRequestMapping();
            String url = requestUrlList.get(i);
            bean.setRequestUrl(url);
            bean.setIsDelete(0);
            bean.setRoleId(roleId);
            if (roleUrlSet.contains(url)) {
                bean.setHasRole(1);
            } else {
                bean.setHasRole(0);
            }
            data.add(bean);
        }
        PageBean<TbRoleRequestMapping> pageBean = new PageBean<TbRoleRequestMapping>();
        pageBean.setData(data);
        pageBean.setCurr(page);
        pageBean.setCount(requestUrlList.size());
        return pageBean;
    }
}