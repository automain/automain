package com.github.automain.vo;

public class ChangePasswordVO {

    private String oriPassword;

    private String password;

    private String password2;

    public String getOriPassword() {
        return oriPassword;
    }

    public ChangePasswordVO setOriPassword(String oriPassword) {
        this.oriPassword = oriPassword;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ChangePasswordVO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPassword2() {
        return password2;
    }

    public ChangePasswordVO setPassword2(String password2) {
        this.password2 = password2;
        return this;
    }

    @Override
    public String toString() {
        return "ChangePasswordVO{" +
                "oriPassword='" + oriPassword + '\'' +
                ", password='" + password + '\'' +
                ", password2='" + password2 + '\'' +
                '}';
    }
}
