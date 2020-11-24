package com.neosao.truedates.configs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;

import com.neosao.truedates.R;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class AuthenticationDialog extends Dialog {

    private final String redirect_url;
    private final String request_url;
    private AuthenticationListener listener;
    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(redirect_url)) {
                AuthenticationDialog.this.dismiss();
                return true;
            }
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //Log.e("url", url);
            String access_token = "";

                try {
                    access_token = URLDecoder.decode(url, "UTF-8");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            if (access_token.contains("code=")) {

                String s[] = access_token.split("code=", 2);
                s = s[1].split("#_");
                listener.onTokenReceived(s[0]);
            }
        }
    };


    public AuthenticationDialog(@NonNull Context context, AuthenticationListener listener) {
        super(context);
        this.listener = listener;
        this.redirect_url = context.getResources().getString(R.string.callback_url);
        this.request_url = context.getResources().getString(R.string.base_url) +
                "oauth/authorize/?client_id=" +
                context.getResources().getString(R.string.client_id) +
                "&redirect_uri=" + redirect_url +
                "&response_type=code&display=touch&scope=user_profile,user_media";//&display=touch
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.auth_dialog);

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(request_url);
        webView.setWebViewClient(webViewClient);

    }
}