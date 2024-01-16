package com.mio.filemanager.helper

import com.mio.filemanager.bean.BaseResponse
import com.mio.filemanager.bean.RFile
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

object KtorHelper {
    private val client by lazy {
        HttpClient(Android) {
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
        }
    }
    private const val BASE_URL = "http://117.50.190.141:8080"

    /**
     * list
     */
    suspend fun list(path: String): BaseResponse<List<RFile>> {
        return (client.get("$BASE_URL/list") {
            parameter("path", path)
        } as HttpResponse).receive<BaseResponse<List<RFile>>>()
    }
}