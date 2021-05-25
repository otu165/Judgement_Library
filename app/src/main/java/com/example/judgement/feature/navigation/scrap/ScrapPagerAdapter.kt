package com.example.judgement.feature.navigation.scrap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.data.ScrapRvData

class ScrapPagerAdapter(
    private val context: Context,
    private val list: Array<String>
    ): RecyclerView.Adapter<ScrapPagerAdapter.ScrapPagerVH>() {

    inner class ScrapPagerVH(view: View): RecyclerView.ViewHolder(view) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapPagerVH {
        val view: View = LayoutInflater.from(context).inflate(R.layout.scrap_vp_item, parent, false)
        return ScrapPagerVH(view)
    }

    override fun onBindViewHolder(holder: ScrapPagerVH, position: Int) { }

    override fun getItemCount(): Int = list.size
}