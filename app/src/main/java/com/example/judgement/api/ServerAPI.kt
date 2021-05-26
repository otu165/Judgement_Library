package com.example.judgement.api

import com.example.judgement.data.ScrapRvData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ServerAPI {

    @GET("/users/scrap/{category}")
    fun getScrap(@Path("category") category: String): Call<List<ScrapRvData>>

    companion object ServiceImpl {
        private const val BASE_URL = "http://ec2-3-35-53-252.ap-northeast-2.compute.amazonaws.com/"

        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val server = retrofit.create(ServerAPI::class.java)
    }
}