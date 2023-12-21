package com.mio.mio_ktx.ui.mqtt

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mio.base.Tag.TAG
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage

object MqttHelper {
    @SuppressLint("StaticFieldLeak")
    private lateinit var mqttClient: MqttAndroidClient

    var isConnected = false

    fun init(
        context: Context,
        clientId: String = "kotlin_client",
        onConnected: () -> Unit = {},
        onDisconnected: () -> Unit = {},
        onMsgArrived: (topic: String, msg: MqttMessage) -> Unit = { _, _ -> },
        onConnectionLost: (cause: Throwable) -> Unit = {},
        onDeliveryComplete: (token: IMqttDeliveryToken) -> Unit = {},
    ) {
        val serverURI = "tcp://121.43.48.225:1883"
//        val serverURI = "tcp://broker.emqx.io:1883"
        mqttClient = MqttAndroidClient(context, serverURI, clientId)

        mqttClient.setCallback(object : MqttCallback {
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d(TAG, "Receive message: ${message.toString()} from topic: $topic")
                message?.let { onMsgArrived(topic.toString(), it) }
            }

            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG, "Connection lost ${cause.toString()}")
                onConnectionLost(cause!!)
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.d(TAG, "deliveryComplete: $token")
                onDeliveryComplete(token!!)
            }
        })

        val options = MqttConnectOptions()
        options.apply {
            userName = "mioandroid"
            password = "123456".toCharArray()
        }
        try {
            mqttClient.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    onConnected()
                    isConnected = true
//                    Log.d(TAG, "Connection success")
//                    subscribe("android")
//
//                    publish("hello pc,im android", "pc")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Connection failure, cause: $exception")
                    onDisconnected()
                    isConnected = false
                }
            })
        } catch (e: MqttException) {
            Log.e(TAG, "connect: $e")
        }

    }

    /**
     * 订阅
     */

    fun subscribe(
        topic: String,
        qos: Int = 1,
        onSuccess: () -> Unit = {},
        onFailure: (e: Throwable) -> Unit = {},
    ) {
        try {
            mqttClient.subscribe(topic, qos, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Subscribed to $topic")
                    onSuccess()
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to subscribe $topic")
                    onFailure(exception!!)
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publish(
        topic: String,
        msg: String,
        qos: Int = 1,
        retained: Boolean = true,
        onSuccess: () -> Unit = {},
        onFailure: (e: Throwable) -> Unit = {},
    ) {
        try {
            mqttClient.publish(
                topic,
                msg.toByteArray(),
                qos,
                retained,
                null,
                object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d(TAG, "Message published")
                        onSuccess()
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d(TAG, "Failed to publish message")
                        onFailure(exception!!)
                    }
                })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    /**
     * 解析出mqtt消息中的对应字段
     */
    fun parseMsg(
        jsonString: String,
        key: String = "msg",
    ): String {
        val parser = JsonParser()
        val jsonObject: JsonObject = parser.parse(jsonString).asJsonObject
        val msg: String = jsonObject.get(key).asString
        return msg
    }
}