package com.rumanweb.quranrecitation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class VideoActivity extends AppCompatActivity {

    NetworkListener networkListener = new NetworkListener();
    public static String videoTitle = "";
    public static String videoId = "";
    ProgressBar progressBarVideoLayout;
    WebView webView;
    TextView tvVideoTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        webView = findViewById(R.id.webView);
        tvVideoTitle = findViewById(R.id.tvVideoTitle);
        progressBarVideoLayout = findViewById(R.id.progressBarVideoLayout);

        Window window = this.getWindow();
        window.setStatusBarColor(this.getResources().getColor(R.color.statusBarColor));

        String iFrameLink = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/"+videoId+"\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            webView.getWebViewClient();
        }
        if (!iFrameLink.isEmpty()) {
            progressBarVideoLayout.setVisibility(View.GONE);
            webView.loadData(iFrameLink,"text/html","utf-8");
            tvVideoTitle.setText(videoTitle);
        }

    }
    @Override
    protected void onStart() {

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {

        unregisterReceiver(networkListener);
        super.onStop();
    }

}