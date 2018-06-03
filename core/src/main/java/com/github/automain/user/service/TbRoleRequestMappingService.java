package com.github.automain.user.service;

import com.github.automain.user.bean.TbRoleRequestMapping;
import com.github.automain.user.dao.TbRoleRequestMappingDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TbRoleRequestMappingService extends BaseService<TbRoleRequestMapping, TbRoleRequestMappingDao> {

    public TbRoleRequestMappingService(TbRoleRequestMapping bean, TbRoleRequestMappingDao dao) {
        super(bean, dao);
    }

    public PageBean<TbRoleRequestMapping> selectTableForCustomPage(ConnectionBean connection, TbRoleRequestMapping bean, HttpServletRequest request) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean, pageFromRequest(request), limitFromRequest(request));
    }

    public Set<String> selectRequestUrlByRoleId(ConnectionBean connection, Long roleId, List<String> requestUrlList) throws SQLException {
        if (roleId.equals(1L)) {
            return new HashSet<String>(requestUrlList);
        }
        return getDao().selectRequestUrlByRoleId(connection, roleId);
    }

    public PageBean<TbRoleRequestMapping> selectTableForRole(ConnectionBean connection, List<String> requestUrlList, HttpServletRequest request, Long roleId, String searchUrl) throws SQLException {
        int page = pageFromRequest(request);
        int limit = limitFromRequest(request);
        limit = limit < 1 ? 1 : limit;
        page = page < 1 ? 1 : page;
        int start = (page - 1) * limit;
        int end = page * limit;
        List<String> urlList = new ArrayList<String>();
        if (searchUrl != null) {
            for (String url : requestUrlList) {
                if (url.contains(searchUrl)) {
                    urlList.add(url);
                }
            }
        } else {
            urlList = requestUrlList;
        }
        int totalSize = urlList.size();
        if (end > totalSize) {
            end = totalSize;
        }
        Set<String> roleUrlSet = selectRequestUrlByRoleId(connection, roleId, urlList);
        TbRoleRequestMapping bean = null;
        List<TbRoleRequestMapping> data = new ArrayList<TbRoleRequestMapping>();
        for (int i = start; i < end; i++) {
            bean = new TbRoleRequestMapping();
            String url = urlList.get(i);
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
        pageBean.setCount(totalSize);
        return pageBean;
    }
}