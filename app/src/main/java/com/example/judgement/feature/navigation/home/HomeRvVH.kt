package com.example.judgement.feature.navigation.home

import android.content.Intent
import android.os.Build
import android.support.v4.media.MediaBrowserCompat
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.judgement.R
import com.example.judgement.data.HomeRvData
import com.example.judgement.data.Items

class HomeRvVH(private val view: View): RecyclerView.ViewHolder(view) {
    private val title: TextView = view.findViewById(R.id.txt_home_news_title)
    private val description: TextView = view.findViewById(R.id.txt_home_news_description)
    private val thumbnail: ImageView = view.findViewById(R.id.img_home_news)

    fun bind(data: Items) {
        title.text = stripHtml(data.title)
        description.text = stripHtml(data.description)
        // TODO 이미지
//        Glide.with(view).load(data.imageUrl).centerCrop().into(thumbnail)

        view.setOnClickListener {
            val intent = Intent(view.context, NewsActivity::class.java)
                .putExtra("address", data.link)

            view.context.startActivity(intent)
        }
    }

    private fun stripHtml(html: String) = Html.fromHtml(html).toString()
}