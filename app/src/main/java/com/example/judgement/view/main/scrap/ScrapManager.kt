package com.example.judgement.view.main.scrap

import com.example.judgement.adapter.ScrapAdapter

data class ScrapManager(
    val scrapRvAdapters: MutableMap<String, ScrapAdapter> = mutableMapOf(), // 어댑터
)