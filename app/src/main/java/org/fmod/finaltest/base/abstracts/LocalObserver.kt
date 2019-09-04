package org.fmod.finaltest.base.abstracts

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.fmod.finaltest.util.toplevel.localLog

abstract class LocalObserver<T>: Observer<T> {
    override fun onSubscribe(d: Disposable) {

    }

    override fun onError(e: Throwable) {
        localLog("RemoteObserver: $e")
    }

    override fun onComplete() {

    }
}