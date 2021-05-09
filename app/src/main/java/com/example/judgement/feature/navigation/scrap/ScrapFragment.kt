package com.example.judgement.feature.navigation.scrap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.judgement.R

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
    }

    companion object {
        private const val TAG = "ScrapFragment"
    }
}