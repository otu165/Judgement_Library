package com.example.judgement.feature.navigation.scrap

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.judgement.R
import com.example.judgement.data.ScrapRvData
import com.example.judgement.databinding.FragmentScrapBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ScrapFragment : Fragment() {

    private lateinit var binding: FragmentScrapBinding
    private lateinit var category: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scrap, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // compose UI
        initViewPagerWithTabLayout()
//        initRecyclerView()

        binding.imgScrapRemove.setOnClickListener {
            removeScrapItem()
        }
    }

    private fun initViewPagerWithTabLayout() {
        category = resources.getStringArray(R.array.bottom_navigation_category)

        val viewPager = view?.findViewById(R.id.view_pager_scrap) as ViewPager2
        val pagerAdapter = ScrapPagerAdapter(requireContext(), category)

        viewPager.adapter = pagerAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d(TAG, "onPageSelected: position : $position")
            }
        })

        initTabLayout(viewPager)


    }

    private fun initTabLayout(viewPager: ViewPager2) {
        val tab = view?.findViewById(R.id.tab_layout_scrap) as TabLayout

        TabLayoutMediator(tab, viewPager) { tab, position ->
            category = resources.getStringArray(R.array.bottom_navigation_category)
            tab.text = category[position]
        }.attach()

        tab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // TODO request server data
                Toast.makeText(requireContext(), "request server data ${tab?.text}", Toast.LENGTH_SHORT).show()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        // TODO add border to tab and widen each tab size
    }

    private fun getTempData(): MutableList<ScrapRvData> {
        val tempData = mutableListOf<ScrapRvData>()
        for (idx in 0..10)
            tempData.add(idx, ScrapRvData("믿을 수 없는 사건", "인천시의 한 아파트에서 싸늘한 주검..", "21.01.19"))

        return tempData
    }

    private fun removeScrapItem() {
        Log.d("TAG", "clicked removeScrapItem function");
    }

    companion object {
        private const val TAG = "ScrapFragment"
    }
}
