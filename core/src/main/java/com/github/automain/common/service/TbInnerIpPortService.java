package com.github.automain.common.service;

import com.github.automain.common.bean.TbInnerIpPort;
import com.github.automain.common.dao.TbInnerIpPortDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

public class TbInnerIpPortService extends BaseService<TbInnerIpPort, TbInnerIpPortDao> {

    public TbInnerIpPortService(TbInnerIpPort bean, TbInnerIpPortDao dao) {
        super(bean, dao);
    }

    public PageBean<TbInnerIpPort> selectTableForCustomPage(ConnectionBean connection, TbInnerIpPort bean, HttpServletRequest request) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean, pageFromRequest(request), limitFromRequest(request));
    }

    public List<String> selectInnerIpList(ConnectionBean connection) throws SQLException {
        return getDao().selectInnerIpList(connection);
    }

    public List<String> selectInnerIpPortList(ConnectionBean connection) throws SQLException {
        return getDao().selectInnerIpPortList(connection);
    }
}