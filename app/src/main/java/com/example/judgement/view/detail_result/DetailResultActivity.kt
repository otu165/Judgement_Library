package com.example.judgement.view.detail_result

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.judgement.R
import com.example.judgement.databinding.ActivityDetailResultBinding
import com.example.judgement.extension.logd
import com.example.judgement.view.main.home.news.NewsActivity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class DetailResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailResultBinding
    private lateinit var async: MyAsyncTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.enter_from_bottom, R.anim.none)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_result)
        binding.activity = this

        setOnClickListener()
        async = MyAsyncTask()
        async.execute()

        // TODO 사용자의 스크랩여부에 따라 토글 버튼 Checked 여부 변경하기
    }

    private fun setOnClickListener() {
        binding.imgDetailResultBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.exit_to_bottom)
        }

        binding.txtDetailResultOriginal.setOnClickListener {
            val address = "https://www.law.go.kr/LSW/precInfoP.do?precSeq=${async.precId}&amp;mode=0"
            startActivity(Intent(this, NewsActivity::class.java)
                .putExtra("address", address))
        }
    }

    //AsyncTask 정의
    inner class MyAsyncTask: AsyncTask<String, String, ArrayList<String>>(){ //input, progress update type, result type
        lateinit var precId : String  // 법령 일련 번호
        lateinit var title : String // 사건명

        var judgementList: ArrayList<String> = arrayListOf()

        override fun onPreExecute() { // background스레드를 실행하기전 준비 단계
            super.onPreExecute()
            precId = intent.getStringExtra("precId") ?: ""
            title = intent.getStringExtra("title") ?: ""
            logd("precId : $precId")
            logd("title : $title")
        }

        override fun doInBackground(vararg params: String?): ArrayList<String> { // background 스레드로 일처리를 해주는 곳(UI 스레드와 별개로 작동)
            // 웹에서 내용을 가져온다.
            val doc: Document = Jsoup.connect("https://www.law.go.kr/LSW/precInfoP.do?precSeq=$precId&amp;mode=0").get()
            doc.select("br").append("\n");

            // 내용 중에서 원하는 부분을 가져온다.

            // 사건번호
            val case: Elements = doc.select("div.subtit1")

            // 판시사항, 판결요지
            val eltsSummary: Elements = doc.select("p.pty4")

            // 재판장
            val judge: Elements = doc.select("div.pgroup div")

            //  사건번호, 판시사항, 판결요지, 원심판결, 주문, 이유, 재판장
            judgementList.add(case.text())
            judgementList.add(eltsSummary[0].text())
            judgementList.add(eltsSummary[1].text())
            judgementList.add(judge.text())

            return judgementList
        }

        @SuppressLint("ResourceType")
        @RequiresApi(Build.VERSION_CODES.M)
        override fun onPostExecute(result: ArrayList<String>?) { // background Thread가 일을 끝마치고 result 리턴
            // 문서제목 출력
            if (result != null) {
                binding.txtDetailResultTitle.text = title // 사건명
                binding.txtDetailResultTitle.setBackgroundColor(Color.TRANSPARENT)
                binding.txtDetailResultCaseDescription.text = result[0] // 사건번호
                binding.txtDetailResultCaseDescription.setBackgroundColor(Color.TRANSPARENT)
                binding.txtDetailResultCaseIssueDescription.text = result[1] // 판시사항
                binding.txtDetailResultCaseIssueDescription.setBackgroundColor(Color.TRANSPARENT)
                binding.txtDetailResultCaseSummaryDescription.text = result[2] // 판결요지
                binding.txtDetailResultCaseSummaryDescription.setBackgroundColor(Color.TRANSPARENT)
                binding.txtDetailResultCaseJudgeDescription.text = result[3] // 재판장
                binding.txtDetailResultCaseJudgeDescription.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

}