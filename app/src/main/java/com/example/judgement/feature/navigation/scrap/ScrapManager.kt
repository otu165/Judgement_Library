package com.example.judgement.feature.navigation.scrap

data class ScrapManager(
    val scrapRvAdapters: MutableMap<String, ScrapRvAdapter> = mutableMapOf(), // 어댑터
)