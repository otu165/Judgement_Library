package com.example.judgement.feature.navigation.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.feature.navigation.MainActivity
import com.example.judgement.feature.navigation.home.HomeFragment
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : Fragment() {
    private lateinit var categoryView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("TAG", "CategoryFragment created");
        categoryView = inflater.inflate(R.layout.fragment_category, container, false)
        return categoryView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setRecyclerView()

        img_category_back.setOnClickListener {
            requestChangeFragment()
        }
    }

    private fun setRecyclerView() {
        val rv = view?.findViewById(R.id.rv_category) as RecyclerView
        val rvAdapter = CategoryRvAdapter(
            requireContext(),
            resources.getStringArray(R.array.bottom_navigation_category)
        )

        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager(requireContext()).orientation))

        rvAdapter.notifyDataSetChanged()
    }

    private fun requestChangeFragment() {
        (requireActivity() as MainActivity).replaceFragment(HomeFragment(), "Home")
    }

    companion object {
        private const val TAG = "CategoryFragment"
    }
}