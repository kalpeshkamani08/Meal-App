package com.mvvm.okcredit.repository

import com.google.gson.GsonBuilder
import com.mvvm.okcredit.model.CategoryListApiRes
import com.mvvm.okcredit.model.ProductListApiRes
import com.mvvm.okcredit.model.SubCategoryListApiRes
import com.mvvm.okcredit.ui.home.model.FoodClassResponse
import com.mvvm.okcredit.ui.mealdetail.model.MealDetailsModelResponse
import com.mvvm.okcredit.util.Constants
import com.mvvm.okcredit.util.SharedPreference
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

public interface ApiService {

    //Todo ("Products List" )
    @GET("json/v1/1/filter.php?c=dessert")
    fun callFoodListAPI(
    ): Observable<FoodClassResponse>

    //Todo("Products Details List")
    @GET("json/v1/1/lookup.php?i=53062")
    fun callMealsDetails(
        @Query("i") i: Int
    ): Observable<MealDetailsModelResponse>




    @GET("Category/list")
    fun callCategoryListAPI(
        @Query("companyId") companyId: Int
    ): Observable<CategoryListApiRes>

    @GET("SubCategory/list")
    fun callSubCategoryListAPI(
        @Query("companyId") companyId: Int,
        @Query("categoryId") categoryId: Int
    ): Observable<SubCategoryListApiRes>

    @GET("Product/list")
    fun callProductListAPI(
        @Query("companyId") companyId: Int,
        @Query("categoryId") categoryId: Int,
        @Query("subCategoryId") subCategoryId: Int
    ): Observable<ProductListApiRes>


    //  https://www.themealdb.com/api/json/v1/1/filter.php?c=dessert

    companion object {
        lateinit var retrofit: Retrofit
        private val timeout = 30

        fun createRetrofit(appPreference: SharedPreference): Retrofit {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(createOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            return retrofit
        }

        private fun createOkHttpClient(): OkHttpClient {
            val builder = OkHttpClient.Builder()
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
            builder.readTimeout(timeout.toLong(), TimeUnit.SECONDS)
            builder.writeTimeout(timeout.toLong(), TimeUnit.SECONDS)
            builder.connectTimeout(timeout.toLong(), TimeUnit.SECONDS)

            builder.addInterceptor { chain ->
                val originalRequest = chain.request()
                val originalUrl = originalRequest.url
                val url = originalUrl.newBuilder()
                    .build()
                val requestBuilder = originalRequest.newBuilder()
                    .url(url)
                chain.proceed(requestBuilder.build())
            }
            return builder.build()
        }
    }

}

