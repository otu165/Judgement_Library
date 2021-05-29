package com.example.judgement.view.main.scrap

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ToggleButton
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.judgement.R
import com.example.judgement.adapter.ScrapPagerAdapter
import com.example.judgement.adapter.ScrapAdapter
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
    @SuppressLint("SetTextI18n")
    private fun initTabLayout(viewPager: ViewPager2) {
        val tab = binding.tabLayoutScrap

        TabLayoutMediator(tab, viewPager) { tab, position ->
            category = resources.getStringArray(R.array.bottom_navigation_category)
            tab.text = category[position]
        }.attach()

        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (scrapManager.scrapRvAdapters[category[tab?.position!!]]?.getItemViewType(tab.position) == ScrapAdapter.VISIBLE_TYPE) { // 삭제중
                    binding.toggleScrapRemove.isChecked = true
                    binding.txtScrapList.text = "삭제"
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

    @SuppressLint("SetTextI18n")
    private fun initRvItemDelete() {
        binding.viewPagerScrap.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (scrapManager.scrapRvAdapters[category[position]]?.getItemViewType(position) == ScrapAdapter.VISIBLE_TYPE) { // 삭제중
                    binding.toggleScrapRemove.isChecked = true
                    binding.txtScrapList.text = "삭제"
                } else {
                    binding.toggleScrapRemove.isChecked = false
                    binding.txtScrapList.text = "목록"
                }
            }
        })

        // 삭제 click listener
        binding.toggleScrapRemove.setOnClickListener {
            val position = binding.viewPagerScrap.currentItem

            if ((it as ToggleButton).isChecked) {
                scrapManager.scrapRvAdapters[category[position]]?.setItemViewType(ScrapAdapter.VISIBLE_TYPE)  // 레이아웃 갱신
                binding.txtScrapList.text = "삭제"
            } else {
                // 삭제 아이템 개수 > 0
                if (scrapManager.scrapRvAdapters[category[position]]!!.toDelete.size > 0) {
                    removeDataFromRv(category[position]) // 삭제 요청
                    scrapManager.scrapRvAdapters[category[position]]!!.toDelete.clear() // 리스트 초기화
                }

                scrapManager.scrapRvAdapters[category[position]]?.setItemViewType(ScrapAdapter.GONE_TYPE)
                binding.txtScrapList.text = "목록"
            }
        }
    }
    
    private fun removeDataFromRv(category: String) {
        // TODO request delete data to Server
        val data = scrapManager.scrapRvAdapters[category]?.toDelete // 삭제할 스크랩 리스트
        scrapManager.scrapRvAdapters[category]?.notifyDataSetChanged()
        Toast.makeText(context, "$data 삭제를 요청합니다.", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "ScrapFragment"
    }
}
