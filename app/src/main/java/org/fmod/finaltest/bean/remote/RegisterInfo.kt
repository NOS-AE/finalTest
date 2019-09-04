package org.fmod.finaltest.bean.remote

class RegisterInfo {
    var state = 0
    var uid = ""
    var name = ""

    override fun toString(): String {
        return "state: $state, uid: $uid, email: $name"
    }
}