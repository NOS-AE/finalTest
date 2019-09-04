package org.fmod.finaltest.ui.books

import android.annotation.SuppressLint
import com.trello.rxlifecycle3.android.lifecycle.kotlin.bindToLifecycle
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.helper.local.LocalHelper

class BooksPresenter(
    private val mView: BooksContract.View
): BooksContract.Presenter {

    override fun start() {

    }

    @SuppressLint("CheckResult")
    override fun loadBooks() {
        try {
            mView.showBooks(MyApp.books)
        } catch (e: UninitializedPropertyAccessException) {
            LocalHelper.loadBooks()
                .bindToLifecycle(mView)
                .subscribe {
                    MyApp.books = it
                    mView.showBooks(it)
                }
        }
    }
}