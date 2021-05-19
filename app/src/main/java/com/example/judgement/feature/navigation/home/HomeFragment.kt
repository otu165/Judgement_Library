package com.example.judgement.feature.navigation.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.api.NaverAPI
import com.example.judgement.data.NaverNewsData
import com.example.judgement.databinding.FragmentHomeBinding
import com.example.judgement.feature.law.search_result.SearchResultActivity
import com.mancj.materialsearchbar.MaterialSearchBar
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initSearchBar()
        setGraph()
        setRecyclerViewForNews()
        setNews()
    }

    // ERROR 첫 번째 history 삭제 안됨 -> library 자체에서 지원 안하는것 확인
    private fun initSearchBar() {

        binding.searchBarHome.apply {
            // 검색 동작 리스너
            setOnSearchActionListener(object :
                MaterialSearchBar.OnSearchActionListener {
                override fun onSearchStateChanged(enabled: Boolean) {
                }

                override fun onSearchConfirmed(text: CharSequence?) {
                    if (text.isNullOrEmpty() || text.isNullOrBlank()) {
                        Toast.makeText(requireContext(), "검색어를 입력하세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        Intent(requireContext(), SearchResultActivity::class.java)
                            .putExtra("keyword", text.toString())
                            .apply {
                                startActivity(this)
                            }
                        closeSearch()
                    }
                }

                override fun onButtonClicked(buttonCode: Int) {
                }
            })

            // 검색 history 클릭 리스너
            setSuggestionsClickListener(object: SuggestionsAdapter.OnItemViewClickListener {
                override fun OnItemClickListener(position: Int, v: View?) {
                    Intent(requireContext(), SearchResultActivity::class.java)
                        .putExtra("keyword", this@apply.lastSuggestions[position].toString())
                        .apply {
                            startActivity(this)
                        }
                    closeSearch()
                }

                override fun OnItemDeleteListener(position: Int, v: View?) {
                }
            })
        }

        // TODO 검색 history 저장 (updateLastSuggestions & setLastSuggestions)
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

        api.getSearchNews(keyword, 10, 1).enqueue(object : Callback<NaverNewsData> {
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
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    itemPosition: Int,
                    parent: RecyclerView
                ) {
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