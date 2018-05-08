package com.github.automain.monitor.action;

import com.github.automain.common.BaseExecutor;
import com.github.automain.monitor.vo.DbSqlVO;
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

public class DbSqlExecutor extends BaseExecutor {

    @Override
    protected String doAction(ConnectionBean connection, Jedis jedis, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Timestamp startTime = getTimestamp("startTime", request, "yyyy-MM-dd", DateUtil.getMinDayTimestamp(System.currentTimeMillis()));
        Timestamp endTime = new Timestamp(startTime.getTime() + 86406000);
        List<DbSqlVO> dbSqlVOList = DB_STATUS_SERVICE.selectDbSql(connection, startTime, endTime);
        List<String> poolNameList = new ArrayList<String>(Arrays.asList(PropertiesUtil.POOL_NAMES));
        String masterName = poolNameList.remove(0);
        request.setAttribute("masterName", masterName);
        request.setAttribute("slaveNames", poolNameList);
        request.setAttribute("dbSqlVOList", dbSqlVOList);
        return null;
    }
}
