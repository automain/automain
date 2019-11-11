package com.github.automain.vo;

public class LoginUserVO {

    private String userName;

    private String password;

    private String captcha;

    private String captchaKey;

    public String getUserName() {
        return userName;
    }

    public LoginUserVO setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginUserVO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getCaptcha() {
        return captcha;
    }

    public LoginUserVO setCaptcha(String captcha) {
        this.captcha = captcha;
        return this;
    }

    public String getCaptchaKey() {
        return captchaKey;
    }

    public LoginUserVO setCaptchaKey(String captchaKey) {
        this.captchaKey = captchaKey;
        return this;
    }

    @Override
    public String toString() {
        return "LoginUserVO{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", captcha='" + captcha + '\'' +
                ", captchaKey='" + captchaKey + '\'' +
                '}';
    }
}
