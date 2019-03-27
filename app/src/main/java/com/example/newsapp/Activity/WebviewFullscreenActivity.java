package com.example.newsapp.Activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.newsapp.R;

public class WebviewFullscreenActivity extends AppCompatActivity {

    private Activity activity;
    private WebView wv_webViewActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_fullscreen);

        initViews();
    }

    private void initViews() {

        activity = WebviewFullscreenActivity.this;
        wv_webViewActivity = findViewById(R.id.wv_webViewActivity);

        String contentURL = getIntent().getStringExtra("contentURL");
        wv_webViewActivity.loadUrl(contentURL);
    }


}
