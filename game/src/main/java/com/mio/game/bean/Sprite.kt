package com.mio.game.bean

abstract class Sprite {
    var name: String = ""
    var x: Int = 0
    var y: Int = 0
    var speed: Float = 1f // 单位每100ms移动多少像素点
}
