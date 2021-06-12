package com.example.judgement.view.main.scrap

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.adapter.ScrapAdapter
import com.example.judgement.api.ServerAPI
import com.example.judgement.data.ScrapData
import com.example.judgement.extension.logd
import com.example.judgement.util.MyPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScrapPagerAdapter(
    private val context: Context,
    private val list: Array<String>,
    val scrapManager: ScrapManager
) : RecyclerView.Adapter<ScrapPagerAdapter.ScrapPagerViewHolder>() {

    inner class ScrapPagerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val rv: RecyclerView = view.findViewById(R.id.rv_scrap)
        val txt: TextView = view.findViewById(R.id.txt_scrap_nothing)

        fun initRecyclerView(category: String, position: Int) {
            val rvAdapter = ScrapAdapter(view.context, scrapManager)
            scrapManager.scrapRvAdapters[category] = rvAdapter // adapter 저장

            rv.apply {
                adapter = rvAdapter
                layoutManager = LinearLayoutManager(view.context)
                addItemDecoration(
                    DividerItemDecoration(
                        view.context,
                        LinearLayoutManager(view.context).orientation
                    )
                )
            }

            requestScrapData(position, rvAdapter)
        }

        private fun requestScrapData(position: Int, adapter: ScrapAdapter) {
            val call: Call<List<ScrapData>> = ServerAPI.server.getScrap(
                MyPreference.prefs.getString("id", ""),
                (position + 1).toString()
            )

            call.enqueue(object : Callback<List<ScrapData>> {
                override fun onResponse(
                    call: Call<List<ScrapData>>,
                    response: Response<List<ScrapData>>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.apply {
                            adapter.updateData(this)
                        }

                        if (response.body().isNullOrEmpty()) {
                            txt.visibility = View.VISIBLE
                        } else {
                            txt.visibility = View.GONE
                        }
                    }
                }

                override fun onFailure(call: Call<List<ScrapData>>, t: Throwable) {
                    logd("onFailure: $t")
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapPagerViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.scrap_vp_item, parent, false)
        return ScrapPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScrapPagerViewHolder, position: Int) {
        holder.initRecyclerView(list[position], position)
    }

    override fun getItemCount(): Int = list.size

}