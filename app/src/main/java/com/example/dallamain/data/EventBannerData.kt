package com.example.dallamain.data

import com.google.gson.annotations.SerializedName

data class EventBannerData(
    @SerializedName("bannerUrl") val event_bannerUrl : String
)
data class EventBannerDataList(
    @SerializedName("BannerList") val event_bannerList : ArrayList<EventBannerData>
)