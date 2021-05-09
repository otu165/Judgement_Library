package com.example.judgement.feature.navigation.category

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R

class CategoryRvVH(view: View): RecyclerView.ViewHolder(view) {
    private val txtCategory: TextView = view.findViewById(R.id.rv_category_item)

    fun bind(category: String) {
        txtCategory.text = category
    }
}