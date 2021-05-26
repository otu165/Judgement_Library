package com.example.judgement.feature.law.search_result

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.judgement.R
import com.example.judgement.data.SearchResultRvData
import com.example.judgement.databinding.ActivitySearchResultBinding
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStreamReader
import java.net.URL


class SearchResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_result)
        binding.activitySearchResult = this

        // search keyword 받기
        intent.getStringExtra("keyword")?.apply {
            binding.txtSearchResultKeyword.text = this
        }

        binding.imgSearchResultBack.setOnClickListener {
            this.finish()
        }

        setupSpinner()
        setupRecyclerView()
    }

    private fun setupSpinner() {
        binding.spinnerSearchResult.adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.spinner)
        )

        binding.spinnerSearchResult.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    Log.d(TAG, "selected spinner item : ${p2}");
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    private fun setupRecyclerView() {
        val rvAdapter = SearchResultRvAdapter(applicationContext, getXmlData())
        binding.rvSearchResult.adapter = rvAdapter
        binding.rvSearchResult.layoutManager = LinearLayoutManager(applicationContext)
        binding.rvSearchResult.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                LinearLayoutManager(applicationContext).orientation
            )
        )

        rvAdapter.notifyDataSetChanged()
    }
/*
    private fun getTempRvData(): MutableList<SearchResultRvData> {

        val data = mutableListOf<SearchResultRvData>()

        for (i in 0..10) {
            data.add(i, SearchResultRvData("\'부모 욕해봐\' 패드립 시킨뒤 죽도록 폭행", "2020도 9337", false))
        }

        return data
    }
*/
    companion object {
        private const val TAG = "SearchResultActivity"
    }

    private fun getXmlData(): MutableList<SearchResultRvData> {
        //네트워크 예외 방지
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // 총 페이지 수 계산
        var keyword: String = intent.getStringExtra("keyword")
        var totalCount: String = (getXmlCnt(keyword)).toString()
        var pageCnt: Int = totalCount.toInt() / 20

        binding.textSearchResultNumber.text = totalCount + " 개"

        val data = mutableListOf<SearchResultRvData>()

        // data index
        var index: Int = 0

        var page: Int
        var name: String? = null
        var caseNum: String? = null
        var serialNum: String? = null

        // 모든 페이지 파싱
        for (i in 0..(pageCnt - 1)!!) {
            try {
                page = i + 1
                val queryUrl =
                    "https://www.law.go.kr/DRF/lawSearch.do?OC=judy6647&target=prec&type=XML&mobileYn=Y&query=$keyword&page=$page"
                val url = URL(queryUrl) //문자열로 된 요청 url을 URL 객체로 생성.
                val `is` = url.openStream() //url위치로 입력스트림 연결
                val factory = XmlPullParserFactory.newInstance() //xml파싱을 위한
                var xpp = factory.newPullParser()
                xpp.setInput(InputStreamReader(`is`, "UTF-8")) //inputstream 으로부터 xml 입력받기
                var tag: String? = null
                xpp.next()
                var eventType = xpp.eventType
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    when (eventType) {
                        XmlPullParser.START_TAG -> {
                            tag = xpp.name //테그 이름 얻어오기

                            if (tag == "prec") ;
                            else if (tag == "사건명") {
                                xpp.next()
                                name = Html.fromHtml(xpp.text).toString()
                                Log.d("name", Html.fromHtml(xpp.text).toString())
                            } else if (tag == "사건번호") {
                                xpp.next()
                                caseNum = Html.fromHtml(xpp.text).toString()
                            } else if (tag == "판례일련번호") {
                                xpp.next()
                            }
                        }
                        XmlPullParser.TEXT -> {
                        }
                        XmlPullParser.END_TAG -> {
                            tag = xpp.name //테그 이름 얻어오기
                            if (tag == "prec") {
                                data.add(index++, SearchResultRvData(name.toString(), caseNum.toString()!!, false))
                            }
                        }
                    }
                    eventType = xpp.next()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d("error", e.toString())
            }
        }
        return data
    }


    // API 총 갯수 가져오기
    fun getXmlCnt(query: String): StringBuffer {

        //네트워크 예외 방지
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val buffer = StringBuffer()

        val queryUrl =
            "https://www.law.go.kr/DRF/lawSearch.do?OC=judy6647&target=prec&type=XML&mobileYn=Y&query=$query"
        try {
            val url = URL(queryUrl) //문자열로 된 요청 url을 URL 객체로 생성.
            val `is` = url.openStream() //url위치로 입력스트림 연결
            val factory = XmlPullParserFactory.newInstance() //xml파싱을 위한
            val xpp = factory.newPullParser()
            xpp.setInput(InputStreamReader(`is`, "UTF-8")) //inputstream 으로부터 xml 입력받기
            var tag: String
            xpp.next()
            var eventType = xpp.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        tag = xpp.name //테그 이름 얻어오기

                        if (tag == "PrecSearch") ;
                        else if (tag == "totalCnt") {
                            xpp.next()
                            buffer.append(xpp.text)
                        }
                    }
                    XmlPullParser.TEXT -> {
                    }
                    XmlPullParser.END_TAG -> {
                    }
                }
                eventType = xpp.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("error", e.toString())
        }
        return buffer
    }
}



