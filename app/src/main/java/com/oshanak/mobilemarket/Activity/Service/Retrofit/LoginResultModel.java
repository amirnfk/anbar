package com.oshanak.mobilemarket.Activity.Service.Retrofit;

public class LoginResultModel {
    DocsTypeModel LoginResult;

    public LoginResultModel( DocsTypeModel loginResult) {
        LoginResult = loginResult;
    }

    public DocsTypeModel getLoginResult() {
        return LoginResult;
    }

    public void setLoginResult( DocsTypeModel loginResult) {
        LoginResult = loginResult;
    }

    @Override
    public String toString() {
        return "LoginResultModel{" +
                "LoginResult=" + LoginResult +
                '}';
    }
}