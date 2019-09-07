package org.fmod.finaltest.ui.mine

import android.annotation.SuppressLint
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.bean.remote.Code
import org.fmod.finaltest.base.abstracts.RemoteObserver
import org.fmod.finaltest.helper.pref.PreferenceHelper
import org.fmod.finaltest.helper.remote.RemoteHelper
import org.fmod.finaltest.util.toplevel.log
import org.fmod.finaltest.util.toplevel.toast


class MinePresenter(
    private val mView: MineContract.View
): MineContract.Presenter {

    override fun start() {

    }

    override fun changePassword(old: String?, new: String?) {
        log("change pw old:$old, new:$new")
        if(old.isNullOrBlank() || new.isNullOrBlank()) {
            mView.changePwFormatFail()
            log("format wrong")
            return
        }
        RemoteHelper.changePassword(MyApp.token, old, new)
            .bindToLifecycle(mView)
            .doOnNext {
                log("${it.state}")
                if (it.state == 403) {
                    log("wrong original pw")
                    mView.finishChangePw(false)
                }else{
                    log("else code ${it.state}")
                }

            }
            .filter {
                it.state == 200 || it.state == 409
            }
            .subscribe(object : RemoteObserver<Code>() {
                override fun onNext(t: Code) {
                    PreferenceHelper.saveMailPw(new)
                    log("change pw finish")
                    mView.finishChangePw(true)
                }

                override fun onError(e: Throwable) {
                    super.onError(e)
                    toast("更改失败，请检查网络")
                }
            })
    }

    @SuppressLint("CheckResult")
    override fun loadInfo() {

        if(MyApp.globalUser.state == 200) return

        RemoteHelper.getUserInfo(MyApp.token)
            .bindToLifecycle(mView)
            .subscribe {
                MyApp.globalUser = it
                mView.showUserInfo(it)
            }
    }

    @SuppressLint("CheckResult")
    override fun changeName(name: String?) {

        if(name.isNullOrBlank()) {
            toast("新名字不能为空")
            return
        }

        RemoteHelper.changeName(MyApp.token, name)
            .bindToLifecycle(mView)
            .doOnNext {
                if(it.state != 200) {
                    log("change name fail: ${it.state}")
                    mView.changeNameFail()
                }
            }
            .filter {
                it.state == 200
            }
            .subscribe {
                toast("修改成功")
                MyApp.globalUser.name = it.name
                mView.showUserInfo(MyApp.globalUser)
            }
    }

}