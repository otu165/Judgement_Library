package com.example.judgement.feature.navigation.scrap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.data.ScrapRvData

class ScrapRvAdapter(
    private val context: Context,
    private val data: MutableList<ScrapRvData>): RecyclerView.Adapter<ScrapRvVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapRvVH {
        val view: View = LayoutInflater.from(context).inflate(R.layout.rv_scrap_item, parent, false)
        return ScrapRvVH(view)
    }

    override fun onBindViewHolder(holder: ScrapRvVH, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}