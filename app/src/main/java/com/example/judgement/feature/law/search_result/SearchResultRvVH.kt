package com.example.judgement.feature.law.search_result

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.data.SearchResultRvData
import com.example.judgement.feature.law.DetailResultActivity

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

        view.setOnClickListener {
            Log.d("SearchResultRvVH", "view clicked");
            val intent = Intent(view.context.applicationContext, DetailResultActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            view.context.startActivity(intent)
        }
    }
}