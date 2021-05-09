package com.example.judgement.feature.navigation.scrap

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.data.ScrapRvData

class ScrapRvVH(view: View): RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.txt_scrap_title)
    private val description: TextView = view.findViewById(R.id.txt_scrap_description)
    private val date: TextView = view.findViewById(R.id.txt_scrap_date)

    fun bind(data: ScrapRvData) {
        title.text = data.title
        description.text = data.description
        date.text = data.date
    }
}