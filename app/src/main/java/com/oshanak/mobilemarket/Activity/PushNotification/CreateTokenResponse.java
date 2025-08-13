package com.oshanak.mobilemarket.Activity.PushNotification;

public class CreateTokenResponse {
    private String jwtToken;
    private long expirationDateTime;

    public String getJwtToken() {
        return jwtToken;
    }

    public long getExpirationDateTime() {
        return expirationDateTime;
    }



    @Override
    public String toString() {
        return "CreateTokenResponse{" +
                "jwtToken='" + jwtToken + '\'' +
                ", expirationDateTime=" + expirationDateTime +

                '}';
    }
}
