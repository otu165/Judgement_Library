package com.example.judgement.view.detail_result

import android.content.ClipData
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.example.judgement.R
import com.example.judgement.databinding.ActivityDetailResultBinding
import com.example.judgement.extension.logd
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class DetailResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.enter_from_bottom, R.anim.none)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_result)
        binding.activity = this

        setOnClickListener()
        MyAsyncTask().execute()

        // TODO 사용자의 스크랩여부에 따라 토글 버튼 Checked 여부 변경하기
    }

    private fun setOnClickListener() {
        binding.imgDetailResultBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.exit_to_bottom)
        }
    }

    //AsyncTask 정의
    inner class MyAsyncTask: AsyncTask<String, String, ArrayList<String>>(){ //input, progress update type, result type
        lateinit var precId : String  // 법령 일련 번호

        var judgementList: ArrayList<String> = arrayListOf()

        override fun onPreExecute() { // background스레드를 실행하기전 준비 단계
            super.onPreExecute()
            precId = intent.getStringExtra("precId") ?: ""
            logd("precId : $precId")
        }

        override fun doInBackground(vararg params: String?): ArrayList<String> { // background 스레드로 일처리를 해주는 곳(UI 스레드와 별개로 작동)
            // 웹에서 내용을 가져온다.
            val doc: Document = Jsoup.connect("https://www.law.go.kr/LSW/precInfoP.do?precSeq=$precId&amp;mode=0").get()
            doc.select("br").append("\n");

            // 내용 중에서 원하는 부분을 가져온다.
            // 사건번호
            val case: Elements = doc.select("div.subtit1")
            Log.d("elts", case.text()) // 사건번호

            // 판시사항, 판결요지
            val eltsSummary: Elements = doc.select("p.pty4")
            Log.d("elts", eltsSummary[0].text())
            Log.d("elts", eltsSummary[1].text())

            // error 원심판결, 주문, 이유
            val eltsOrigin: Elements = doc.select("p.pty4_dep1")
            Log.d("elts", eltsOrigin[3].text())
            Log.d("elts", eltsOrigin[4].text())
            Log.d("elts", eltsOrigin[5].text())

            val judge: Elements = doc.select("div.pgroup div")
            Log.d("elts", judge.text()) // 재판장
            //Log.d("els", doc.title())

            //  사건번호, 판시사항, 판결요지, 원심판결, 주문, 이유, 재판장
            judgementList.add(case.text())
            judgementList.add(eltsSummary[0].text())
            judgementList.add(eltsSummary[1].text())
            judgementList.add(eltsOrigin[3].text())
            judgementList.add(eltsOrigin[4].text())
            judgementList.add(eltsOrigin[5].text())
            judgementList.add(judge.text())

/*
            elts.forEachIndexed{ index, elem ->
                val a_href = elem.select("title")
                //val thumb_img = elem.select("img").attr("src")
                //val title = elem.select("strong.tit_thumb").text()
                Log.d("index", a_href.toString())

                //var judgement = Item()
            }
*/
            return judgementList
        }

        override fun onPostExecute(result: ArrayList<String>?) { // background Thread가 일을 끝마치고 result 리턴
            // 문서제목 출력
            if (result != null) {
                binding.txtDetailResultTitle.text = result[0]
                binding.txtDetailResultCaseDescription.text = result[0]
                binding.txtDetailResultCaseIssueDescription.text = result[1]
                binding.txtDetailResultCaseSummaryDescription.text = result[2]
                binding.txtDetailResultCaseOriginDescription.text = result[3]
                binding.txtDetailResultCaseDecisionDescription.text = result[4]
                binding.txtDetailResultCaseReasonDescription.text = result[5]
                binding.txtDetailResultCaseJudgeDescription.text = result[6]
            }
//             tv_title.setText(result)

        }
    }

}