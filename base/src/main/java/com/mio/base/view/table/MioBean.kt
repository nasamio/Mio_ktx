package com.mio.base.view.table

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class MioBean(
    val title: String = "",
)
