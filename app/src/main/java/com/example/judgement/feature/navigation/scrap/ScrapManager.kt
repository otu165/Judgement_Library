package com.example.judgement.feature.navigation.scrap


data class ScrapManager(
    val scrapRvAdapters: MutableMap<String, ScrapRvAdapter> = mutableMapOf(), // 어댑터
    val scrapStatus: MutableMap<String, Boolean> = mutableMapOf(), // 삭제 상태
    val scrapList: MutableMap<String, List<String>> = mutableMapOf() // 스크랩 리스트
) {
}