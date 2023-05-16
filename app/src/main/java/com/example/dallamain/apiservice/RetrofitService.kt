package com.example.dallamain.apiservice

import com.example.dallamain.data.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitService {
    @GET("RqBannerList")
    suspend fun getTopBnrData(): Response<TopBnrDataList>
//    fun getTopBnrData(): Call<TopBnrDataList>

    @GET("RqMyStarList")
    suspend fun getFollowingData(): Response<FollowingDataList>

    @GET("RqEventBannerList")
    suspend fun getEventData(): Response<EventBannerDataList>

    @POST("RqRoomList")
    suspend fun getLiveSectionData(
        @Body request: RoomListRequest
    ): Response<LiveSectionDataList>

}