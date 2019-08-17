package com.github.automain.common.thread;

import com.github.automain.util.DateUtil;
import com.github.automain.util.RedisUtil;
import com.github.automain.util.SystemUtil;
import com.github.automain.util.http.HTTPRequestBean;
import com.github.automain.util.http.HTTPUtil;

public class ScheduleThread implements Runnable {

    private String scheduleUrl;

    private long period;

    public ScheduleThread(String scheduleUrl, long period) {
        this.scheduleUrl = scheduleUrl;
        this.period = period;
    }

    @Override
    public void run() {
        try {
            int expireSeconds = period > 59 ? 50 : (int) (period / 2);
            String lockKey = "SCHEDULE_" + scheduleUrl;
            boolean lock = RedisUtil.getDistributeLock(lockKey, expireSeconds);
            if (lock) {
                HTTPRequestBean bean = new HTTPRequestBean();
                bean.setUrl(SystemUtil.PROJECT_HOST + scheduleUrl);
                bean.setHeaders(HTTPUtil.generateAPIToken("test", scheduleUrl, null, DateUtil.getNow() + 1800));
                HTTPUtil.sendRequest(bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
