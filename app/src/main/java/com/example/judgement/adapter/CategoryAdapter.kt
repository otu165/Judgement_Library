package com.example.judgement.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.view.search_result.SearchResultActivity

class CategoryAdapter(
    private val context: Context,
    private val data: Array<String>
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.rv_category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    /* ViewHolder */
    inner class CategoryViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
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
}