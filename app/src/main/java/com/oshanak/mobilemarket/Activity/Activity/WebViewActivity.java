package com.oshanak.mobilemarket.Activity.Activity;



import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.oshanak.mobilemarket.R;

import java.util.HashMap;
import java.util.Map;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private String url;
    private String username;
    private String password;
    private boolean hasAttempted = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webView = findViewById(R.id.webView);
        url = getIntent().getStringExtra("url");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        promptLoginAndLoad();
    }

    private void promptLoginAndLoad() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Credentials");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText inputUser = new EditText(this);
        inputUser.setHint("Username");
        layout.addView(inputUser);

        final EditText inputPass = new EditText(this);
        inputPass.setHint("Password");
        inputPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(inputPass);

        builder.setView(layout);

        builder.setPositiveButton("Login", (dialog, which) -> {
            username = inputUser.getText().toString();
            password = inputPass.getText().toString();
            loadWebViewWithAuth();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void loadWebViewWithAuth() {
        String credentials = username + ":" + password;
        final String authHeader = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", authHeader);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                request.getRequestHeaders().put("Authorization", authHeader);
                return super.shouldInterceptRequest(view, request);
            }
        });

        webView.loadUrl(url, headers);
    }
}
