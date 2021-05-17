package com.example.judgement.feature.law.search_result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.judgement.R
import com.example.judgement.data.SearchResultRvData
import kotlinx.android.synthetic.main.activity_search_result.*

class SearchResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        // search keyword 받기
        intent.getStringExtra("keyword")?.apply {
            txt_search_result_keyword.text = this
        }

        img_search_result_back.setOnClickListener {
            this.finish()
        }

        setupSpinner()
        setupRecyclerView()
    }

    private fun setupSpinner() {
        spinner_search_result.adapter = ArrayAdapter(
            applicationContext,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.spinner)
        )

        spinner_search_result.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.d(TAG, "selected spinner item : ${p2}");
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun setupRecyclerView() {
        val rvAdapter = SearchResultRvAdapter(applicationContext, getTempRvData())
        rv_search_result.adapter = rvAdapter
        rv_search_result.layoutManager = LinearLayoutManager(applicationContext)
        rv_search_result.addItemDecoration(
            DividerItemDecoration(
                applicationContext,
                LinearLayoutManager(applicationContext).orientation
            )
        )

        rvAdapter.notifyDataSetChanged()
    }

    private fun getTempRvData(): MutableList<SearchResultRvData> {
        val data = mutableListOf<SearchResultRvData>()

        for (i in 0..10) {
            data.add(i, SearchResultRvData("\'부모 욕해봐\' 패드립 시킨뒤 죽도록 폭행", "2020도 9337", false))
        }

        return data
    }

    companion object {
        private const val TAG = "SearchResultActivity"
    }
}