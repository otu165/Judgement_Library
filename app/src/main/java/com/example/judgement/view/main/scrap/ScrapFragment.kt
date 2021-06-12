package com.example.judgement.view.main.scrap

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.ToggleButton
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.judgement.R
import com.example.judgement.adapter.ScrapAdapter
import com.example.judgement.api.ServerAPI
import com.example.judgement.databinding.FragmentScrapBinding
import com.example.judgement.extension.logd
import com.example.judgement.util.MyPreference
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

    override fun onResume() {
        super.onResume()
        pagerAdapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPagerWithTabLayout()
        initRvItemDelete()
    }

    private fun initViewPagerWithTabLayout() {
        category = resources.getStringArray(R.array.bottom_navigation_category).plus("기타")

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
            category = resources.getStringArray(R.array.bottom_navigation_category).plus("기타")
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

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
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
                if (scrapManager.scrapRvAdapters[category[position]]!!.toDelete.isNotEmpty()) {
                    val length = scrapManager.scrapRvAdapters[category[position]]!!.toDelete.values.size
                    for ((idx, j_serial)  in scrapManager.scrapRvAdapters[category[position]]!!.toDelete.values.withIndex()) {
                        removeDataFromRv(idx, j_serial, length) // 삭제 요청
                    }

                    scrapManager.scrapRvAdapters[category[position]]!!.toDelete.clear() // 리스트 초기화
                }
                scrapManager.scrapRvAdapters[category[binding.viewPagerScrap.currentItem]]?.setItemViewType(ScrapAdapter.GONE_TYPE)
                binding.txtScrapList.text = "목록"
            }
        }
    }
    
    private fun removeDataFromRv(idx: Int, j_serial: String, length: Int) {
        val call = ServerAPI.server.removeScrap(
            MyPreference.prefs.getString("id", ""),
            j_serial
        )
        Log.d("Scrap", "removeDataFromRv: id: ${MyPreference.prefs.getString("id", "")}, j_serial: $j_serial")

        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                if (idx == length - 1) {
                    logd("실행됨")
                    Log.d("Scrap", "onResponse: here")
                    scrapManager.scrapRvAdapters[category[binding.viewPagerScrap.currentItem]]?.setItemViewType(ScrapAdapter.GONE_TYPE)
                    pagerAdapter.notifyDataSetChanged()
                }
            }
        })
    }
}
