package com.example.judgement.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.judgement.R
import com.example.judgement.data.Items
import com.example.judgement.extension.logd
import com.example.judgement.view.WebViewActivity
import com.facebook.shimmer.ShimmerFrameLayout
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.io.IOException

class HomeAdapter(private val context: Context): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    private var data = listOf<Items>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.rv_home_item, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun updateData(serverData: List<Items>) {
        data = serverData
    }

    /* ViewHolder */
    inner class HomeViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.txt_home_news_title)
        private val description: TextView = view.findViewById(R.id.txt_home_news_description)
        private val thumbnail: ImageView = view.findViewById(R.id.img_home_news)
        private val shimmer: ShimmerFrameLayout = view.findViewById(R.id.shimmer_view_container)

        fun bind(data: Items) {
            title.text = stripHtml(data.title)
            description.text = stripHtml(data.description)
            GetThumbnailAsync(data.link).execute()

            view.setOnClickListener {
                val intent = Intent(view.context, WebViewActivity::class.java)
                    .putExtra("address", data.link)

                view.context.startActivity(intent)
            }
        }

        private fun stripHtml(html: String) = Html.fromHtml(html).toString()

        @SuppressLint("StaticFieldLeak")
        inner class GetThumbnailAsync internal constructor(var this_url: String) :
            AsyncTask<Any?, Any?, Any?>() {
            private var image: String = ""

            override fun onPreExecute() {
                shimmer.startShimmer()
            }

            override fun doInBackground(vararg params: Any?): Any? {
                try {
                    val con: org.jsoup.Connection = Jsoup.connect(this_url) as org.jsoup.Connection
                    val doc: Document = con.get()
                    val ogTags: Elements = doc.select("meta[property^=og:]")
                    if (ogTags.size <= 0) {
                        return null
                    }
                    // extract ogTag
                    for (i in 0 until ogTags.size) {
                        val tag: Element = ogTags[i]
                        val text: String = tag.attr("property")

                        if ("og:image" == text) {
                            image = tag.attr("content")
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    logd("doInBackground: error, image : $image")
                }
                return null
            }

            override fun onPostExecute(result: Any?) {
                try {
                    Glide.with(view)
                        .load(image)
                        .error(view.context.resources.getDrawable(R.drawable.error_thumbnail))
                        .override(100, 100)
                        .centerCrop()
                        .into(thumbnail)
                    shimmer.stopShimmer()
                    shimmer.visibility = View.GONE

                    thumbnail.visibility = View.VISIBLE
                } catch (i: IllegalArgumentException) {
                    logd("doInBackground: no thumbnail")
                }
            }
        }
    }
}