package com.example.judgement.api

import com.example.judgement.data.ScrapData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServerAPI {
    // 스크랩 조회
    @GET("/retrieveScrap.php?")
    fun getScrap(
        @Query("u_id") u_id: String,
        @Query("c_id") c_id: String
    ) : Call<List<ScrapData>>

    // 스크랩 추가
    @GET("/registerScrap.php?")
    fun addScrap(
        @Query("u_id") u_id: String,
        @Query("c_id") c_id: String,
        @Query("j_id") j_id: String,
        @Query("j_name") j_name: String,
        @Query("j_serial") j_serial: String
    ) : Call<String>

    // 스크랩 삭제
    @GET("/deleteScrap.php?")
    fun removeScrap(
        @Query("u_id") u_id: String,
        @Query("j_serial") j_serial: String
    ) : Call<String>

    companion object ServiceImpl {
        private const val BASE_URL = "http://ec2-3-35-53-252.ap-northeast-2.compute.amazonaws.com/"

        private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val server = retrofit.create(ServerAPI::class.java)
    }
}