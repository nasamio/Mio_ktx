package com.mio.base.view.table

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class MioColumn(
    val index: Int,
    val title: String,
    val type: CellType = CellType.NormalText,
    val width: Int = 120,
    val propertyName: String = "", // 存储对应的属性名 获取参数的时候使用
)