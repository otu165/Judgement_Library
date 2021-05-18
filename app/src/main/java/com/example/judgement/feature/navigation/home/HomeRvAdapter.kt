package com.example.judgement.feature.navigation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.data.HomeRvData
import com.example.judgement.data.Items

class HomeRvAdapter(private val context: Context): RecyclerView.Adapter<HomeRvVH>() {
    private var data = listOf<Items>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRvVH {
        val view: View = LayoutInflater.from(context).inflate(R.layout.rv_home_item, parent, false)
        return HomeRvVH(view)
    }

    override fun onBindViewHolder(holder: HomeRvVH, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun updateData(serverData: List<Items>) {
        data = serverData
    }
}