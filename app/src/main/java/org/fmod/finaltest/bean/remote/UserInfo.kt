package org.fmod.finaltest.bean.remote

import com.google.gson.annotations.SerializedName

data class UserInfo(
    @SerializedName("name")
    var name: String,
    @SerializedName("figureurl_2")
    var avatar: String
)