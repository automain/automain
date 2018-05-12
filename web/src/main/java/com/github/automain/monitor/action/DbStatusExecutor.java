package com.github.automain.monitor.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.monitor.vo.DbPagesVO;
import com.github.automain.monitor.vo.DbSqlVO;
import com.github.automain.monitor.vo.DbThreadVO;
import com.github.automain.monitor.vo.DbTransactionVO;
import com.github.automain.util.DateUtil;
import com.github.automain.util.PropertiesUtil;
import com.github.fastjdbc.bean.ConnectionBean;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DbStatusExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String dataType = getString("dataType", request, "");
        Timestamp startTime = getTimestamp("startTime", request, "yyyy-MM-dd", DateUtil.getMinDayTimestamp(System.currentTimeMillis()));
        Timestamp endTime = new Timestamp(startTime.getTime() + 86406000);
        List<String> poolNameList = new ArrayList<String>(Arrays.asList(PropertiesUtil.POOL_NAMES));
        String masterName = poolNameList.remove(0);
        request.setAttribute("masterName", masterName);
        request.setAttribute("slaveNames", poolNameList);
        switch (dataType) {
            case "sql":
                List<DbSqlVO> dbSqlVOList = DB_STATUS_SERVICE.selectDbSql(connection, startTime, endTime);
                request.setAttribute("dbSqlVOList", dbSqlVOList);
                break;
            case "transaction":
                List<DbTransactionVO> dbTransactionVOList = DB_STATUS_SERVICE.selectDbTransaction(connection, startTime, endTime, masterName);
                request.setAttribute("dbTransactionVOList", dbTransactionVOList);
                break;
            case "thread":
                List<DbThreadVO> dbThreadVOList = DB_STATUS_SERVICE.selectDbThread(connection, startTime, endTime);
                request.setAttribute("dbThreadVOList", dbThreadVOList);
                break;
            case "pages":
                List<DbPagesVO> dbPagesVOList = DB_STATUS_SERVICE.selectDbPages(connection, startTime, endTime);
                request.setAttribute("dbPagesVOList", dbPagesVOList);
                break;
        }
        return null;
    }
}
