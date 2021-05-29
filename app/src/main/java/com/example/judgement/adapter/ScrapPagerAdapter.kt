package com.example.judgement.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.api.ServerAPI
import com.example.judgement.data.ScrapRvData
import com.example.judgement.view.main.scrap.ScrapManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScrapPagerAdapter(
    private val context: Context,
    private val list: Array<String>,
    val scrapManager: ScrapManager
) : RecyclerView.Adapter<ScrapPagerAdapter.ScrapPagerVH>() {

    inner class ScrapPagerVH(val view: View) : RecyclerView.ViewHolder(view) {
        val rv: RecyclerView = view.findViewById(R.id.rv_scrap)

        fun initRecyclerView(category: String) {
            val rvAdapter = ScrapRvAdapter(view.context, scrapManager)
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

            requestScrapData(category, rvAdapter)
        }

        private fun requestScrapData(category: String, adapter: ScrapRvAdapter) {
            val call: Call<List<ScrapRvData>> = ServerAPI.server.getScrap(category)

            call.enqueue(object : Callback<List<ScrapRvData>> {
                override fun onResponse(
                    call: Call<List<ScrapRvData>>,
                    response: Response<List<ScrapRvData>>
                ) {
                    if (response.isSuccessful) {
                        adapter.updateData(response.body()!!)
                    } else {
                        // Todo delete
                        adapter.updateData(getTempData())
                    }
                }

                override fun onFailure(call: Call<List<ScrapRvData>>, t: Throwable) {
                    Log.d("requestScrapData", "onFailure: $t")
                }
            })
        }

        private fun getTempData() = listOf(
            ScrapRvData("제목", "설명", "날짜"),
            ScrapRvData("제목", "설명", "날짜"),
            ScrapRvData("제목", "설명", "날짜"),
            ScrapRvData("제목", "설명", "날짜"),
            ScrapRvData("제목", "설명", "날짜"),
            ScrapRvData("제목", "설명", "날짜"),
            ScrapRvData("제목", "설명", "날짜"),
            ScrapRvData("제목", "설명", "날짜"),
            ScrapRvData("제목", "설명", "날짜"),
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapPagerVH {
        val view: View = LayoutInflater.from(context).inflate(R.layout.scrap_vp_item, parent, false)
        return ScrapPagerVH(view)
    }

    override fun onBindViewHolder(holder: ScrapPagerVH, position: Int) {
        holder.initRecyclerView(list[position])
    }

    override fun getItemCount(): Int = list.size
}