package com.example.judgement.feature.navigation.category

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R

class CategoryRvVH(private val view: View): RecyclerView.ViewHolder(view) {
    private val txtCategory: TextView = view.findViewById(R.id.rv_category_item)

    fun bind(category: String) {
        txtCategory.text = category

        // TODO link it to Search_Result_Activity
        view.setOnClickListener {
            Toast.makeText(view.context, "$category clicked", Toast.LENGTH_SHORT).show()
        }
    }
}