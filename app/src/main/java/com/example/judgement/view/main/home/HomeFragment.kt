package com.example.judgement.view.main.home

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
import com.example.judgement.adapter.HomeAdapter
import com.example.judgement.api.NaverAPI
import com.example.judgement.data.GetFilteredNewsItems
import com.example.judgement.data.NaverNewsData
import com.example.judgement.databinding.FragmentHomeBinding
import com.example.judgement.extension.logd
import com.example.judgement.view.search_result.SearchResultActivity
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
    private lateinit var rvAdapter: HomeAdapter

    private lateinit var keyword: String

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initSearchBar()
        initGraph()
        initNews()
    }

    // ERROR ??? ?????? history ?????? ?????? -> library ???????????? ?????? ???????????? ??????
    private fun initSearchBar() {
        binding.searchBarHome.apply {
            // ?????? ?????? ?????????
            setOnSearchActionListener(object : MaterialSearchBar.OnSearchActionListener {
                override fun onSearchStateChanged(enabled: Boolean) {}

                override fun onSearchConfirmed(text: CharSequence?) {
                    if (text.isNullOrEmpty() || text.isNullOrBlank()) {
                        Toast.makeText(requireContext(), "???????????? ???????????????.", Toast.LENGTH_SHORT).show()
                    } else {
                        Intent(requireContext(), SearchResultActivity::class.java)
                            .putExtra("keyword", text.toString())
                            .putExtra("position", 8)
                            .let {
                                startActivity(it)
                            }
                        closeSearch()
                    }
                }

                override fun onButtonClicked(buttonCode: Int) {}
            })

            // ?????? history ?????? ?????????
            setSuggestionsClickListener(object : SuggestionsAdapter.OnItemViewClickListener {
                override fun OnItemClickListener(position: Int, v: View?) {
                    Intent(requireContext(), SearchResultActivity::class.java)
                        .putExtra("keyword", this@apply.lastSuggestions[position].toString())
                        .let {
                            startActivity(it)
                        }
                    closeSearch()
                }

                override fun OnItemDeleteListener(position: Int, v: View?) {}
            })
        }
    }

    private fun initGraph() {
        binding.pieChart.setUsePercentValues(true)

        // data Set
        val entries = ArrayList<PieEntry>()
/*
        entries.add(PieEntry(304472f, "??????"))
        entries.add(PieEntry(165358f, "??????"))
        entries.add(PieEntry(157197f, "??????"))
        entries.add(PieEntry(297f, "??????"))
        entries.add(PieEntry(5433f, "??????"))
        entries.add(PieEntry(250f, "????????????"))
        entries.add(PieEntry(5310f, "??????"))*/

        entries.add(PieEntry(376862f, "??????")) //32802f
        entries.add(PieEntry(32802f, "??????"))
        entries.add(PieEntry(658664f, "??????"))
        entries.add(PieEntry(209911f, "??????"))

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
            centerText = "2020 \n ????????????"
            setEntryLabelColor(Color.BLACK)

            //Animate
            animateY(1400, Easing.EaseInOutQuad)
            animate()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun initNews() {
        initNewsTitle()
        initRecyclerViewForNews()
        requestNewsData()
    }

    private fun initNewsTitle() {
        val day = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul")).get(Calendar.DAY_OF_WEEK)
        keyword = resources.getStringArray(R.array.bottom_navigation_category)[day - 1]
        binding.txtHomeNews.text = "????????? ?????? (????????? : $keyword)"
    }

    private fun requestNewsData() {
        val api = NaverAPI.create()
        api.getSearchNews("$keyword ????????? ?????????", 100, 1).enqueue(object : Callback<NaverNewsData> {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(
                call: Call<NaverNewsData>,
                response: Response<NaverNewsData>
            ) {
                if (response.isSuccessful) {  // ??????
                    rvAdapter.updateData(GetFilteredNewsItems(response.body()!!.items).getSelectedItems())
                    rvAdapter.notifyDataSetChanged()
                } else {
                    logd("onResponse: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<NaverNewsData>, t: Throwable) {
                logd("onFailure: ${t.printStackTrace()}")
            }
        })
    }

    private fun initRecyclerViewForNews() {
        rvAdapter = HomeAdapter(requireContext())

        binding.rvHome.apply {
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
}