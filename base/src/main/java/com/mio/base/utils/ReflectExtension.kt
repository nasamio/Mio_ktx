package com.mio.base.utils

import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

fun Any.getMembers(): MutableList<String> {
    return this::class.memberProperties.map {
        it.name
    }.toCollection(mutableListOf())
}

fun Any.getProperty(name: String): Any? {
    return this::class.memberProperties.find { it.name == name }?.call(this)
}

fun Any.setProperty(propertyName: String, newValue: Any) {
    this::class.memberProperties
        .firstOrNull { it.name == propertyName }
        ?.let { (it as? KMutableProperty<*>)?.setter?.call(this, newValue) }
}

fun KCallable<*>.getAnnotationValue(annotationClass: KClass<*>, name: String): Any? {
    return this.annotations.find { it.annotationClass == annotationClass }
        ?.getProperty(name)
}

fun Annotation.getAnnotationValue(name: String): Any? {
    return this.getMembers().find { it == "index" }
}