package com.mio.weather

import android.app.Application
import com.qweather.sdk.view.HeConfig

class MyApp :Application() {
    companion object {
        // 和风天气 Public Id
        private const val WEATHER_PUBLIC_ID = "HE2311031415011938"
        // 和风天气 Key
        private const val WEATHER_KEY = "9132db0b741740e694862c11c199de0b"
    }

    override fun onCreate() {
        super.onCreate()

        // 初始化和风天气
        HeConfig.init(WEATHER_PUBLIC_ID, WEATHER_KEY)
        //切换至开发版服务
        HeConfig.switchToDevService()
    }
}