package com.github.automain.util.mail;

import java.util.List;
import java.util.Map;

public class Mail {

    // 发送者邮箱
    private String username;
    // 发送者昵称
    private String nickname;
    // 发生者邮箱密码
    private String password;
    // SMTP host
    private String SMTPHost;
    // 邮件主题
    private String subject;
    // 邮件内容
    private String content;
    // 收件人集合
    private List<String> toAddressList;
    // 抄送人集合
    private List<String> ccAddressList;
    // 暗送人集合
    private List<String> bccAddressList;
    // 文件集合(key为要显示的文件名称,value为文件地址)
    private Map<String, String> fileMap;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSMTPHost() {
        return SMTPHost;
    }

    public void setSMTPHost(String SMTPHost) {
        this.SMTPHost = SMTPHost;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getToAddressList() {
        return toAddressList;
    }

    public void setToAddressList(List<String> toAddressList) {
        this.toAddressList = toAddressList;
    }

    public List<String> getCcAddressList() {
        return ccAddressList;
    }

    public void setCcAddressList(List<String> ccAddressList) {
        this.ccAddressList = ccAddressList;
    }

    public List<String> getBccAddressList() {
        return bccAddressList;
    }

    public void setBccAddressList(List<String> bccAddressList) {
        this.bccAddressList = bccAddressList;
    }

    public Map<String, String> getFileMap() {
        return fileMap;
    }

    public void setFileMap(Map<String, String> fileMap) {
        this.fileMap = fileMap;
    }
}
