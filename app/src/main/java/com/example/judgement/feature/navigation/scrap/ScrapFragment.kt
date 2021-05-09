package com.example.judgement.feature.navigation.scrap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.data.ScrapRvData
import com.google.android.material.tabs.TabLayout

class ScrapFragment : Fragment() {
    private lateinit var scrapView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        scrapView = inflater.inflate(R.layout.fragment_scrap, container, false)
        return scrapView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // tab layout setup
        val tab = view?.findViewById(R.id.tab_layout_scrap) as TabLayout
        for (category in resources.getStringArray(R.array.bottom_navigation_category)) {
            tab.addTab(tab.newTab().setText(category))
        }

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

        // recycler view setup
        val rv = view?.findViewById(R.id.rv_scrap) as RecyclerView
        val tempData = mutableListOf<ScrapRvData>( // TODO replace it to server data
            ScrapRvData("믿을 수 없는 사건", "인천시의 한 아파트에서 싸늘한 주검..", "21.01.19"),
            ScrapRvData("믿을 수 없는 사건", "인천시의 한 아파트에서 싸늘한 주검..", "21.01.19"),
            ScrapRvData("믿을 수 없는 사건", "인천시의 한 아파트에서 싸늘한 주검..", "21.01.19"),
            ScrapRvData("믿을 수 없는 사건", "인천시의 한 아파트에서 싸늘한 주검..", "21.01.19"),
            ScrapRvData("믿을 수 없는 사건", "인천시의 한 아파트에서 싸늘한 주검..", "21.01.19"),
            ScrapRvData("믿을 수 없는 사건", "인천시의 한 아파트에서 싸늘한 주검..", "21.01.19")
        )
        val rvAdapter = ScrapRvAdapter(requireContext(), tempData)

        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager(requireContext()).orientation))

        rvAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "ScrapFragment"
    }
}