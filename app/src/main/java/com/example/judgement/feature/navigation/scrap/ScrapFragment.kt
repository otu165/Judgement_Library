package com.example.judgement.feature.navigation.scrap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.data.ScrapRvData

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

        // recycler view setup
        val rv = view?.findViewById(R.id.rv_scrap) as RecyclerView
        val tempData = mutableListOf<ScrapRvData>( // Todo 실제 서버 데이터로 수정
            ScrapRvData("믿을 수 없는 사건", "인천시의 한 아파트에서 싸늘한 주검..", "21.01.19"),
            ScrapRvData("믿을 수 없는 사건", "인천시의 한 아파트에서 싸늘한 주검..", "21.01.19"),
            ScrapRvData("믿을 수 없는 사건", "인천시의 한 아파트에서 싸늘한 주검..", "21.01.19"),
            ScrapRvData("믿을 수 없는 사건", "인천시의 한 아파트에서 싸늘한 주검..", "21.01.19"),
            ScrapRvData("믿을 수 없는 사건", "인천시의 한 아파트에서 싸늘한 주검..", "21.01.19"),
            ScrapRvData("믿을 수 없는 사건", "인천시의 한 아파트에서 싸늘한 주검..", "21.01.19"))
        val rvAdapter = ScrapRvAdapter(requireContext(), tempData)

        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(requireContext())

        rvAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "ScrapFragment"
    }
}