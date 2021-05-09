package com.example.judgement.feature.navigation.category

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.judgement.R

class CategoryFragment : Fragment() {
    private lateinit var categoryView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        categoryView = inflater.inflate(R.layout.fragment_category, container, false)
        return categoryView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    companion object {
        private const val TAG = "CategoryFragment"
    }
}