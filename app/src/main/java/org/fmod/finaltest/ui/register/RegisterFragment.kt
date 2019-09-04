package org.fmod.finaltest.ui.register


import android.annotation.SuppressLint
import android.view.ViewTreeObserver
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.textChanges
import com.rengwuxian.materialedittext.validation.RegexpValidator
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.fragment_register.*
import org.fmod.finaltest.R
import org.fmod.finaltest.base.fragment.BaseFragment
import org.fmod.finaltest.util.toplevel.dp2px
import org.fmod.finaltest.util.toplevel.regexEmail
import org.fmod.finaltest.util.toplevel.statusBarHeight
import org.fmod.finaltest.util.toplevel.toast
import java.util.concurrent.TimeUnit

private const val sendCodeInterval = 1000 * 60L //1min

class RegisterFragment : BaseFragment(), RegisterContract.View {

    companion object {
        @JvmStatic
        fun newInstance() = RegisterFragment()
    }

    lateinit var presenter: RegisterContract.Presenter

    private var sendCodeTime = 0L

    override fun getLayoutId() = R.layout.fragment_register

    @SuppressLint("CheckResult")
    override fun setListener() {

        register_toolbar.setNavigationOnClickListener {
            pop()
        }

        register_send_code.clicks()
            .bindToLifecycle(this)
            .filter {
                //检测邮箱格式
                if(register_email.validate()) {
                    //检测发送时间间隔
                    val interval = System.currentTimeMillis() - sendCodeTime
                    if (interval < sendCodeInterval) {
                        toast("休息一下吧，还有${(sendCodeInterval - interval) / 1000}秒")
                        false
                    } else {
                        true
                    }
                } else {
                    false
                }
            }
            .doOnNext {
                sendCodeTime = System.currentTimeMillis()
            }
            .subscribe {
                presenter.sendCode(register_email.text?.toString())
            }

        register_next_step
            .clicks()
            .bindToLifecycle(this)
            .throttleFirst(5L, TimeUnit.SECONDS)
            .subscribe {
                presenter.register()
            }

        presenter.checkEditText(
            register_code.textChanges(),
            register_password.textChanges()
        )
    }

    override fun initView() {

        register_toolbar.run {
            layoutParams.height += statusBarHeight
            setPadding(0, statusBarHeight, 0, 0)
        }

        register_email.addValidator(
            RegexpValidator("邮箱格式错误",
                regexEmail
            )
        )

        register_send_code.viewTreeObserver.addOnGlobalLayoutListener(object: ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                register_email.setPaddings(
                    0,
                    0,
                    register_send_code.width + dp2px(5f),
                    0
                )
                register_send_code.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        register_send_code.bringToFront()

    }

    override fun onBackPressedSupport(): Boolean {
        fixInputMethodManagerLeak(context)
        return super.onBackPressedSupport()
    }

    /* RegiserContract.View */
    override fun injectPresenter(presenter: RegisterContract.Presenter) {
        this.presenter = presenter
    }

    override fun finishRegister() {
        toast("注册成功")
        pop()
    }

    override fun setRegisterButtonEnable(enable: Boolean) {
        register_next_step.isEnabled = enable
    }

}
