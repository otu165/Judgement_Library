package com.example.judgement.view.detail_result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.judgement.R
import com.example.judgement.databinding.ActivityDetailResultBinding

class DetailResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.enter_from_bottom, R.anim.none)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_result)
        binding.activity = this

        setOnClickListener()
        // TODO 사용자의 스크랩여부에 따라 토글 버튼 Checked 여부 변경하기
    }

    private fun setOnClickListener() {
        binding.imgDetailResultBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.exit_to_bottom)
        }
    }
}