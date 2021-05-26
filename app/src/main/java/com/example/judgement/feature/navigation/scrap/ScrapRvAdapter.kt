package com.example.judgement.feature.navigation.scrap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.data.ScrapRvData

class ScrapRvAdapter(
    private val context: Context
) : RecyclerView.Adapter<ScrapRvAdapter.ScrapRvVH>() {
    private var data = listOf<ScrapRvData>()

    inner class ScrapRvVH(val view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.txt_scrap_title)
        private val description: TextView = view.findViewById(R.id.txt_scrap_description)
        private val date: TextView = view.findViewById(R.id.txt_scrap_date)

        fun bind(data: ScrapRvData) {
            title.text = data.title
            description.text = data.description
            date.text = data.date
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapRvVH {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_scrap_item, parent, false)
        return ScrapRvVH(view)
    }

    override fun onBindViewHolder(holder: ScrapRvVH, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun updateData(latestData: List<ScrapRvData>) {
        data = latestData
    }
}