package com.leecoder.network.di

import android.util.Log
import com.leecoder.network.StockChartNetwork
import com.leecoder.network.StockChartNetworkImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StockInterceptors

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @StockInterceptors
    fun providerHttpLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor {
            Log.d("[LeeCorder]", it)
        }.apply {
            level = if (true) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

    @Provides
    @Singleton
    @Named("stock")
    fun provideStockOkhttpClient(
        @StockInterceptors interceptors: Provider<Set<@JvmSuppressWildcards Interceptor>>,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .apply {
                interceptors.get().forEach { addInterceptor(it) }
                readTimeout(5000L, TimeUnit.MILLISECONDS)
            }
            .build()

    @Provides
    @Named("stock")
    fun provideRetrofitBuilder(
        @Named("stock") okHttpClient: OkHttpClient,
    ): Retrofit.Builder =
        Retrofit.Builder()
            .client(okHttpClient)


    @Provides
    @Singleton
    fun provideStockChartNetwork(
        @Named("stock") retrofit: Retrofit.Builder,
    ): StockChartNetwork = StockChartNetworkImpl(retrofit)
}