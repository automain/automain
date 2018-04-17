package com.github.automain.common.service;

import com.github.automain.common.bean.TbDictionary;
import com.github.automain.common.dao.TbDictionaryDao;
import com.github.fastjdbc.bean.ConnectionBean;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;

public class TbDictionaryService extends BaseService<TbDictionary, TbDictionaryDao> {

    public TbDictionaryService(TbDictionary bean, TbDictionaryDao dao) {
        super(bean, dao);
    }

    public PageBean<TbDictionary> selectTableForCustomPage(ConnectionBean connection, TbDictionary bean, HttpServletRequest request) throws Exception {
        PageBean<TbDictionary> pageBean = getDao().selectTableForCustomPage(connection, bean, pageFromRequest(request), limitFromRequest(request));
        List<TbDictionary> data = pageBean.getData();
        for (TbDictionary dictionary : data) {
            Long parentId = dictionary.getParentId();
            if (parentId != null && !parentId.equals(0L)) {
                TbDictionary parent = selectTableById(connection, dictionary.getParentId());
                if (parent != null) {
                    dictionary.setParentName(parent.getDictionaryName());
                    continue;
                }
            }
            dictionary.setParentName("无");
        }
        return pageBean;
    }

    public List<TbDictionary> selectValidTable(ConnectionBean connection) throws SQLException {
        return getDao().selectValidTable(connection, getBean());
    }

    public List<String> selectTableNameList(ConnectionBean connection) throws Exception{
        return getDao().selectTableNameList(connection);
    }

    public List<String> selectColumnNameList(ConnectionBean connection, String tableName) throws Exception{
        return getDao().selectColumnNameList(connection, tableName);
    }

}