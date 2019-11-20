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

    public Mail setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public Mail setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Mail setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getSMTPHost() {
        return SMTPHost;
    }

    public Mail setSMTPHost(String SMTPHost) {
        this.SMTPHost = SMTPHost;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Mail setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Mail setContent(String content) {
        this.content = content;
        return this;
    }

    public List<String> getToAddressList() {
        return toAddressList;
    }

    public Mail setToAddressList(List<String> toAddressList) {
        this.toAddressList = toAddressList;
        return this;
    }

    public List<String> getCcAddressList() {
        return ccAddressList;
    }

    public Mail setCcAddressList(List<String> ccAddressList) {
        this.ccAddressList = ccAddressList;
        return this;
    }

    public List<String> getBccAddressList() {
        return bccAddressList;
    }

    public Mail setBccAddressList(List<String> bccAddressList) {
        this.bccAddressList = bccAddressList;
        return this;
    }

    public Map<String, String> getFileMap() {
        return fileMap;
    }

    public Mail setFileMap(Map<String, String> fileMap) {
        this.fileMap = fileMap;
        return this;
    }

    @Override
    public String toString() {
        return "Mail{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", SMTPHost='" + SMTPHost + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                ", toAddressList=" + toAddressList +
                ", ccAddressList=" + ccAddressList +
                ", bccAddressList=" + bccAddressList +
                ", fileMap=" + fileMap +
                '}';
    }
}
