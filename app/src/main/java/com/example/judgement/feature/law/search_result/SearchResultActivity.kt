package com.example.judgement.feature.law.search_result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.judgement.R
import com.example.judgement.data.SearchResultRvData
import com.example.judgement.databinding.ActivitySearchResultBinding

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

        binding.spinnerSearchResult.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.d(TAG, "selected spinner item : ${p2}");
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun setupRecyclerView() {
        val rvAdapter = SearchResultRvAdapter(applicationContext, getTempRvData())
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