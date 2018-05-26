package com.github.automain.util.mail;

import com.github.automain.util.PropertiesUtil;
import com.github.automain.util.SystemUtil;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class SendMailThread implements Runnable {

    private static Logger LOGGER = SystemUtil.getLoggerByName("system");

    // 默认邮件发送邮箱用户名
    private static final String DEFAULT_EMAIL_SENDER_USERNAME = "";
    // 默认邮件发送邮箱密码
    private static final String DEFAULT_EMAIL_SENDER_PASSWORD = "";
    // 默认邮件发送邮箱smtp域名
    private static final String DEFAULT_EMAIL_SENDER_SMTP_HOST = "";

    private Mail mail;

    public SendMailThread(Mail mail) {
        this.mail = mail;
    }

    @Override
    public void run() {
        if (mail != null) {
            try {
                if (mail.getUsername() == null && mail.getPassword() == null && mail.getSMTPHost() == null) {
                    mail.setUsername(DEFAULT_EMAIL_SENDER_USERNAME);
                    mail.setPassword(DEFAULT_EMAIL_SENDER_PASSWORD);
                    mail.setSMTPHost(DEFAULT_EMAIL_SENDER_SMTP_HOST);
                }
                if (mail.getNickname() == null) {
                    mail.setNickname(DEFAULT_EMAIL_SENDER_USERNAME);
                }
                // 初始化properties
                Properties properties = new Properties();
                properties.put("mail.smtp.host", mail.getSMTPHost());
                properties.put("mail.smtp.auth", "true");
                // 进行邮件服务用户认证
                Authenticator auth = new MailAuthenticator(mail.getUsername(), mail.getPassword());
                // 创建session,和邮件服务器进行通讯
                Session session = Session.getDefaultInstance(properties, auth);
                // 创建mime类型邮件
                MimeMessage message = new MimeMessage(session);
                // 设置邮件发送日期
//                message.setSentDate(new Date());
                // 设置发送者地址
                Address senderAddress = new InternetAddress(mail.getUsername(), mail.getNickname());
                message.setFrom(senderAddress);
                //设置邮件主题
                message.setSubject(mail.getSubject(), PropertiesUtil.DEFAULT_CHARSET);
                // 设置邮件内容
                MimeMultipart contentParts = new MimeMultipart();
                MimeBodyPart content = new MimeBodyPart();
                content.setContent(mail.getContent(), "text/html;charset=" + PropertiesUtil.DEFAULT_CHARSET);
                contentParts.addBodyPart(content);
                // 设置附件
                if (mail.getFileMap() != null && mail.getFileMap().size() > 0) {
                    for (Map.Entry<String, String> entry : mail.getFileMap().entrySet()) {
                        MimeBodyPart file = new MimeBodyPart();
                        file.attachFile(new File(entry.getValue()));
                        file.setFileName(entry.getKey());
                        contentParts.addBodyPart(file);
                    }
                }
                message.setContent(contentParts);
                StringBuilder toAddress = new StringBuilder();
                // 设置收件人
                if (mail.getToAddressList() != null) {
                    setReceivers(message, mail.getToAddressList(), Message.RecipientType.TO);
                    for (String s : mail.getToAddressList()) {
                        toAddress.append(s).append(",");
                    }
                    if (toAddress.length() > 1) {
                        toAddress.deleteCharAt(toAddress.length());
                    }
                }
                // 设置抄送人
                if (mail.getCcAddressList() != null) {
                    setReceivers(message, mail.getCcAddressList(), Message.RecipientType.CC);
                }
                // 设置密送人
                if (mail.getBccAddressList() != null) {
                    setReceivers(message, mail.getBccAddressList(), Message.RecipientType.BCC);
                }
                LOGGER.info("subject:" + mail.getSubject() + ", to:" + toAddress.toString());
                Transport transport = session.getTransport();
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void setReceivers(MimeMessage message, List<String> addressList, Message.RecipientType type) throws MessagingException {
        for (String s : addressList) {
            message.addRecipients(type, s);
        }
    }
}
