package com.example.judgement.feature.navigation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.data.HomeRvData

class HomeFragment : Fragment() {
    private lateinit var mainView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_home, container, false)
        return mainView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO attach graph

        // news list setup
        val rv = view?.findViewById(R.id.rv_home) as RecyclerView
        val tempData = mutableListOf<HomeRvData>( // TODO replace it to server data
            HomeRvData("살인 피의자 구속", "피의자 영장... 스토킹 정황 수사 경찰, \'큰 딸 A씨 스토킹 피해\' 진술 .."),
            HomeRvData("살인 피의자 구속", "피의자 영장... 스토킹 정황 수사 경찰, \'큰 딸 A씨 스토킹 피해\' 진술 .."),
            HomeRvData("살인 피의자 구속", "피의자 영장... 스토킹 정황 수사 경찰, \'큰 딸 A씨 스토킹 피해\' 진술 .."),
            HomeRvData("살인 피의자 구속", "피의자 영장... 스토킹 정황 수사 경찰, \'큰 딸 A씨 스토킹 피해\' 진술 .."),)
        val rvAdapter = HomeRvAdapter(requireContext(), tempData)

        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(requireContext())

        rvAdapter.notifyDataSetChanged()
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}