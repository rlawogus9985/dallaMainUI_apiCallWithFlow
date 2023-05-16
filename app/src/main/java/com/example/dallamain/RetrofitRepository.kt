package com.example.dallamain

import com.example.dallamain.data.TopBnrDataList
import com.example.dallamain.manager.RetrofitManager
import retrofit2.Response

class RetrofitRepository{
    suspend fun getTopBnrDataList() : Response<TopBnrDataList> {
        return RetrofitManager.retrofit.getTopBnrData()
    }
}