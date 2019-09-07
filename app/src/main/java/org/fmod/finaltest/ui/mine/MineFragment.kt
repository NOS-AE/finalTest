package org.fmod.finaltest.ui.mine


import android.annotation.SuppressLint
import android.content.DialogInterface
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.jakewharton.rxbinding3.view.clicks
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.dialog_chang_password.view.*
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.mine1.*
import kotlinx.android.synthetic.main.mine4.*
import okhttp3.*
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.R
import org.fmod.finaltest.base.activity.BaseActivity
import org.fmod.finaltest.base.fragment.BaseFragment
import org.fmod.finaltest.bean.remote.UserInfo
import org.fmod.finaltest.ui.login.LoginActivity
import org.fmod.finaltest.util.toplevel.log
import org.fmod.finaltest.util.toplevel.statusBarHeight
import org.fmod.finaltest.util.toplevel.toast
import java.util.concurrent.TimeUnit


class MineFragment : BaseFragment(), MineContract.View {

    companion object {
        fun newInstance() = MineFragment()
    }

    lateinit var presenter: MineContract.Presenter
    /*
        用于inflateLayout时加载LayoutParams
        实际上没有引入parent
     */
    private lateinit var virtualParent: ViewGroup
    private lateinit var pwChangeDialogContent: View
    private lateinit var oldPw: MaterialEditText
    private lateinit var newPw: MaterialEditText

    private lateinit var changPwdBuilder: AlertDialog.Builder
    private lateinit var tempChangePwDialog: AlertDialog

    private lateinit var changeNameView: EditText
    private lateinit var changeNameBuilder: AlertDialog.Builder
    private var defaultName: String? = null

    override fun getLayoutId() = R.layout.fragment_mine

    //TODO test change pw
    val client = OkHttpClient()

    @SuppressLint("CheckResult")
    override fun setListener() {
        mine1.setOnClickListener {
            mine1_switch.isChecked = !mine1_switch.isChecked
        }

        mine1_switch.setOnCheckedChangeListener { _, isChecked ->

        }

        mine_logout.setOnClickListener {
            (activity as BaseActivity).startActivity(LoginActivity::class.java)
        }



        mine4.clicks()
            .throttleFirst(1L, TimeUnit.SECONDS)
            .doOnNext {
                if(pwChangeDialogContent.parent != null){
                    (pwChangeDialogContent.parent as ViewGroup).removeView(pwChangeDialogContent)
                }
            }
            .subscribe(
                {
                    tempChangePwDialog = changPwdBuilder.create()
                    tempChangePwDialog.setOnShowListener {
                        tempChangePwDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                            presenter.changePassword(oldPw.text?.toString(), newPw.text?.toString())
                        }
                    }
                    /*tempChangePwDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {

                    }*/
                    tempChangePwDialog.show()
                },
                {
                    toast("无法启动对话框")
                    log(it.toString())
                }
            )

        mine_edit_info.clicks()
            .throttleFirst(1L, TimeUnit.SECONDS)
            .doOnNext {
                if(changeNameView.parent != null) {
                    (changeNameView.parent as ViewGroup).removeView(changeNameView)
                }
            }
            .subscribe({
                changeNameBuilder.create().show()
            },
                {
                    toast("无法启动对话框")
                    log(it.toString())
                })
    }

    override fun initView() {

        virtualParent =  FrameLayout(requireContext())
        pwChangeDialogContent = layoutInflater.inflate(R.layout.dialog_chang_password,virtualParent, false).apply {
            newPw = new_password
            oldPw = old_password
        }

        changPwdBuilder = AlertDialog.Builder(requireContext())
            .setTitle("更换密码")
            .setPositiveButton("确定",null)
            .setNegativeButton("取消",null)
            .setView(pwChangeDialogContent)

        changeNameView = EditText(requireContext()).apply {
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams = lp
            hint = "新用户名"
        }
        changeNameBuilder = AlertDialog.Builder(requireContext())
            .setTitle("更换用户名")
            .setPositiveButton("确定") { _, _ ->
                defaultName = changeNameView.text.toString()
                presenter.changeName(changeNameView.text.toString())
            }
            .setNegativeButton("取消",null)
            .setView(changeNameView)

        mine_toolbar.run {
            layoutParams.height += statusBarHeight
            setPadding(0, statusBarHeight, 0, 0)
        }

        mine_header.setPadding(0, statusBarHeight, 0, 0)

        mine_username.text = MyApp.globalUser.name

    }

    override fun afterInitView() {
        presenter.loadInfo()
    }

    /* MineContract.View Methods */

    override fun injectPresenter(presenter: MineContract.Presenter) {
        this.presenter = presenter
    }

    override fun changePwFormatFail() {
        toast("密码格式错误，取消修改")
    }

    override fun finishChangePw(success: Boolean) {
        if (success){
            toast("修改成功")
            tempChangePwDialog.dismiss()
        } else {
            tempChangePwDialog.dismiss()
            toast("旧密码错误")
        }
    }

    override fun showUserInfo(info: UserInfo) {
        mine_username.text = info.name
    }

    //TODO 2001
    override fun changeNameFail() {
        mine_username.text = defaultName
        toast("修改成功")
    }
}
