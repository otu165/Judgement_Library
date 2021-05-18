package com.example.judgement.feature.law.search_result

import android.view.View
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.data.SearchResultRvData

class SearchResultRvVH(val view: View): RecyclerView.ViewHolder(view) {
    val title = view.findViewById<TextView>(R.id.txt_search_result_title)
    val description = view.findViewById<TextView>(R.id.txt_search_result_description)
    val scrap = view.findViewById<ToggleButton>(R.id.toggle_search_result_scrap)

    fun bind(data: SearchResultRvData) {
       title.text = data.title
       description.text = data.description
       if (data.scrap) {
           scrap.toggle()
       }
    }
}