package com.github.automain.util.mail;

import com.github.automain.util.PropertiesUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.MXRecord;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MailUtil {

    private static final String NEXT_LINE = System.lineSeparator();
    // 发送邮件线程池大小
    private static ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10);
    // 验证email有效性的域名
    private static final String DEFAULT_EMAIL_CHECK_HOST = "baidu.com";

    /**
     * 检查邮箱是否存在
     *
     * @param email
     * @return
     */
    public static boolean checkEmailExist(String email) throws Exception {
        if (email == null || email.indexOf(".") < email.indexOf("@") || !email.contains("@")) {
            return false;
        }
        String host = email.split("@", 2)[1];
        if (DEFAULT_EMAIL_CHECK_HOST.equalsIgnoreCase(host)) {
            return false;
        }
        Socket socket = new Socket();
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            // 查找mx记录
            Record[] mxRecords = new Lookup(host, Type.MX).run();
            if (ArrayUtils.isEmpty(mxRecords)) {
                return false;
            }
            // 邮件服务器地址
            String mxHost = ((MXRecord) mxRecords[0]).getTarget().toString();
            if (mxRecords.length > 1) { // 优先级排序
                List<Record> arrRecords = new ArrayList<Record>();
                Collections.addAll(arrRecords, mxRecords);
                Collections.sort(arrRecords, (o1, o2) -> new CompareToBuilder().append(((MXRecord) o1).getPriority(), ((MXRecord) o2).getPriority()).toComparison());
                mxHost = ((MXRecord) arrRecords.get(0)).getTarget().toString();
            }
            // 开始smtp
            socket.connect(new InetSocketAddress(mxHost, 25));
            bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream()), PropertiesUtil.DEFAULT_CHARSET));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), PropertiesUtil.DEFAULT_CHARSET));
            // 超时时间(毫秒)
            long timeout = 6000;
            // 睡眠时间片段(50毫秒)
            int sleepSect = 50;
            // 连接(服务器是否就绪)
            if (getResponseCode(timeout, sleepSect, bufferedReader) != 220) {
                return false;
            }
            // 握手
            bufferedWriter.write("HELO " + DEFAULT_EMAIL_CHECK_HOST + NEXT_LINE);
            bufferedWriter.flush();
            if (getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
                return false;
            }
            // 身份
            bufferedWriter.write("MAIL FROM: <check@" + DEFAULT_EMAIL_CHECK_HOST + ">" + NEXT_LINE);
            bufferedWriter.flush();
            if (getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
                return false;
            }
            // 验证
            bufferedWriter.write("RCPT TO: <" + email + ">" + NEXT_LINE);
            bufferedWriter.flush();
            if (getResponseCode(timeout, sleepSect, bufferedReader) != 250) {
                return false;
            }
            // 断开
            bufferedWriter.write("QUIT" + NEXT_LINE);
            bufferedWriter.flush();
            return true;
        } finally {
            socket.close();
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
    }

    private static int getResponseCode(long timeout, int sleepSect, BufferedReader bufferedReader) throws InterruptedException, NumberFormatException, IOException {
        for (long i = sleepSect; i < timeout; i += sleepSect) {
            Thread.sleep(sleepSect);
            if (bufferedReader.ready()) {
                String outline = bufferedReader.readLine();
                if (StringUtils.isNotBlank(outline)) {
                    return Integer.parseInt(outline.substring(0, 3));
                }
            }
        }
        return 0;
    }

    /**
     * 发送邮件
     *
     * @param mail
     * @return
     * @throws Exception
     */
    public static void sendEmail(Mail mail) {
        THREAD_POOL.execute(new SendMailThread(mail));
    }

}
