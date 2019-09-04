package org.fmod.finaltest.base.abstracts

import org.fmod.finaltest.util.toplevel.networkLog
import retrofit2.Call
import retrofit2.Callback

/**
 * @param tag 关于此CallBack调用者的更多信息
 */
abstract class RemoteCallback<T>(private val tag: String): Callback<T> {
    override fun onFailure(call: Call<T>, t: Throwable) {
        networkLog("$tag: ${t.message}")
    }

    //override fun onResponse(call: Call<T>, response: Response<T>)
}