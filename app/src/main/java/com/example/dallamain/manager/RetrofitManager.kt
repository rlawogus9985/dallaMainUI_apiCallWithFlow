package com.example.dallamain.manager

import com.example.dallamain.apiservice.RetrofitService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitManager {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://61.80.148.23:3000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
//    create추가. retrofit의 확장함수로 정의되어있다.
        .create(RetrofitService::class.java)
}