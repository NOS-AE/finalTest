package org.fmod.finaltest.base.activity

interface IBaseUniversal {
    /**
     * 需要在setContentView之后的操作
     */
    fun afterSetContentView()
    /**
     * 需要在setContentView之前的操作
     */
    fun beforeSetContentView()
    /**
     * 获取根布局id
     * @return 根布局id
     */
    fun getLayoutId(): Int
    /**
     * 设置View的监听
     */
    fun setListener()

    /**
     * 初始化界面
     */
    fun initView()
}