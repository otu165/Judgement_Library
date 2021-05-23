package com.example.judgement.feature.navigation.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.api.NaverAPI
import com.example.judgement.data.NaverNewsData
import com.example.judgement.databinding.FragmentHomeBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
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

        entries.add(PieEntry(209911f, "폭력"))
        entries.add(PieEntry(376862f, "교통"))
        entries.add(PieEntry(658664f, "재산"))
        entries.add(PieEntry(32802f, "강력"))

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
            valueTextSize = 16f
            valueFormatter = PercentFormatter(binding.pieChart)

            // Value lines
            valueLinePart1Length = 0.6f
            valueLinePart2Length = 0.3f
            valueLineWidth = 2f
            valueLinePart1OffsetPercentage = 115f  // Line starts outside of chart
            isUsingSliceColorAsValueLineColor = true

            // Value text appearance
            yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            //xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
            valueTypeface = Typeface.DEFAULT_BOLD

            /*valueFormatter = object : ValueFormatter() {
                private val formatter = NumberFormat.getPercentInstance()

                override fun getFormattedValue(value: Float) =
                    formatter.format(value / 100f)
            }*/
        }

        val pieData = PieData(pieDataSet)

        binding.pieChart.apply {
            data = pieData
            description.isEnabled = false
            isRotationEnabled = false
            centerText = "2020 \n 발생건수"

            setEntryLabelColor(Color.BLACK)
            animateY(1400, Easing.EaseInOutQuad)
            animate()
        }

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