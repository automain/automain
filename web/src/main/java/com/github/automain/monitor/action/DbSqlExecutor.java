package com.github.automain.monitor.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.monitor.vo.DbSqlVO;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.List;

public class DbSqlExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Timestamp startTime = getTimestamp("startTime", request, "yyyy-MM-dd");
        Timestamp endTime = new Timestamp(startTime.getTime() + 86406000);
        List<DbSqlVO> dbSqlVOList = DB_STATUS_SERVICE.selectDbSql(connection, startTime, endTime);
        request.setAttribute("dbSqlVOList", dbSqlVOList);
        return null;
    }
}
