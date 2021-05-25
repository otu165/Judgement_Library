package com.example.judgement.feature.navigation.scrap

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.judgement.R
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

        initViewPagerWithTabLayout()

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
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // TODO refresh
            }
        })

        // TODO add border to tab and widen each tab size
    }

    private fun removeScrapItem() {
        Log.d("TAG", "clicked removeScrapItem function");
    }

    companion object {
        private const val TAG = "ScrapFragment"
    }
}
