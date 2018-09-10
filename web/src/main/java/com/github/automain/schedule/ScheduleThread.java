package com.github.automain.schedule;

import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.RedisUtil;
import com.github.automain.util.SystemUtil;
import com.github.automain.util.http.HTTPRequestBean;
import com.github.automain.util.http.HTTPUtil;
import redis.clients.jedis.Jedis;

public class ScheduleThread implements Runnable {

    private String scheduleUrl;

    private Jedis jedis;

    private long jump;

    public ScheduleThread(String scheduleUrl, Jedis jedis, long jump) {
        this.scheduleUrl = scheduleUrl;
        this.jedis = jedis;
        this.jump = jump;
    }

    @Override
    public void run() {
        try {
            int expireSeconds = jump > 59 ? 50 : (int) (jump / 2);
            String lockKey = "SCHEDULE_" + scheduleUrl;
            boolean lock = RedisUtil.getDistributeLock(jedis, lockKey, expireSeconds);
            if (lock) {
                HTTPRequestBean bean = new HTTPRequestBean();
                bean.setUrl(PropertiesUtil.PROJECT_HOST + scheduleUrl);
                bean.setHeaders(HTTPUtil.generateAPIToken("appkey", scheduleUrl, null, SystemUtil.getNowSecond() + 1800));
                HTTPUtil.sendRequest(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
