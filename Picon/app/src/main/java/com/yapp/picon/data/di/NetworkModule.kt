package com.yapp.picon.data.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    factory { (baseUrl: String) ->
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(get())
            .client(get())
            .build()
    }

    single {
        GsonConverterFactory.create() as Converter.Factory
    }

    single {
        OkHttpClient.Builder()
            .addNetworkInterceptor(
                HttpLoggingInterceptor().also {
                    it.level = HttpLoggingInterceptor.Level.BODY
                })
            .build()
    }

}