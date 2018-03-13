package com.github.automain.user.service;

import com.github.automain.common.container.ServiceContainer;
import com.github.automain.user.bean.TbRequestMapping;
import com.github.automain.user.bean.TbRoleRequestMapping;
import com.github.automain.user.dao.TbRequestMappingDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

public class TbRequestMappingService extends BaseService<TbRequestMapping, TbRequestMappingDao> implements ServiceContainer {

    public TbRequestMappingService(TbRequestMapping bean, TbRequestMappingDao dao) {
        super(bean, dao);
    }

    public PageBean<TbRequestMapping> selectTableForCustomPage(ConnectionBean connection, TbRequestMapping bean, HttpServletRequest request) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean,  pageFromRequest(request), limitFromRequest(request));
    }

    public PageBean<TbRequestMapping> selectTableForRole(ConnectionBean connection, TbRequestMapping bean, HttpServletRequest request, Long roleId) throws Exception {
        PageBean<TbRequestMapping> pageBean = selectTableForCustomPage(connection, bean, request);
        List<TbRequestMapping> data = pageBean.getData();
        TbRoleRequestMapping roleRequestMappingParam = new TbRoleRequestMapping();
        roleRequestMappingParam.setRoleId(roleId);
        roleRequestMappingParam.setIsDelete(0);
        for (TbRequestMapping requestMapping : data) {
            roleRequestMappingParam.setRequestMappingId(requestMapping.getRequestMappingId());
            TbRoleRequestMapping roleRequestMapping = TB_ROLE_REQUEST_MAPPING_SERVICE.selectOneTableByBean(connection, roleRequestMappingParam);
            if (roleRequestMapping != null) {
                requestMapping.setHasRole(1);
            } else {
                requestMapping.setHasRole(0);
            }
        }
        return pageBean;
    }

    public TbRequestMapping selectTableByRequestUrl(ConnectionBean connection, String requestUrl) throws SQLException {
        TbRequestMapping bean = new TbRequestMapping();
        bean.setRequestUrl(requestUrl);
        return TB_REQUEST_MAPPING_SERVICE.selectOneTableByBean(connection, bean);
    }
}