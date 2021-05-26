package com.example.judgement.feature.navigation.scrap

import android.os.Build
import android.os.Build.*
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.core.view.allViews
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.judgement.R
import com.example.judgement.databinding.FragmentScrapBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class ScrapFragment : Fragment() {

    private lateinit var binding: FragmentScrapBinding
    private lateinit var category: Array<String>
    private lateinit var pagerAdapter: ScrapPagerAdapter
    private var scrapManager = ScrapManager()

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
        initRvItemDelete()
    }

    private fun initViewPagerWithTabLayout() {
        category = resources.getStringArray(R.array.bottom_navigation_category)

        val viewPager = binding.viewPagerScrap
        pagerAdapter = ScrapPagerAdapter(requireContext(), category, scrapManager)

        viewPager.adapter = pagerAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        initTabLayout(viewPager)
    }

    private fun initTabLayout(viewPager: ViewPager2) {
        val tab = binding.tabLayoutScrap

        TabLayoutMediator(tab, viewPager) { tab, position ->
            category = resources.getStringArray(R.array.bottom_navigation_category)
            tab.text = category[position]
        }.attach()

        tab.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (scrapManager.scrapRvAdapters[category[tab?.position!!]]?.getItemViewType(tab.position) == ScrapRvAdapter.VISIBLE_TYPE) { // 삭제중
                    binding.toggleScrapRemove.isChecked = true
                    binding.txtScrapList.text = "선택된 판례 : 0개"
                } else {
                    binding.toggleScrapRemove.isChecked = false
                    binding.txtScrapList.text = "목록"
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun initRvItemDelete() {
        binding.viewPagerScrap.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (scrapManager.scrapRvAdapters[category[position]]?.getItemViewType(position) == ScrapRvAdapter.VISIBLE_TYPE) { // 삭제중
                    binding.toggleScrapRemove.isChecked = true
                    binding.txtScrapList.text = "선택된 판례 : 0개"
                } else {
                    binding.toggleScrapRemove.isChecked = false
                    binding.txtScrapList.text = "목록"
                }
            }
        })

        // 삭제 click listener
        binding.toggleScrapRemove.setOnClickListener {
            val position = binding.viewPagerScrap.currentItem

            if((it as ToggleButton).isChecked) {
                scrapManager.scrapStatus[category[position]] = true  // 상태 저장
                scrapManager.scrapRvAdapters[category[position]]?.setItemViewType(ScrapRvAdapter.VISIBLE_TYPE)  // 레이아웃 갱신
                binding.txtScrapList.text = "선택된 판례 : 0개"
            } else {
                scrapManager.scrapStatus[category[position]] = false
                scrapManager.scrapRvAdapters[category[position]]?.setItemViewType(ScrapRvAdapter.GONE_TYPE)
                binding.txtScrapList.text = "목록"
            }
        }
    }

    companion object {
        private const val TAG = "ScrapFragment"
    }
}
