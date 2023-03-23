package com.example.notes.retrofit

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class MyRetrofitBuilder @Inject constructor()  {
    companion object {
        private const val BASEURL = "https://jsonplaceholder.typicode.com/"
        private var retrofit: Retrofit
        init {
            Log.d("TAG", "MyRetrofitBuilder: Initialize")
            retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASEURL)
                .build()
        }
    }
    fun getRetrofitApi() : RetrofitApi{
        return retrofit.create(RetrofitApi::class.java)
    }

}