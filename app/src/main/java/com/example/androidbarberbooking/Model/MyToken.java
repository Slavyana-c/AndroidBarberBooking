package com.example.androidbarberbooking.Model;

import com.example.androidbarberbooking.Common.Common;

public class MyToken {
    private String userEmail;
    private Common.TOKEN_TYPE token_type;
    private String token;

    public MyToken() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Common.TOKEN_TYPE getToken_type() {
        return token_type;
    }

    public void setToken_type(Common.TOKEN_TYPE token_type) {
        this.token_type = token_type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
