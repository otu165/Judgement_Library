package com.example.judgement.view.main.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.judgement.R
import com.example.judgement.adapter.CategoryAdapter
import com.example.judgement.databinding.FragmentCategoryBinding
import com.example.judgement.extension.logd
import com.example.judgement.view.main.MainActivity
import com.example.judgement.view.main.home.HomeFragment

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logd("CategoryFragment created")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListener()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val rvAdapter = CategoryAdapter(
            requireContext(),
            resources.getStringArray(R.array.bottom_navigation_category)
        )

        binding.rvCategory.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager(requireContext()).orientation
                )
            )
        }

        rvAdapter.notifyDataSetChanged()
    }

    private fun setOnClickListener() {
        binding.imgCategoryBack.setOnClickListener {
            requestChangeFragmentToHome()
        }
    }


    private fun requestChangeFragmentToHome() {
        (requireActivity() as MainActivity).replaceFragment(HomeFragment(), "Home")
    }
}