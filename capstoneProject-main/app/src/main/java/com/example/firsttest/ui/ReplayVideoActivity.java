package com.example.firsttest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.firsttest.databinding.ActivityReplayVideoBinding;

public class ReplayVideoActivity extends AppCompatActivity {
    private ActivityReplayVideoBinding binding;
    private WebSettings webSettings;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReplayVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        webSettings = binding.webvReplay.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        webSettings.setSupportZoom(false); // 화면 줌 허용 여부
        webSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        webSettings.setUseWideViewPort(true);
        binding.webvReplay.setInitialScale(1);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        Log.d("ReplayVideoActivity", url + "유알엘");

        //intent로 url 받기
        binding.webvReplay.setWebChromeClient(new WebChromeClient());
        binding.webvReplay.loadUrl(url);

        binding.webvReplay.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                view.loadUrl(url);
                return false; // then it is not handled by default action
            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ReplayListActivity.class));
            }
        });
    }
}
