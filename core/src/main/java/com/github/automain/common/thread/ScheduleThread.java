package com.github.automain.common.thread;

import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.RedisUtil;
import com.github.automain.util.SystemUtil;
import com.github.automain.util.http.HTTPRequestBean;
import com.github.automain.util.http.HTTPUtil;

public class ScheduleThread implements Runnable {

    private String scheduleUrl;

    private long jump;

    public ScheduleThread(String scheduleUrl, long jump) {
        this.scheduleUrl = scheduleUrl;
        this.jump = jump;
    }

    @Override
    public void run() {
        try {
            int expireSeconds = jump > 59 ? 50 : (int) (jump / 2);
            String lockKey = "SCHEDULE_" + scheduleUrl;
            boolean lock = RedisUtil.getDistributeLock(lockKey, expireSeconds);
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
