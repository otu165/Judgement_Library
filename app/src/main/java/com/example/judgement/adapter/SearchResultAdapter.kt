package com.example.judgement.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.api.ServerAPI
import com.example.judgement.data.SearchResultData
import com.example.judgement.extension.logd
import com.example.judgement.util.MyPreference
import com.example.judgement.view.detail_result.DetailResultActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultAdapter(
    val context: Context,
    val pos: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data: MutableList<SearchResultData?> = mutableListOf()

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
            holder.bind(data[position]!!)
        }
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int =
        if (data[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM

    /* ViewHolder for item */
    inner class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txt_search_result_title)
        val description: TextView = view.findViewById(R.id.txt_search_result_description)

        fun bind(data: SearchResultData) {
            title.text = data.title
            description.text = data.description

            view.setOnClickListener {
                val intent =
                    Intent(view.context.applicationContext, DetailResultActivity::class.java)
                        .putExtra("precId", data.serialNum)
                        .putExtra("title", data.title)
                        .putExtra("pos", pos)
                        .putExtra("description", data.description)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                view.context.startActivity(intent)
            }
        }
    }

    /* ViewHolder for loading */
    inner class LoadingViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    companion object {
        private const val VIEW_TYPE_ITEM = 0
        private const val VIEW_TYPE_LOADING = 1
    }
}