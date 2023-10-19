package com.mio.base

import java.util.Random

object Utils {

    private val random = Random()

    fun generateRandomChineseName(): String {
        val firstNames = listOf("张", "王", "李", "赵", "刘", "陈", "杨", "黄", "周", "吴")
        val lastNames = listOf("伟", "秀英", "建华", "小红", "国强", "美丽", "军", "芳", "敏", "明")

        val randomFirstName = firstNames[random.nextInt(firstNames.size)]
        val randomLastName = lastNames[random.nextInt(lastNames.size)]

        return randomFirstName + randomLastName
    }

}