package com.github.automain.vo;

import com.github.fastjdbc.BaseBean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class IdNameVO implements BaseBean<IdNameVO> {

    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public IdNameVO setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public IdNameVO setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String tableName() {
        return null;
    }

    @Override
    public Map<String, Object> columnMap(boolean b) {
        return null;
    }

    @Override
    public IdNameVO beanFromResultSet(ResultSet rs) throws SQLException {
        return new IdNameVO().setId(rs.getInt("id")).setName(rs.getString("name"));
    }

    @Override
    public String toString() {
        return "IdNameVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
