package com.mio.account.net

import com.mio.account.bean.BaseResponse
import com.mio.account.bean.User
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

/**
 * 依赖:
 *     // retrofit
 *     implementation "com.squareup.retrofit2:retrofit:2.9.0"
 *     //retrofit依赖Gson
 *     implementation "com.squareup.retrofit2:converter-gson:2.9.0"
 *     //OkHttp
 *     implementation "com.squareup.okhttp3:okhttp:3.2.0"
 *     //retrofit依赖RxJava
 *     implementation "com.squareup.retrofit2:adapter-rxjava:2.9.0"
 *
 *     // 需要修改对应的版本和jvm版本至少为17：
 *      compileOptions {
 *         sourceCompatibility JavaVersion.VERSION_17
 *         targetCompatibility JavaVersion.VERSION_17
 *     }
 *     kotlinOptions {
 *         jvmTarget = '17'
 *     }
 */
object NetHelper {
    private const val LOCAL_URL = "192.168.1.200:8080"
    private const val hw_url = "60.204.244.109:8082"


    private const val BASE_URL = hw_url

    private const val DEFAULT_CONNECT_TIME = 30;
    private const val DEFAULT_WRITE_TIME = 30;
    private const val DEFAULT_READ_TIME = 30;

    lateinit var apiService: ApiService

    // 需要动态配置请求地址
    fun initApiService(url: String = BASE_URL): ApiService {
        val client = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_CONNECT_TIME.toLong(), TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_WRITE_TIME.toLong(), TimeUnit.SECONDS)
            .readTimeout(DEFAULT_READ_TIME.toLong(), TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("http://$url/")
            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
        return apiService
    }


    interface ApiService {
        @POST("user/login")
        suspend fun login(@Body user: User): BaseResponse<User>

        @POST("user/register")
        suspend fun register(@Body user: User): BaseResponse<Int>
    }

}