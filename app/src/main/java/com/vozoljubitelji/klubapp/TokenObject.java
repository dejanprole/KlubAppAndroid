package com.vozoljubitelji.klubapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by macosx on 1/24/18.
 */

public class TokenObject {
    @SerializedName("token")
    private String token;
    public TokenObject(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}