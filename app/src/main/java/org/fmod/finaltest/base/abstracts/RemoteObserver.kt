package org.fmod.finaltest.base.abstracts

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.fmod.finaltest.util.toplevel.localLog
import org.fmod.finaltest.util.toplevel.log
import org.fmod.finaltest.util.toplevel.networkLog
import org.fmod.finaltest.util.toplevel.toast

abstract class RemoteObserver<T>: Observer<T> {

    override fun onSubscribe(d: Disposable) {

    }

    override fun onError(e: Throwable) {
        networkLog("RemoteObserver: $e")
        /*when(getPrintType()) {
            APP -> {
                toast("操作失败")
                log("RemoteObserver: $e")
            }
            NET -> {

            }
            DB -> {
                toast("数据存取失败")
                localLog("RemoteObserver: $e")
            }
        }*/
    }

    override fun onComplete() {

    }

}