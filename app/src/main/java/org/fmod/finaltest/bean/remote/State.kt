package org.fmod.finaltest.bean.remote

data class State(var state: Int) {
    override fun toString(): String {
        return "state: $state"
    }
}