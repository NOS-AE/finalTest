package org.fmod.finaltest.ui.login

import org.fmod.finaltest.manager.ActivityManager
import android.content.Intent
import android.os.Bundle
import com.tencent.connect.common.Constants
import com.tencent.tauth.Tencent
import org.fmod.finaltest.R
import org.fmod.finaltest.base.activity.BaseActivity
import org.fmod.finaltest.helper.pref.PreferenceHelper

class LoginActivity : BaseActivity() {
    /*override fun beforeSetContentView() {
        window.run {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            enterTransition = Fade().apply {
                duration = 300L
                excludeTarget(android.R.id.navigationBarBackground, true)
            }
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceHelper.setLoginWay(PreferenceHelper.wayNoLogin)
    }

    private lateinit var loginFragment: LoginFragment

    override fun getLayoutId() = R.layout.activity_login

    override fun setListener() {

    }

    override fun initView() {

        ActivityManager.finishOnActivityCreate(this)

        transparentStatusBar()

        loginFragment = LoginFragment.newInstance().apply {
            injectPresenter(LoginPresenter(this))
        }
        loadRootFragment(R.id.login_fragment_container, loginFragment)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == Constants.REQUEST_LOGIN){
            Tencent.onActivityResultData(requestCode, resultCode, data, loginFragment.qqListener)
        }
        super.onActivityResult(requestCode, resultCode, data)

    }

}
