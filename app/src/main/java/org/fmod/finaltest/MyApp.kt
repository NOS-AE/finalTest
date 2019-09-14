package org.fmod.finaltest

import android.app.Application
import android.content.Context
import me.yokeyword.fragmentation.Fragmentation
import org.fmod.finaltest.bean.BigKind
import org.fmod.finaltest.bean.Book
import org.fmod.finaltest.bean.remote.UserInfo
import org.fmod.finaltest.helper.local.LocalHelper
import org.fmod.finaltest.helper.remote.RemoteHelper
import org.fmod.finaltest.manager.DataManager
import org.fmod.finaltest.util.toplevel.networkLog
import org.fmod.finaltest.util.toplevel.statusBarHeight
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal

class MyApp : Application(){

    companion object{
        lateinit var appContext: Context

        //与用户无关，由RemoteHelper管理
        lateinit var token: String

        //一个App一个User
        var globalUser = UserInfo("未登录", "")

        //大分类（所有Item将引用此唯一列表中元素）
        lateinit var bigKinds: ArrayList<BigKind>
        //账本
        lateinit var books: ArrayList<Book>
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        statusBarHeight = getStatusBarHeight()

        initBus()
        initDb()
        initNet()

        Fragmentation.builder()
            .stackViewMode(Fragmentation.BUBBLE)
            .debug(BuildConfig.DEBUG)
            .handleException{
                networkLog("Fragmentation: ${it.message}")
            }
            .install()


    }

    private fun initBus() {
        EventBus.builder().addIndex(MyEventBusIndex()).installDefaultEventBus()
    }

    private fun getStatusBarHeight(): Int{
        val id = resources.getIdentifier("status_bar_height","dimen","android")
        return if(id > 0){
            resources.getDimensionPixelSize(id)
        }else{
            0
        }
    }

    private fun initNet() {
        RemoteHelper.init(this)
    }

    private fun initDb() {
        LitePal.initialize(this)
        if(DataManager.firstLaunch){
            DataManager.firstLaunch = false
            LocalHelper.firstApp()
        }
        LocalHelper.init()
    }

}