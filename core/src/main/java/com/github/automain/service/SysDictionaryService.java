package com.github.automain.service;

import com.github.automain.bean.SysDictionary;
import com.github.automain.dao.SysDictionaryDao;
import com.github.automain.vo.DictionaryVO;
import com.github.automain.vo.SysDictionaryVO;
import com.github.fastjdbc.bean.PageBean;
import com.github.fastjdbc.common.BaseService;

import java.sql.Connection;
import java.util.List;

public class SysDictionaryService extends BaseService<SysDictionary, SysDictionaryDao> {

    public SysDictionaryService(SysDictionary bean, SysDictionaryDao dao) {
        super(bean, dao);
    }

    public PageBean<SysDictionary> selectTableForCustomPage(Connection connection, SysDictionaryVO bean) throws Exception {
        return getDao().selectTableForCustomPage(connection, bean);
    }

    public List<DictionaryVO> selectAllDictionaryVO(Connection connection) throws Exception{
        return getDao().selectAllDictionaryVO(connection);
    }

    public List<String> selectDictionaryColumn(Connection connection, String tableName) throws Exception{
        return getDao().selectDictionaryColumn(connection, tableName);
    }
}