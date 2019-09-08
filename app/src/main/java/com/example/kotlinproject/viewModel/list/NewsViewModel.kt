package com.example.kotlinproject.viewModel.list

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.kotlinproject.global.common.AppApplication
import com.example.kotlinproject.model.bean.newsChannel.NewsChanelRespo
import com.example.kotlinproject.model.repository.news.NewsRepository
import com.example.kotlinproject.viewModel.base.BaseViewModel
import com.prodege.shopathome.model.networkCall.ApiResponse
import org.koin.core.KoinComponent
/**
 * Created by Deepak Sharma on 01/07/19.
 */
class NewsViewModel( private val projectRepository: NewsRepository) :
   BaseViewModel(){
    private var channelResponse = MutableLiveData<ApiResponse<NewsChanelRespo>>()

    fun getNewsChannelLiveData(): MutableLiveData<ApiResponse<NewsChanelRespo>> {
        return channelResponse
    }

    fun newsChannelApi(strUrl: String) {
        projectRepository.getNewsChannel(strUrl, channelResponse)
    }

}