package org.fmod.finaltest.bean.remote

import com.google.gson.annotations.SerializedName

data class QUserInfo(
    @SerializedName("nickname")
    var name: String,
    @SerializedName("figureurl_2")
    var avatar: String
)