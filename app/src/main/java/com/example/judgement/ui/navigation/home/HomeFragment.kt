package com.example.judgement.ui.navigation.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.api.NaverAPI
import com.example.judgement.data.GetFilteredNewsItems
import com.example.judgement.data.NaverNewsData
import com.example.judgement.databinding.FragmentHomeBinding
import com.example.judgement.ui.law.search_result.SearchResultActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS
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
            setSuggestionsClickListener(object : SuggestionsAdapter.OnItemViewClickListener {
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

        binding.pieChart.setUsePercentValues(true)

        // data Set
        val entries = ArrayList<PieEntry>()
/*
        entries.add(PieEntry(304472f, "사기"))
        entries.add(PieEntry(165358f, "절도"))
        entries.add(PieEntry(157197f, "폭행"))
        entries.add(PieEntry(297f, "살인"))
        entries.add(PieEntry(5433f, "마약"))
        entries.add(PieEntry(250f, "아동학대"))
        entries.add(PieEntry(5310f, "강간"))*/

        entries.add(PieEntry(376862f, "교통")) //32802f
        entries.add(PieEntry(32802f, "강력"))
        entries.add(PieEntry(658664f, "재산"))
        entries.add(PieEntry(209911f, "폭력"))

        // add a lot of colors
        val colorsItems = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colorsItems.add(c)
        for (c in COLORFUL_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colorsItems.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colorsItems.add(c)
        colorsItems.add(ColorTemplate.getHoloBlue())


        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.apply {
            colors = colorsItems
            valueTextColor = Color.BLACK
            valueTextSize = 14f
            valueFormatter = PercentFormatter(binding.pieChart)

            // Value lines
            valueLinePart1Length = 0.3f
            valueLinePart2Length = 0.6f
            valueLineWidth = 2f
            valueLinePart1OffsetPercentage = 80.0f  // Line starts outside of chart
            //isUsingSliceColorAsValueLineColor = true
            valueLineColor = Color.BLACK

            // Value text appearance
            yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            //xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            valueTypeface = Typeface.DEFAULT_BOLD
            selectionShift = 5f
        }

        val pieData = PieData(pieDataSet)

        binding.pieChart.apply {
            data = pieData
            description.isEnabled = false
            isRotationEnabled = false
            centerText = "2020 \n 발생건수"
            setEntryLabelColor(Color.BLACK)

            //Animate
            animateY(1400, Easing.EaseInOutQuad)
            animate()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setNews() {
        val day = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul")).get(Calendar.DAY_OF_WEEK)
        val keyword = resources.getStringArray(R.array.bottom_navigation_category)[day - 1]

        binding.txtHomeNews.text = "오늘의 뉴스 (키워드 : $keyword)"

        val api = NaverAPI.create()

        api.getSearchNews("$keyword 피해자 피의자", 100, 1).enqueue(object : Callback<NaverNewsData> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(
                call: Call<NaverNewsData>,
                response: Response<NaverNewsData>
            ) {
                // 성공
                if (response.isSuccessful) {
                    rvAdapter.updateData(GetFilteredNewsItems(response.body()!!.items).getSelectedItems())
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