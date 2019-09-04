package org.fmod.finaltest.ui.main

import android.annotation.SuppressLint
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import org.fmod.finaltest.helper.local.LocalHelper

class MainPresenter (
    private val mView: MainContract.View
): MainContract.Presenter {
    override fun start() {

    }

    @SuppressLint("CheckResult")
    override fun loadItems() {
        LocalHelper.loadItems()
            .bindToLifecycle(mView)
            .subscribe {
                mView.showItems(it)
            }
    }
}