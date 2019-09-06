package org.fmod.finaltest.ui.main

import android.util.Log
import io.reactivex.Observable
import org.fmod.finaltest.R
import org.fmod.finaltest.base.activity.BaseActivity
import org.fmod.finaltest.manager.ActivityManager
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription


class MainActivity : BaseActivity() {
    companion object {
        const val TAG = "MyApp"
    }

    override fun beforeSetContentView() {
        ActivityManager.finishOnActivityCreate(this)
    }

    override fun getLayoutId(): Int = R.layout.activity_main


    override fun setListener() {

    }

    override fun initView() {
        transparentStatusBar()
        val fragment = findFragment(MainFragment::class.java)
        if (fragment == null) {
            loadRootFragment(R.id.fragment_container, MainFragment.newInstance().apply {
                injectPresenter(MainPresenter(this))
            })
        }
        attachKeyBoard()
    }

    override fun onDestroy() {
        detachKeyBoard()
        super.onDestroy()
    }

}
