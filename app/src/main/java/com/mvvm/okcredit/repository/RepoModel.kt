package com.mvvm.okcredit.repository

import android.content.Context
import com.mvvm.okcredit.repository.ApiService.Companion.createRetrofit
import com.mvvm.okcredit.util.SharedPreference
import org.koin.core.KoinComponent

class RepoModel(context: Context) : KoinComponent {

    val appPreference = SharedPreference(context)
    var api = createRetrofit(appPreference).create(ApiService::class.java)
}