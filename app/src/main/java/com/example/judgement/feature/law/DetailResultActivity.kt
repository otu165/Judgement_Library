package com.example.judgement.feature.law

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ToggleButton
import com.example.judgement.R
import kotlinx.android.synthetic.main.activity_detail_result.*

class DetailResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_result)

        // TODO 사용자의 스크랩여부에 따라 토글 버튼 Checked 여부 변경하기

        img_detail_result_back.setOnClickListener {
            this.finish()
        }


    }

    companion object {
        private const val TAG = "DetailResultActivity"
    }
}