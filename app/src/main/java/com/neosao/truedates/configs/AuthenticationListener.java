package com.neosao.truedates.configs;

public interface AuthenticationListener {
    void onTokenReceived(String auth_token);
}