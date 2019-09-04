package org.fmod.finaltest.bean.remote

import com.google.gson.annotations.SerializedName

class UserInfo {
    var state = 0
    @SerializedName("nickname", alternate = ["name"])
    var name: String = ""
    @SerializedName("figureurl_2")
    var avatar: String = ""

    override fun toString(): String {
        return "state:${state}, nickname: $name, avatar: $avatar"
    }
}