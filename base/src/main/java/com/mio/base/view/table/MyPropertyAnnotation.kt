package com.mio.base.view.table

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class MyPropertyAnnotation(val info: String)
