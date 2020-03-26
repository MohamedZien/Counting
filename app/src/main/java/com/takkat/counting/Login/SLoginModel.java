package com.takkat.counting.Login;

public class SLoginModel {

    private String UserName ;
    private String Password ;
    private String grant_type ;

    public SLoginModel() {
    }

    public SLoginModel(String userName, String password, String grant_type) {
        UserName = userName;
        Password = password;
        this.grant_type = grant_type;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getGrant_type() {
        return grant_type;
    }

    public void setGrant_type(String grant_type) {
        this.grant_type = grant_type;
    }
}
