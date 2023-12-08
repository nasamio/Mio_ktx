package com.mio.compose.net

import com.mio.compose.bean.LoginBean
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

object KtorHelper {
    private val client by lazy {
        HttpClient(Android) {
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
        }
    }
    private const val BASE_URL = "http://192.168.1.159:3000"

    /**
     * get 请求访问：/captcha/sent
     * 参数：phone
     */
    suspend fun sendYzm(phoneNumber: String): LoginBean {
        return (client.get("$BASE_URL/captcha/sent?phone=$phoneNumber")
                as HttpResponse).receive<LoginBean>()
    }

    /**
     * get: /login/cellphone
     */
    private suspend fun loginWithPhone(
        phone: String,
        password: String = "",
        captcha: String = "",
    ): LoginBean {
        return (client.get(
            "$BASE_URL/login/cellphone?" +
                    "phone=$phone" +
                    "password=$password" +
                    "captcha=$captcha"
        ) as HttpResponse).receive<LoginBean>()
    }

    /**
     * 验证码登录
     */
    suspend fun loginWithYzm(
        phoneNumber: String,
        yzm: String,
    ): LoginBean {
        return loginWithPhone(
            phone = phoneNumber,
            captcha = yzm,
        )
    }

    /**
     * 密码登录
     */
    suspend fun loginWithPwd(
        phoneNumber: String,
        password: String,
    ): LoginBean {
        return loginWithPhone(
            phone = phoneNumber,
            password = password,
        )
    }
}