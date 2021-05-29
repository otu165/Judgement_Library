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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_result)
        binding.activity = this

        // TODO 사용자의 스크랩여부에 따라 토글 버튼 Checked 여부 변경하기

        binding.imgDetailResultBack.setOnClickListener {
            this.finish()
        }
    }

    companion object {
        private const val TAG = "DetailResultActivity"
    }
}