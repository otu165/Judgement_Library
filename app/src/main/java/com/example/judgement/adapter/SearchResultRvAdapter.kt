package com.example.judgement.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.data.SearchResultRvData
import com.example.judgement.view.detail_result.DetailResultActivity

class SearchResultRvAdapter(
    val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data: MutableList<SearchResultRvData?> = mutableListOf()

    // item ViewHolder
    inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txt_search_result_title)
        val description: TextView = view.findViewById(R.id.txt_search_result_description)
        val scrap: ToggleButton = view.findViewById(R.id.toggle_search_result_scrap)

        fun bind(data: SearchResultRvData, position: Int) {
            title.text = data.title
            description.text = data.description
            if (data.scrap) {
                scrap.toggle()
            }

            view.setOnClickListener {
                Log.d("SearchResultRvVH", "view clicked");
                val intent =
                    Intent(view.context.applicationContext, DetailResultActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                view.context.startActivity(intent)
            }
        }
    }

    // loading ViewHolder
    inner class LoadingViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.rv_search_result_item, parent, false)
            ItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.rv_search_result_loading, parent, false)
            LoadingViewHolder(view)
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bind(data[position]!!, position)
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int =
        if (data[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }
}