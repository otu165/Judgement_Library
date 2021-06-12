package com.example.judgement.view.detail_result

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.judgement.R
import com.example.judgement.api.ServerAPI
import com.example.judgement.databinding.ActivityDetailResultBinding
import com.example.judgement.extension.logd
import com.example.judgement.util.MyPreference
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

        binding.toggleDetailResultScrap.setOnClickListener {
            if ((it as ToggleButton).isChecked) {  // 스크랩 해제
                addScrap()
            } else {  // 스크랩 추가
                removeScrap()
            }
        }
    }

    private fun addScrap() {
        logd("requestScrap")
        val call = ServerAPI.server.addScrap(
            MyPreference.prefs.getString("id", ""), // 사용자 아이디
            intent.getStringExtra("pos")?:"", // 카테고리 번호
            intent.getStringExtra("description")?:"", // 형량 2020노
            intent.getStringExtra("title")?:"", // 제목
            intent.getStringExtra("precId")?:"" // 216543
        )

        call.enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    logd("${response.body()}")
                } else {
                    logd("fail")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                logd("error : $t")
            }
        })
    }

    private fun removeScrap() {
        val call = ServerAPI.server.removeScrap(
            MyPreference.prefs.getString("id", ""),
            intent.getStringExtra("precId")?:""
        )

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    logd("successful")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                logd("onFailure : $t")
            }
        })
    }

    //AsyncTask 정의
    inner class MyAsyncTask: AsyncTask<String, String, ArrayList<String>>(){ //input, progress update type, result type
        lateinit var precId : String  // 법령 일련 번호
        lateinit var title : String // 사건명
        lateinit var eltsOriginContents : String // 전체 원문

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
            val outputSettings = Document.OutputSettings()
            outputSettings.prettyPrint(false)

            // 내용 중에서 원하는 부분을 가져온다.
            // 사건번호
            val case: Elements = doc.select("div.subtit1")

            // 전체 내용 출력
            val eltsOrigin: Elements = doc.select("div.pgroup")

            eltsOriginContents = eltsOrigin.html().replace("&nbsp;", " ")

            //  사건번호
            judgementList.add(case.text())

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

                binding.txtDetailResultCaseIssueDescription.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(eltsOriginContents, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    Html.fromHtml(eltsOriginContents)
                }
               // binding.txtDetailResultCaseIssueDescription.movementMethod = LinkMovementMethod.getInstance()
                binding.txtDetailResultCaseIssueDescription.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }
}