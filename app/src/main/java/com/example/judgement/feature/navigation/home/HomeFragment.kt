package com.example.judgement.feature.navigation.home

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.api.NaverAPI
import com.example.judgement.data.HomeRvData
import com.example.judgement.data.NaverNewsData
import com.example.judgement.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var rvAdapter: HomeRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setGraph()
        setRecyclerViewForNews()
        setNews()
    }

    private fun setGraph() {
        // TODO attach graph
    }

    @SuppressLint("SetTextI18n")
    private fun setNews() {
        val day = Calendar.DAY_OF_WEEK
        val keyword = resources.getStringArray(R.array.bottom_navigation_category)[day - 1]
        Log.d("TAG", "day : $day"); // ERROR wrong day of week

        binding.txtHomeNews.text = "오늘의 뉴스 (키워드 : $keyword)"

        val api = NaverAPI.create()

        api.getSearchNews("살인범", 10, 1).enqueue(object : Callback<NaverNewsData> {
            override fun onResponse(
                call: Call<NaverNewsData>,
                response: Response<NaverNewsData>
            ) {
                // 성공
                if (response.isSuccessful) {
                    rvAdapter.updateData(response.body()!!.items)
                    rvAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<NaverNewsData>, t: Throwable) {
                // 실패
            }
        })
    }

    private fun setRecyclerViewForNews() {
        val rv = view?.findViewById(R.id.rv_home) as RecyclerView
        rvAdapter = HomeRvAdapter(requireContext())

        rv.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(object: RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
                    outRect.top = 16
                    outRect.bottom = 16
                }
            })
        }

        rvAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}