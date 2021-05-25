package com.example.judgement.data

import android.os.Build
import androidx.annotation.RequiresApi
import kotlin.math.min

data class NaverNewsData(
    var lastBuildDate: String = "",
    var total: Int = 0,
    var start: Int = 0,
    var display: Int = 0,
    var items: List<Items>
)

data class Items(
    var title: String = "",
    var originallink: String = "",
    var link: String = "",
    var description: String = "",
    var pubDate: String = ""
)

class GetFilteredNewsItems(val items: List<Items>) {

    @RequiresApi(Build.VERSION_CODES.N)
    fun getSelectedItems(): List<Items> {
        val news = mutableListOf<Items>()
        news.add(items[0])

        // 유사 뉴스 필터링
        for (i in 1 until items.size - 1) {
            if (news.size >= 10) {
                break
            }

            val first = news.last().description
            val second = items[i].description

            if (first.length >= second.length) {
                val s = getSimilarity(first, second)
                if (s >= 0.2) {
                    continue
                } else {
                    news.add(items[i])
                }
            } else {
                val s = getSimilarity(second, first)
                if (s >= 0.2) {
                    continue
                } else {
                    news.add(items[i])
                }
            }
        }

        return news
    }

    private fun getSimilarity(longer: String, shorter: String) = when (longer.length) {
        0 -> 1.0
        else -> {
            val longerLen = longer.length.toDouble()
            (longerLen - editDistance(longer, shorter)) / longerLen
        }
    }

    private fun editDistance(s1: String, s2: String): Int {
        val costs = IntArray(s2.length + 1)

        for (i in 0..s1.length) {
            var lastValue = i
            for (j in 0..s2.length) {
                if (i == 0) {
                    costs[j] = j
                } else {
                    if (j > 0) {
                        var newValue = costs[j - 1]
                        if (s1.elementAt(i - 1) != s2.elementAt(j - 1)) {
                            newValue = min(min(newValue, lastValue), costs[j]) + 1
                        }
                        costs[j - 1] = lastValue
                        lastValue = newValue
                    }
                }
            }
            if (i > 0) costs[s2.length] = lastValue
        }

        return costs[s2.length]
    }
}
