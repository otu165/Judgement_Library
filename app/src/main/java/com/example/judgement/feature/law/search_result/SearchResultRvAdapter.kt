package com.example.judgement.feature.law.search_result

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.data.SearchResultRvData

class SearchResultRvAdapter(val context: Context, val data: MutableList<SearchResultRvData>): RecyclerView.Adapter<SearchResultRvVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultRvVH {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_search_result_item, parent, false)
        return SearchResultRvVH(view)
    }

    override fun onBindViewHolder(holder: SearchResultRvVH, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}