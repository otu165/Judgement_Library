package com.example.judgement.feature.navigation.home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.judgement.R
import com.example.judgement.data.HomeRvData

class HomeRvVH(private val view: View): RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.txt_home_news_title)
    private val description: TextView = view.findViewById(R.id.txt_home_news_description)
    private val thumbnail: ImageView = view.findViewById(R.id.img_home_news)

    fun bind(data: HomeRvData) {
        title.text = data.title
        description.text = data.description
        Glide.with(view).load(data.imageUrl).centerCrop().into(thumbnail)
    }
}