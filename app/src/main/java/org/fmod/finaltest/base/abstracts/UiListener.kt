package org.fmod.finaltest.base.abstracts

import com.tencent.tauth.IUiListener
import com.tencent.tauth.UiError
import org.fmod.finaltest.util.toplevel.log
import org.fmod.finaltest.util.toplevel.toast

abstract class UiListener: IUiListener {
    override fun onCancel() {
        log("QQLogin cancel")
    }

    override fun onComplete(p0: Any?) {
        log("QQLogin complete")
    }

    override fun onError(p0: UiError?) {
        log("QQLogin error")
    }
}