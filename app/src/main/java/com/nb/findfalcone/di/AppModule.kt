package com.nb.findfalcone.di

import android.content.Context
import android.util.Log
import com.nb.findfalcone.BuildConfig
import com.nb.findfalcone.FindFalconeApplication
import com.nb.findfalcone.api.ApiService
import com.nb.findfalcone.repository.ApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBaseUrl():String = BuildConfig.BASE_URL

    @Singleton
    @Provides
    fun provideClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder().apply {
            addInterceptor(
                Interceptor { chain: Interceptor.Chain ->
                    val original = chain.request()
                    val header = Headers.Builder().apply {
                        add("Content-Type", "application/json")
                        add("Accept", "application/json")
                    }.build()
                    val request = original.newBuilder().also {
                        it.method(original.method, original.body)
                        Log.d("AppModule", "Request: ${original.url}")
                        it.headers(header)
                    }
                    chain.proceed(request.build())
                }
            )
            build()
        }
        .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(baseUrl: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(
        retrofit: Retrofit
    ): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideRepository(apiService: ApiService): ApiRepository {
        return ApiRepository(apiService)
    }

}