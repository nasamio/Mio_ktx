package com.mio.account.net

import android.util.Log
import com.mio.account.bean.BaseResponse
import com.mio.account.bean.User
import com.mio.base.Tag.TAG
import com.mio.base.extension.toJson
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.request
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.contentType
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.streams.asInput

object KtorHelper {
    private val client by lazy {
        HttpClient(Android) {
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
        }
    }
    private const val BASE_URL = "http://192.168.1.200:8080"

    suspend fun login(user: User): BaseResponse<User> {
        return (client.post("$BASE_URL/login") {
            contentType(ContentType.Application.Json)
            body = user
            Log.d(TAG, "login: user:${user.toJson()}")
        } as HttpResponse).receive<BaseResponse<User>>()
    }

    suspend fun register(user: User): BaseResponse<User> {
        return (client.post("$BASE_URL/register") {
            contentType(ContentType.Application.Json)
            body = user
        } as HttpResponse).receive<BaseResponse<User>>()
    }

    suspend fun getVipLeftDay(id: Int): BaseResponse<Long> {
        return (client.get("$BASE_URL/getVipLeftDay?id=$id")
                as HttpResponse).receive<BaseResponse<Long>>()
    }

    suspend fun addUser(user: User): BaseResponse<Int> {
        return (client.post("$BASE_URL/addUser") {
            contentType(ContentType.Application.Json)
            body = user
        } as HttpResponse).receive<BaseResponse<Int>>()
    }

    suspend fun deleteUser(id: Int): BaseResponse<Boolean> {
        return (client.delete("$BASE_URL/deleteUser?id=$id")
                as HttpResponse).receive<BaseResponse<Boolean>>()
    }

    suspend fun updateUser(user: User): BaseResponse<String> {
        return (client.put("$BASE_URL/updateUser") {
            contentType(ContentType.Application.Json)
            body = user
        } as HttpResponse).receive<BaseResponse<String>>()
    }

    suspend fun queryUser(id: Int): BaseResponse<User> {
        return (client.get("$BASE_URL/queryUser?id=$id")
                as HttpResponse).receive<BaseResponse<User>>()
    }

    suspend fun getAllUser(): BaseResponse<List<User>> {
        return (client.get("$BASE_URL/getAllUser")
                as HttpResponse).receive<BaseResponse<List<User>>>()
    }

    // 上传文件
    suspend fun upload(
        filePath: String,
    ) {
        val file = java.io.File(filePath)

        client.submitFormWithBinaryData<BaseResponse<String>>(
            url = "$BASE_URL/upload",
            formData = formData {
                appendInput(
                    key = "file",
                    headers = Headers.build {
                        append(
                            name = "Content-Disposition",
                            value = "form-data; name=\"file\"; filename=\"${file.name}\""
                        )
                    },
                    size = file.length()
                ) {
                    file.inputStream().asInput()
                }
            },
            block = {
                method = io.ktor.http.HttpMethod.Post
            }
        )
    }

}