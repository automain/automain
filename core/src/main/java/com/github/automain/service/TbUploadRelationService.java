package com.github.automain.service;

import com.github.automain.bean.TbUploadRelation;
import com.github.automain.dao.TbUploadRelationDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

public class TbUploadRelationService extends BaseService<TbUploadRelation, TbUploadRelationDao> {

    public TbUploadRelationService(TbUploadRelation bean, TbUploadRelationDao dao) {
        super(bean, dao);
    }

    public PageBean<TbUploadRelation> selectTableForCustomPage(ConnectionBean connection, TbUploadRelation bean, HttpServletRequest request) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean, pageFromRequest(request), limitFromRequest(request));
    }

    public TbUploadRelation selectMaxOrderNumberRelationByBean(ConnectionBean connection, TbUploadRelation bean) throws SQLException {
        return getDao().selectMaxOrderNumberRelationByBean(connection, bean);
    }

}