package com.mio.music.helper

import com.mio.music.data.BaseResponse
import com.mio.music.data.LoginBean
import com.mio.music.data.RecommendBean
import com.mio.music.data.Song
import com.mio.music.data.SongListResponse
import com.mio.music.data.UrlResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

object KtorHelper {
    private val client by lazy {
        HttpClient(Android) {
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
        }
    }
    private const val BASE_URL = "http://117.50.190.141:3000"

    /**
     * 发送验证码
     */
    suspend fun sendCaptcha(num: String): BaseResponse<Boolean> {
        return (client.get("$BASE_URL/captcha/sent") {
            contentType(ContentType.Application.Json)
            parameter("phone", num)
        } as HttpResponse).receive<BaseResponse<Boolean>>()
    }


    /**
     * 手机号登录
     */
    suspend fun loginCellPhone(
        phone: String,
        captcha: String = "",
        password: String = "",
        md5_password: String = "",
    ): LoginBean {
        return (client.get("$BASE_URL/login/cellphone") {
            contentType(ContentType.Application.Json)
            parameter("phone", phone)
            parameter("captcha", captcha)
            parameter("password", password)
            parameter("md5_password", md5_password)
        } as HttpResponse).receive<LoginBean>()
    }

    /**
     * 推荐歌单
     */
    suspend fun recommendList(): RecommendBean {
        return (client.get("$BASE_URL/recommend/resource")
                as HttpResponse).receive<RecommendBean>()
    }

    /**
     * 获取歌单
     */
    suspend fun getSongList(
        id: Long,
        limit: Int = -1,
        offset: Int = 0,
    ): SongListResponse {
        return (client.get("$BASE_URL/playlist/track/all") {
            contentType(ContentType.Application.Json)
            parameter("id", id)
            if (limit != -1) parameter("limit", limit)
            parameter("offset", offset)
        } as HttpResponse).receive<SongListResponse>()
    }

    suspend fun getSongUrl(
        id: Long,
    ): UrlResponse {
        return (client.get("$BASE_URL/song/url") {
            contentType(ContentType.Application.Json)
            parameter("id", id)
        } as HttpResponse).receive<UrlResponse>()
    }

//
//    suspend fun register(user: User): BaseResponse<User> {
//        return (client.post("$BASE_URL/register") {
//            contentType(ContentType.Application.Json)
//            body = user
//        } as HttpResponse).receive<BaseResponse<User>>()
//    }
//
//    suspend fun getVipLeftDay(id: Int): BaseResponse<Long> {
//        return (client.get("$BASE_URL/getVipLeftDay?id=$id")
//                as HttpResponse).receive<BaseResponse<Long>>()
//    }
//
//    suspend fun addUser(user: User): BaseResponse<Int> {
//        return (client.post("$BASE_URL/addUser") {
//            contentType(ContentType.Application.Json)
//            body = user
//        } as HttpResponse).receive<BaseResponse<Int>>()
//    }
//
//    suspend fun deleteUser(id: Int): BaseResponse<Boolean> {
//        return (client.delete("$BASE_URL/deleteUser?id=$id")
//                as HttpResponse).receive<BaseResponse<Boolean>>()
//    }
//
//    suspend fun updateUser(user: User): BaseResponse<String> {
//        return (client.put("$BASE_URL/updateUser") {
//            contentType(ContentType.Application.Json)
//            body = user
//        } as HttpResponse).receive<BaseResponse<String>>()
//    }
//
//    suspend fun queryUser(id: Int): BaseResponse<User> {
//        return (client.get("$BASE_URL/queryUser?id=$id")
//                as HttpResponse).receive<BaseResponse<User>>()
//    }
//
//    suspend fun getAllUser(): BaseResponse<List<User>> {
//        return (client.get("$BASE_URL/getAllUser")
//                as HttpResponse).receive<BaseResponse<List<User>>>()
//    }
//
//    // 上传文件
//    suspend fun upload(
//        filePath: String,
//    ) {
//        val file = java.io.File(filePath)
//
//        client.submitFormWithBinaryData<BaseResponse<String>>(
//            url = "$BASE_URL/upload",
//            formData = formData {
//                appendInput(
//                    key = "file",
//                    headers = Headers.build {
//                        append(
//                            name = "Content-Disposition",
//                            value = "form-data; name=\"file\"; filename=\"${file.name}\""
//                        )
//                    },
//                    size = file.length()
//                ) {
//                    file.inputStream().asInput()
//                }
//            },
//            block = {
//                method = io.ktor.http.HttpMethod.Post
//            }
//        )
//    }

}