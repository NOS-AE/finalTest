package org.fmod.finaltest.base.fragment

interface IBaseUniversal {
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

    fun afterInitView()
}