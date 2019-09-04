package org.fmod.finaltest.base

import androidx.lifecycle.LifecycleOwner

interface BaseView<T>: LifecycleOwner {

    fun injectPresenter(presenter: T)

}