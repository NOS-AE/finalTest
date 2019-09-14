package org.fmod.finaltest.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.rengwuxian.materialedittext.validation.RegexpValidator
import com.tencent.tauth.IUiListener
import kotlinx.android.synthetic.main.fragment_login.*
import org.fmod.finaltest.R
import org.fmod.finaltest.base.abstracts.UiListener
import org.fmod.finaltest.base.fragment.BaseFragment
import org.fmod.finaltest.ui.main.MainActivity
import org.fmod.finaltest.ui.register.RegisterFragment
import org.fmod.finaltest.ui.register.RegisterPresenter
import org.fmod.finaltest.util.toplevel.regexEmail
import java.util.concurrent.TimeUnit

class LoginFragment: BaseFragment(), LoginContract.View {

    companion object {
        fun newInstance() = LoginFragment()
    }

    lateinit var presenter: LoginContract.Presenter
    private var loading = false
    lateinit var qqListener: UiListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.start()
    }

    override fun getLayoutId() = R.layout.fragment_login

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        presenter.prepareLogin(mActivity)

        qqListener = object: UiListener() {
            override fun onComplete(p0: Any?) {
                super.onComplete(p0)
                presenter.getQQInfo(p0, mActivity)
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    @SuppressLint("CheckResult")
    override fun setListener() {
        presenter.listenFormatCheck(
            login_id.textChanges(),
            login_password.textChanges()
        )

        login_login.clicks().throttleFirst(
            2L, TimeUnit.SECONDS
        ).filter {
            !loading
        }.subscribe {
            presenter.login()
        }

        login_qq.clicks().throttleFirst(
            1L, TimeUnit.SECONDS
        ).filter {
            !loading
        }.subscribe {
            presenter.loginQQ(mActivity)
        }

        login_register.clicks().throttleFirst(
            1L, TimeUnit.SECONDS
        ).filter {
            !loading
        }.subscribe {
            val toStart = RegisterFragment.newInstance().apply {
                injectPresenter(RegisterPresenter(this))
            }
            startFragment(toStart)
        }
    }

    override fun initView() {
        login_id.addValidator(
            RegexpValidator("邮箱格式错误",
                regexEmail
            )
        )
    }

    /* LoginContract.View Methods */

    override fun injectPresenter(presenter: LoginContract.Presenter) {
        this.presenter = presenter
    }

    override fun setLoginButtonEnable(enable: Boolean) {
        login_login.isEnabled = enable
    }

    override fun validateEmail(): Boolean {
        return login_id.validate()
    }

    override fun getQQListener(): IUiListener {
        return qqListener
    }

    override fun finishLogin() {
        startActivity(MainActivity::class.java)
    }

    override fun showProgress() {
        loading = true
        login_progress_bar.visibility = View.VISIBLE
        login_id.isEnabled = !loading
        login_password.isEnabled = !loading
    }

    override fun hideProgress() {
        login_progress_bar.visibility = View.INVISIBLE
        loading = false
        login_id.isEnabled = !loading
        login_password.isEnabled = !loading
    }
}