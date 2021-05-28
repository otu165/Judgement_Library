package com.example.judgement.ui.navigation.category

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R

class CategoryRvAdapter(private val context: Context, private val data: Array<String>): RecyclerView.Adapter<CategoryRvVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryRvVH {
        val view: View = LayoutInflater.from(context).inflate(R.layout.rv_category_item, parent, false)
        return CategoryRvVH(view)
    }

    override fun onBindViewHolder(holder: CategoryRvVH, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}