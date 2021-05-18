package com.example.judgement.feature.navigation.home

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.example.judgement.R
import com.example.judgement.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news)
        binding.activityNews = this

        intent.getStringExtra("address")?.apply {
            setupWebView(this)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(address: String) {
        binding.webViewNews.apply {
            // 자바스크립트 허용
            settings.javaScriptEnabled = true

            // 새 창 뜨기 방지
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()

            // 주소 연결
            loadUrl(address)
        }
    }

    override fun onBackPressed() {
        if (binding.webViewNews.canGoBack()) {
            binding.webViewNews.goBack()
        } else {
            super.onBackPressed()
        }
    }
}