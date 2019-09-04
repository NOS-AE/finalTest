package org.fmod.finaltest.ui.books

import org.fmod.finaltest.base.BasePresenter
import org.fmod.finaltest.base.BaseView
import org.fmod.finaltest.bean.Book

interface BooksContract {
    interface View: BaseView<Presenter> {
        fun showBooks(books: ArrayList<Book>)
    }

    interface Presenter: BasePresenter {
        fun loadBooks()
    }
}