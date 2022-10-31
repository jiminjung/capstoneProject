package com.example.firsttest.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.preference.PreferenceManager;

import com.example.firsttest.databinding.ActivityEmergencyLiveBinding;

public class EmergencyLiveActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityEmergencyLiveBinding binding;
    private WebSettings webSettings;
    SharedPreferences prefs;
    private final String TAG = "MyFirebaseMsgService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmergencyLiveBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        webSettings = binding.liveStreaming.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        webSettings.setSupportZoom(false); // 화면 줌 허용 여부
        webSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        webSettings.setUseWideViewPort(true);
        binding.liveStreaming.setInitialScale(1);

        Intent intent = getIntent();
        String userIP = intent.getStringExtra("userIP");
        String id = prefs.getString("id", " ");

        binding.liveStreaming.setWebChromeClient(new WebChromeClient());
        binding.liveStreaming.loadUrl("http://"+userIP+":9090/?"+"id=" + id);

        binding.liveStreaming.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                view.loadUrl(url);
                return false; // then it is not handled by default action
            }
        });

        //뒤로가기 버튼 클릭
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //메인 액티비티로 이동
                Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
                prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String userId = prefs.getString("id", " ");
                intent.putExtra("id", userId);
                startActivity(intent);
            }
        });

        //간편 신고 기능
        binding.report.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EmergencyLiveActivity.this);
            builder.setTitle("신고");
            builder.setMessage("신고하시겠습니까?");
            builder.setPositiveButton("네", (dialogInterface, i) -> {
                Intent intent2 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:119"));
                startActivity(intent2);
            });
            builder.setNegativeButton("아니오", (dialogInterface, i) -> {
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }
}



