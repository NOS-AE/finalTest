package org.fmod.finaltest.base.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import com.trello.rxlifecycle3.components.support.RxFragment
import leakcanary.AppWatcher
import me.yokeyword.fragmentation.ExtraTransaction
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportFragmentDelegate
import me.yokeyword.fragmentation.anim.FragmentAnimator
import org.fmod.finaltest.base.activity.BaseActivity
import org.fmod.finaltest.util.toplevel.log
import java.lang.reflect.Field

abstract class BaseFragment: RxFragment(), ISupportFragment, IBaseUniversal {
    private val mDelegate = SupportFragmentDelegate(this)
    protected lateinit var mActivity: FragmentActivity

    override fun getSupportDelegate(): SupportFragmentDelegate {
        return mDelegate
    }

    override fun extraTransaction(): ExtraTransaction {
        return mDelegate.extraTransaction()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDelegate.onAttach(activity)
        mActivity = mDelegate.activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setListener()
        afterInitView()
    }

    override fun afterInitView() {

    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return mDelegate.onCreateAnimation(transit, enter, nextAnim)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mDelegate.onActivityCreated(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mDelegate.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        mDelegate.onResume()
    }

    override fun onPause() {
        super.onPause()
        mDelegate.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mDelegate.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDelegate.onDestroy()
        //检测Fragment销毁后是否还有引用导致Leak
        AppWatcher.objectWatcher.watch(this)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mDelegate.onHiddenChanged(hidden)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mDelegate.setUserVisibleHint(isVisibleToUser)
    }

    /**
     * enqueueAction is deprecated, use post instead
     * */
    override fun enqueueAction(runnable: Runnable?) {
        mDelegate.post(runnable)
    }

    /**
     * 前面的事务全部执行后 执行该Action
     */
    override fun post(runnable: Runnable?) {
        mDelegate.post(runnable)
    }

    override fun onEnterAnimationEnd(savedInstanceState: Bundle?) {
        mDelegate.onEnterAnimationEnd(savedInstanceState)
    }

    /**
     * Lazy initial，Called when fragment is first called.
     */
    override fun onLazyInitView(savedInstanceState: Bundle?) {
        mDelegate.onLazyInitView(savedInstanceState)
    }

    /**
     * Fragment对用户可见时回调
     * */
    override fun onSupportVisible() {
        mDelegate.onSupportVisible()
    }

    /**
     * Fragment对用户不可见时回调
     * */
    override fun onSupportInvisible() {
        mDelegate.onSupportInvisible()
    }

    /**
     * Fragment是否对用户可见
     * */
    override fun isSupportVisible(): Boolean {
        return mDelegate.isSupportVisible
    }

    /**
     * 设定当前Fragment动画,优先级比在SupportActivity里高
     * */
    override fun onCreateFragmentAnimator(): FragmentAnimator = mDelegate.onCreateFragmentAnimator()

    override fun setFragmentAnimator(fragmentAnimator: FragmentAnimator?) {
        mDelegate.fragmentAnimator = fragmentAnimator
    }

    override fun getFragmentAnimator(): FragmentAnimator = mDelegate.fragmentAnimator

    /**
     * 按返回键触发,前提是SupportActivity的onBackPressed()方法能被调用
     *
     * @return false则继续向上传递, true则消费掉该事件
     */
    protected fun startWithPop(toFragment: ISupportFragment) = mDelegate.startWithPop(toFragment)

    protected fun startFragment(toFragment: ISupportFragment) = mDelegate.start(toFragment)

    protected fun startFragment(toFragment: ISupportFragment, launchMode: Int) = mDelegate.start(toFragment, launchMode)

    override fun onBackPressedSupport(): Boolean = mDelegate.onBackPressedSupport()

    override fun setFragmentResult(resultCode: Int, bundle: Bundle?) = mDelegate.setFragmentResult(resultCode, bundle)

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) = mDelegate.onFragmentResult(requestCode, resultCode, data)

    override fun onNewBundle(args: Bundle?) = mDelegate.onNewBundle(args)

    override fun putNewBundle(newBundle: Bundle?) = mDelegate.putNewBundle(newBundle)

    /* 其它方法 */
    protected fun pop() = mDelegate.pop()

    /**
     * 启动Activity
     *
     * @param toActivity
     *          即将启动的Activity
     * @param bundle
     *          要传递的参数
     */
    protected fun <T>startActivity(toActivity: Class<T>, bundle: Bundle? = null) {
        val intent = Intent(mActivity, toActivity)
        if(bundle != null){
            intent.putExtra(BaseActivity.PARAM_BUNDLE, bundle)
        }
        startActivity(intent)
    }

    //TODO 修复Android内部输入法的Bug
    protected fun fixInputMethodManagerLeak(context: Context?) {
        context ?: return
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager? ?: return

        val arr = arrayOf("mNextServedView","mCurRootView", "mServedView")
        var f: Field
        var objGet: Any?
        for(param in arr){
            try {
                f = imm.javaClass.getDeclaredField(param)
                if(!f.isAccessible)
                    f.isAccessible = true
                objGet = f.get(imm)
                if(objGet != null && objGet is View){
                    if(objGet.context == context)
                        f.set(imm, null)
                    else {
                        log("context is not suitable, context: ${objGet.context}, destContext: $context")

                    }
                }
            } catch (t: Throwable) {
                log("fixInputMethodManagerLeak Exception: $t")
            }
        }
    }

    fun fixInputMethod(context: Context?) {
        if (context == null) {
            return
        }
        var inputMethodManager: InputMethodManager? = null
        try {
            inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        } catch (th: Throwable) {
            th.printStackTrace()
        }
        if (inputMethodManager == null) {
            return
        }
        val declaredFields = inputMethodManager.javaClass.declaredFields
        for (declaredField in declaredFields) {
            try {
                if (!declaredField.isAccessible) {
                    declaredField.isAccessible = true
                }
                val obj = declaredField.get(inputMethodManager)
                if (obj == null || obj !is View) {
                    continue
                }
                val view: View = obj
                if (view.context === context) {
                    declaredField.set(inputMethodManager, null)
                } else {
                    return
                }
            } catch (th: Throwable) {
                th.printStackTrace()
            }
        }
    }

}