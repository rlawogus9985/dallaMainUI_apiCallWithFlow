package com.example.dallamain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dallamain.apiservice.RetrofitService
import com.example.dallamain.data.EventBannerData
import com.example.dallamain.data.EventBannerDataList
import com.example.dallamain.data.FollowingData
import com.example.dallamain.data.FollowingDataList
import com.example.dallamain.data.LiveSectionData
import com.example.dallamain.data.LiveSectionDataList
import com.example.dallamain.data.RoomListRequest
import com.example.dallamain.data.TopBnrData
import com.example.dallamain.data.TopBnrDataList
import com.example.dallamain.manager.RetrofitManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(): ViewModel() {

    private val service: RetrofitService = RetrofitManager.retrofit

    init {
        getTopBnrLists()
        getFollowingList()
        getEventList()
        getLiveDataList()
    }

    // top bnr
    private var _topBnrLists = MutableSharedFlow<ArrayList<TopBnrData>>()
    val topBnrLists: SharedFlow<ArrayList<TopBnrData>> = _topBnrLists

    // following list
    private var _followingList = MutableSharedFlow<ArrayList<FollowingData>>()
    val followingList: SharedFlow<ArrayList<FollowingData>> = _followingList

    // eventLists
    private var _eventLists = MutableSharedFlow<ArrayList<EventBannerData>>()
    val eventLists: SharedFlow<ArrayList<EventBannerData>> = _eventLists

    // live section
    private var _liveSectionLists = MutableSharedFlow<ArrayList<LiveSectionData>>()
    val liveSectionLists: SharedFlow<ArrayList<LiveSectionData>> = _liveSectionLists

    // bj textview boolean
    private var _bjText = MutableLiveData<Boolean>(true)
    val bjText: LiveData<Boolean> = _bjText

    // fan textview boolean
    private var _fanText = MutableLiveData<Boolean>(false)
    val fanText: LiveData<Boolean> = _fanText

    // team textview boolean
    private var _teamText = MutableLiveData<Boolean>(false)
    val teamText: LiveData<Boolean> = _teamText

    fun getTopBnrLists(){
        viewModelScope.launch {
            val response = service.getTopBnrData()
            if (response.isSuccessful){
                response.body()?.let{
//                    _topBnrLists.value = it.TopBannerList
                    _topBnrLists.emit(it.TopBannerList)
                    Log.d("태그", "getTopBnrLists: $it")
                }
            }
        }
    }

    /*
    fun getTopBnrLists(){
        service.getTopBnrData().enqueue(object: Callback<TopBnrDataList> {
            override fun onResponse( call: Call<TopBnrDataList>, response: Response<TopBnrDataList>) {
                if(response.isSuccessful){
                    response.body()?.let{
                        _topBnrLists.value = it.TopBannerList
                    }
                }
            }
            override fun onFailure(call: Call<TopBnrDataList>, t: Throwable) {
                Log.d("viewbannerlist","${t.toString()}")
            }
        })
    }

     */

    fun getFollowingList(){
        viewModelScope.launch {
            val response = service.getFollowingData()
            if(response.isSuccessful){
                response.body()?.let{
                    _followingList.emit(it.StarLists)
                }
            }
        }
    }

    fun getEventList(){
        viewModelScope.launch {
            val response = service.getEventData()
            if(response.isSuccessful){
                response.body()?.let{
                    _eventLists.emit(it.event_bannerList)
                }
            }
        }
    }

    fun getLiveDataList(){
        viewModelScope.launch {
            val response = service.getLiveSectionData(RoomListRequest(PageNo = 1))
            if(response.isSuccessful){
                response.body()?.let{
                    _liveSectionLists.emit(it.RoomLists)
                }
            }
        }
    }

    fun onTextBJClicked(){
        if(_bjText.value == true) return
        _bjText.value = true
        _fanText.value = false
        _teamText.value = false
    }
    fun onTextFanClicked(){
        if(_fanText.value == true) return
        _bjText.value = false
        _fanText.value = true
        _teamText.value = false
    }
    fun onTextTeamClicked(){
        if(_teamText.value == true) return
        _bjText.value = false
        _fanText.value = false
        _teamText.value = true
    }


}
