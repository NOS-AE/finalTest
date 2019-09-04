package org.fmod.finaltest.manager

import android.app.Activity
import org.fmod.finaltest.ui.login.LoginActivity
import org.fmod.finaltest.ui.main.MainActivity
import org.fmod.finaltest.ui.splash.SplashActivity

class ActivityManager {
    companion object{
        private val activityCollection = ArrayList<Activity>()

        /**
         * 活动被创建时(onCreate)调用
         *
         * 某些Activity需要在startOtherActivity并在otherActivity显示后关闭
         *
         * 具体关闭哪些otherActivity，根据实际需求修改
         *
         * @param activity 被开启的Activity
         */
        fun onActivityCreate(activity: Activity){
            //添加被创建的Activity
            activityCollection.add(activity)
        }

        fun finishOnActivityCreate(activity: Activity){
            //结束其它应该被结束的Activity
            when(activity){
                is MainActivity -> finishOnMainCreate()
                is LoginActivity -> finishOnLoginCreate()
            }
        }
        /**
         * 活动被销毁时(onDestroy)时调用
         */
        fun onActivityDestroy(activity: Activity){
            //将collection持有的activity引用移除
            activityCollection.remove(activity)

        }

        private fun finishOnMainCreate(){
            val iterator = activityCollection.iterator()
            var i: Activity
            while (iterator.hasNext()){
                i = iterator.next()
                when(i){
                    is SplashActivity -> {
                        iterator.remove()
                        i.finish(0,0)
                    }
                    is LoginActivity -> {
                        iterator.remove()
                        i.finish(0,0)
                    }
                }
            }
        }

        private fun finishOnLoginCreate(){
            val iterator = activityCollection.iterator()
            var i: Activity
            while (iterator.hasNext()){
                i = iterator.next()
                when(i){
                    is SplashActivity -> {
                        iterator.remove()
                        i.finish(0,0)
                    }
                    is MainActivity -> {
                        iterator.remove()
                        i.finish(0, 0)
                    }
                }
            }
        }
    }
}