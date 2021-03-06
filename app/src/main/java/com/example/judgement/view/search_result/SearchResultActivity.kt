package com.example.judgement.view.search_result

import android.os.Bundle
import android.os.Handler
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
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.adapter.SearchResultAdapter
import com.example.judgement.data.SearchResultData
import com.example.judgement.databinding.ActivitySearchResultBinding
import com.example.judgement.extension.logd
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStreamReader
import java.net.URL


class SearchResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var rvAdapter: SearchResultAdapter

    private lateinit var keyword: String
    private lateinit var position: String

    private var isLoading = true
    private var page: Int = 1
    private lateinit var totalCount: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_result)
        binding.activitySearchResult = this

        binding.imgSearchResultBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }

        initUI()
        initSpinner()
        initScrollListener()
    }

    private fun initUI() {
        // search keyword 받기
        intent.getStringExtra("keyword")?.apply { keyword = this }
        binding.txtSearchResultKeyword.text = keyword

        // position 받기
        position = intent.getIntExtra("position", 0).toString()

        // 총 페이지 수 계산 & 검색결과 개수 반영
        totalCount = (getXmlCnt(keyword)).toString()
        binding.textSearchResultNumber.text = totalCount + " 개"

        // 검색 결과에 따라 View 초기화
        if (totalCount.toInt() == 0) {
            binding.rvSearchResult.visibility = View.GONE
            binding.constraintLayoutSearchResultNothing.visibility = View.VISIBLE
        } else {
            initRecyclerView()
        }
    }

    private fun initSpinner() {
        binding.spinnerSearchResult.adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.spinner)
        )

        binding.spinnerSearchResult.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    // TODO request server data
                    logd("selected spinner item : ${p2}");
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    private fun initRecyclerView() {
        rvAdapter = SearchResultAdapter(applicationContext, position)
        getXmlData()

        binding.rvSearchResult.apply {
            setItemViewCacheSize(totalCount.toInt())
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(applicationContext)
            addItemDecoration(
                DividerItemDecoration(
                    applicationContext,
                    LinearLayoutManager(applicationContext).orientation
                )
            )
        }
        rvAdapter.notifyDataSetChanged()

    }

    private fun initScrollListener() {
        binding.rvSearchResult.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (isLoading) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == rvAdapter.data.size - 1 &&
                            rvAdapter.data.size < totalCount.toInt()) {
                        // 리스트의 마지막 아이템인 경우 데이터를 더 불러옴
                        loadData()
                        isLoading = false
                    }
                }
            }
        })
    }

    private fun loadData() {
        // 로딩 시작
        rvAdapter.data.add(null)
        rvAdapter.notifyItemInserted(rvAdapter.data.size - 1)

        // 데이터 불러오기
        val handler = Handler()
        handler.postDelayed({
            // 로딩 끝
            rvAdapter.data.removeAt(rvAdapter.data.size - 1)
            val scrollPosition = rvAdapter.data.size

            rvAdapter.apply {
                notifyItemRemoved(scrollPosition)
                getXmlData()
//                data.addAll(getXmlData())
                notifyDataSetChanged()
            }
            isLoading = true
        }, 1000)
    }

    private fun getXmlData() {
        //네트워크 예외 방지
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // data index
        var name: String? = null
        var caseNum: String? = null
        var serialNum: String? = null

        // 모든 페이지 파싱 -> 한 페이지씩 파싱하도록 변경
        try {
            val queryUrl =
                "https://www.law.go.kr/DRF/lawSearch.do?OC=judy6647&target=prec&type=XML&mobileYn=Y&query=$keyword&page=$page"
            logd("getXmlData: $queryUrl")
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
                            serialNum = Html.fromHtml(xpp.text).toString()
                        }
                    }
                    XmlPullParser.TEXT -> {
                    }
                    XmlPullParser.END_TAG -> {
                        tag = xpp.name //테그 이름 얻어오기
                        if (tag == "prec") {
                            rvAdapter.data.add(
                                SearchResultData(name.toString(), caseNum.toString(), serialNum.toString(),false)
                            )
                        }
                    }
                }
                eventType = xpp.next()
            }
            page++
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("error", e.toString())
        }
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

    override fun onBackPressed() {
        super.onBackPressed()
        if (isFinishing) {
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
        }
    }
}



