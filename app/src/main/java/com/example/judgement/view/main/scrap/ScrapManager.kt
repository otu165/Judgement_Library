package com.example.judgement.view.main.scrap

import com.example.judgement.adapter.ScrapRvAdapter

data class ScrapManager(
    val scrapRvAdapters: MutableMap<String, ScrapRvAdapter> = mutableMapOf(), // 어댑터
)