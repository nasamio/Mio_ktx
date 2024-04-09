import android.os.Handler
import okhttp3.*
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyWebSocketClient {
    private val client = OkHttpClient()

    fun connectToWebSocket(url: String, uid: Int) {
        val request = Request.Builder()
            .url("$url?uid=$uid")
            .build()

        val webSocketListener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                // WebSocket 连接建立
                Log.d(TAG, "onOpen: WebSocket connection successful")

                startSend(webSocket)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                // 收到服务端发送来的 String 类型消息
                Log.d(TAG, "onMessage: Received message: $text")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // 收到服务端发送来的 ByteString 类型消息
                Log.d(TAG, "onMessage: Received message: $bytes")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                // 收到服务端发来的 CLOSE 帧消息，准备关闭连接
                Log.d(TAG, "onClosing: WebSocket closing")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                // WebSocket 连接关闭
                Log.d(TAG, "onClosed: WebSocket closed")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // WebSocket 连接失败
                Log.d(
                    TAG,
                    "onFailure: WebSocket connection failed: ${response?.message},reason:${t.toString()}"
                )
            }
        }

        client.newWebSocket(request, webSocketListener)
    }

    var count = 0

    private fun startSend(webSocket: WebSocket) {
        webSocket.send("这是第${count++}条信息")

        GlobalScope.launch {
            delay(1000)
            startSend(webSocket)
        }
    }

    companion object {
        private const val TAG = "MyWebSocketClient"
    }
}
