package com.example.judgement.view.main.home.news

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import com.example.judgement.R
import com.example.judgement.databinding.ActivityNewsBinding

/**
 * HomeFragment 에서 사용자가 뉴스를 클릭한 경우 보여줄 WebView
 */

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news)
        binding.activityNews = this

        initWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        intent.getStringExtra("address")?.let {
            binding.webViewNews.apply {
                // 자바스크립트 허용
                settings.javaScriptEnabled = true

                // 새 창 뜨기 방지
                webViewClient = WebViewClient()
                webChromeClient = WebChromeClient()

                // 주소 연결
                loadUrl(it)
            }
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