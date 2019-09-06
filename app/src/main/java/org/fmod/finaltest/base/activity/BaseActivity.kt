package org.fmod.finaltest.base.activity

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import leakcanary.AppWatcher
import me.yokeyword.fragmentation.*
import me.yokeyword.fragmentation.anim.FragmentAnimator
import org.fmod.finaltest.R
import org.fmod.finaltest.base.BasePresenter
import org.fmod.finaltest.manager.ActivityManager
import org.fmod.finaltest.util.toplevel.log

abstract class BaseActivity: RxAppCompatActivity(), ISupportActivity, IBaseUniversal{
    companion object{
        const val DEFAULT_ENTER_ANIM = R.anim.fade_in_300
        const val DEFAULT_EXIT_ANIM = R.anim.fade_out_300

        const val PARAM_BUNDLE = "bundle"
    }

    private val mDelegate = SupportActivityDelegate(this)

    private lateinit var decorView: View
    private lateinit var globalListener: ViewTreeObserver.OnGlobalLayoutListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDelegate.onCreate(savedInstanceState)
        ActivityManager.onActivityCreate(this)
        beforeSetContentView()
        setContentView(getLayoutId())
        afterSetContentView()
        //为控件设置Listener
        setListener()
        //初始化界面
        initView()
    }

    override fun beforeSetContentView() {
        //留给需要实现的Activity
    }

    override fun afterSetContentView() {
        //留给需要实现的Activity
    }

    override fun getSupportDelegate(): SupportActivityDelegate {
        return mDelegate
    }

    /**
     * 额外的事务：自定义Tag，添加SharedElement动画，操作非回退栈Fragment
     */
    override fun extraTransaction(): ExtraTransaction {
        return mDelegate.extraTransaction()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDelegate.onPostCreate(savedInstanceState)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return mDelegate.dispatchTouchEvent(ev) ||super.dispatchTouchEvent(ev)
    }
    /**
     * 该方法回调时机为,Activity回退栈内Fragment的数量 小于等于1 时,默认finish Activity
     * 请尽量复写该方法,避免复写onBackPress(),以保证SupportFragment内的onBackPressedSupport()回退事件正常执行
     */
    override fun onBackPressedSupport() {
        log("activity back")
        mDelegate.onBackPressedSupport()
    }

    override fun onBackPressed() {
        onBackPressedSupport()
    }

    /**
     * 获取设置全局的动画
     * */
    override fun getFragmentAnimator(): FragmentAnimator {
        return mDelegate.fragmentAnimator
    }

    /**
     * 设置Fragment全局动画
     * */
    override fun setFragmentAnimator(fragmentAnimator: FragmentAnimator?) {
        mDelegate.fragmentAnimator = fragmentAnimator
    }

    /**
     * 构件Fragment转场动画
     * */
    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return mDelegate.onCreateFragmentAnimator()
    }

    /**
     * 前面事务全部执行后，执行该Action
     * */
    override fun post(runnable: Runnable?) {
        mDelegate.post(runnable)
    }

    override fun onDestroy() {

        mDelegate.onDestroy()
        super.onDestroy()
        ActivityManager.onActivityDestroy(this)
        //检测Activity销毁后是否还有引用导致Leak
        AppWatcher.objectWatcher.watch(this)
    }

    /****** 其他方法 ******/

    /**
     * 透明任务栏
     * */
    protected fun transparentStatusBar(){
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT


    }

    /**
     * 启动Activity
     *
     * @param toActivity
     *          即将启动的Activity
     * @param bundle
     *          要传递的参数
     * @param anim
     *          是否启用默认自定义anim动画（淡出淡入），false则使用默认系统定义的动画
     */
    fun <T>startActivity(toActivity: Class<T>, anim: Boolean = false,
                                   bundle: Bundle? = null
                                   ) {
        val intent = Intent(this, toActivity)
        if(bundle != null){
            intent.putExtra(PARAM_BUNDLE, bundle)
        }
        startActivity(intent)
        if(anim){
            overridePendingTransition(DEFAULT_ENTER_ANIM, DEFAULT_EXIT_ANIM)
        }
    }

    /**
     * 启动活动，自定义动画
     * @param enterAnim
     *          入场动画
     * @param exitAnim
     *          出场动画
     */
    fun <T>startActivity(toActivity: Class<T>, enterAnim: Int = -1, exitAnim: Int = -1,
                         bundle: Bundle? = null
    ) {
        val intent = Intent(this, toActivity)
        if(bundle != null){
            intent.putExtra(PARAM_BUNDLE, bundle)
        }
        startActivity(intent)
        overridePendingTransition(enterAnim, exitAnim)
    }

    fun <T>startActivityElementTransition(toActivity: Class<T>, v: View, bundle: Bundle? = null){
        val intent = Intent(this, toActivity)
        if(bundle != null){
            intent.putExtra(PARAM_BUNDLE, bundle)
        }
        val finalBundle = ActivityOptions.makeSceneTransitionAnimation(this, v, v.transitionName).toBundle()
        startActivity(intent,finalBundle)
    }

    fun <T>startAtivityTransition(toActivity: Class<T>, bundle: Bundle? = null){
        val intent = Intent(this, toActivity)
        if(bundle != null){
            intent.putExtra(PARAM_BUNDLE, bundle)
        }
        val finalBundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        startActivity(intent,finalBundle)
    }

    /**
     * @param anim
     *          是否启用默认anim动画（淡出淡入）
     */
    fun finish(anim: Boolean){
        finish()
        if(anim){
            overridePendingTransition(DEFAULT_ENTER_ANIM, DEFAULT_EXIT_ANIM)
        }
    }

    fun finish(enterAnim: Int = 0, exitAnim: Int = 0){
        finish()
        overridePendingTransition(enterAnim, exitAnim)
    }

    /**
     * 获取栈内的Fragment对象
     * */
    protected fun <T: ISupportFragment>findFragment(fragmentClass: Class<T>): T?{
        return SupportHelper.findFragment(supportFragmentManager, fragmentClass)
    }

    /**
     * 加载并作为根Fragment
     * */
    protected fun loadRootFragment(containerId: Int, toFragment: ISupportFragment){
        mDelegate.loadRootFragment(containerId, toFragment)
    }

    /**
     * 启动Fragment
     */
    protected fun startFragment(fragment: ISupportFragment, @ISupportFragment.LaunchMode launchMode: Int){
        mDelegate.start(fragment, launchMode)
    }

    /**
     * 启动Fragment并pop栈顶Fragment
     */
    protected fun startWithPop(fragment: ISupportFragment){
        mDelegate.startWithPop(fragment)
    }

    protected fun attachKeyBoard(){
        decorView = window.decorView ?: return
        val content = findViewById<View>(Window.ID_ANDROID_CONTENT)
        globalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            decorView.getWindowVisibleDisplayFrame(r)

            val height = decorView.context.resources.displayMetrics.heightPixels
            val diff = height - r.bottom

            if(diff != 0) {
                if(content.paddingBottom != diff) {
                    content.setPadding(0, 0, 0, diff)
                }
            }else{
                if(content.paddingBottom != 0) {
                    content.setPadding(0, 0, 0, 0)
                }
            }
        }
        decorView.viewTreeObserver.addOnGlobalLayoutListener(globalListener)
    }

    protected fun detachKeyBoard(){
        decorView.viewTreeObserver.removeOnGlobalLayoutListener(globalListener)
    }
}