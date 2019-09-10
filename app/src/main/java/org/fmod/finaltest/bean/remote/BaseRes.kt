package org.fmod.finaltest.bean.remote

data class BaseRes<T>(
    var state: Int,
    var result: T
)