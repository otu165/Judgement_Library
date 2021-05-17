package com.example.judgement.feature.navigation.category

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.feature.law.search_result.SearchResultActivity

class CategoryRvVH(private val view: View): RecyclerView.ViewHolder(view) {
    private val txtCategory: TextView = view.findViewById(R.id.rv_category_item)

    fun bind(category: String) {
        txtCategory.text = category

        view.setOnClickListener {
            val intent = Intent(view.context, SearchResultActivity::class.java)
                .putExtra("keyword", category)
            view.context.startActivity(intent)
        }
    }
}