package com.mio.compose.bean

import com.google.gson.annotations.SerializedName

data class Profile(
    @SerializedName("backgroundUrl")
    val backgroundUrl: String = "",
    @SerializedName("detailDescription")
    val detailDescription: String = "",
    @SerializedName("birthday")
    val birthday: Long = 0,
    @SerializedName("gender")
    val gender: Int = 0,
    @SerializedName("city")
    val city: Int = 0,
    @SerializedName("signature")
    val signature: String = "",
    @SerializedName("followeds")
    val followeds: Int = 0,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("remarkName")
    val remarkName: String? = null,
    @SerializedName("eventCount")
    val eventCount: Int = 0,
    @SerializedName("playlistBeSubscribedCount")
    val playlistBeSubscribedCount: Int = 0,
    @SerializedName("accountStatus")
    val accountStatus: Int = 0,
    @SerializedName("avatarImgId")
    val avatarImgId: Long = 0,
    @SerializedName("defaultAvatar")
    val defaultAvatar: Boolean = false,
    @SerializedName("avatarImgIdStr")
    val avatarImgIdStr: String = "",
    @SerializedName("backgroundImgIdStr")
    val backgroundImgIdStr: String = "",
    @SerializedName("province")
    val province: Int = 0,
    @SerializedName("nickname")
    val nickname: String = "",
    @SerializedName("expertTags")
    val expertTags: String? = null,
    @SerializedName("djStatus")
    val djStatus: Int = 0,
    @SerializedName("avatarUrl")
    val avatarUrl: String = "",
    @SerializedName("authStatus")
    val authStatus: Int = 0,
    @SerializedName("follows")
    val follows: Int = 0,
    @SerializedName("vipType")
    val vipType: Int = 0,
    @SerializedName("followed")
    val followed: Boolean = false,
    @SerializedName("userId")
    val userId: Int = 0,
    @SerializedName("mutual")
    val mutual: Boolean = false,
    @SerializedName("authority")
    val authority: Int = 0,
    @SerializedName("backgroundImgId")
    val backgroundImgId: Long = 0,
    @SerializedName("userType")
    val userType: Int = 0,
    @SerializedName("experts")
    val experts: Experts,
    @SerializedName("avatarDetail")
    val avatarDetail: String? = null,
    @SerializedName("playlistCount")
    val playlistCount: Int = 0,
)