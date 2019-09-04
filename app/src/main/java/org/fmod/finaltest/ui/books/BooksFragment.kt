package org.fmod.finaltest.ui.books


import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_books.*
import org.fmod.finaltest.BookDecoration

import org.fmod.finaltest.R
import org.fmod.finaltest.adapter.BookAdapter
import org.fmod.finaltest.base.fragment.BaseFragment
import org.fmod.finaltest.bean.Book
import org.fmod.finaltest.util.toplevel.statusBarHeight

/**
 * A simple [Fragment] subclass.
 */
class BooksFragment : BaseFragment(), BooksContract.View {

    companion object {
        fun newInstance() = BooksFragment()
    }

    lateinit var presenter: BooksContract.Presenter

    override fun getLayoutId() = R.layout.fragment_books

    override fun setListener() {
        books_toolbar.setNavigationOnClickListener {
            pop()
        }
    }

    override fun initView() {

        books_toolbar.run {

            layoutParams.height += statusBarHeight
            setPadding(0, statusBarHeight, 0, 0)

        }

        books_list.layoutManager = GridLayoutManager(context, 2)
    }

    override fun afterInitView() {
        presenter.loadBooks()
    }

    /* BooksContract.View Methods */

    override fun injectPresenter(presenter: BooksContract.Presenter) {
        this.presenter = presenter
    }

    override fun showBooks(books: ArrayList<Book>) {
        books_list.adapter = BookAdapter(books)
        books_list.addItemDecoration(BookDecoration())
    }

}
