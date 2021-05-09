package com.example.judgement.feature

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.judgement.R
import com.example.judgement.feature.signin.SignInActivity

class SplashActivity : AppCompatActivity() {

    val SPLASH_VIEW_TIME: Long = 3000 //2초간 스플래시 화면을 보여줌 (ms)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({ //delay를 위한 handler
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }, SPLASH_VIEW_TIME)
    }
}