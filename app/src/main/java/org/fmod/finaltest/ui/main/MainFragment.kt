package org.fmod.finaltest.ui.main

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.android.synthetic.main.all_statistics_container.*
import kotlinx.android.synthetic.main.fragment_main.*
import org.fmod.finaltest.DealDecoration
import org.fmod.finaltest.R
import org.fmod.finaltest.adapter.MainListAdapter
import org.fmod.finaltest.base.activity.BaseActivity
import org.fmod.finaltest.base.fragment.BaseFragment
import org.fmod.finaltest.bean.DealItem
import org.fmod.finaltest.bean.MainListGroupInfo
import org.fmod.finaltest.bean.bus.MainDealItem
import org.fmod.finaltest.ui.books.BooksFragment
import org.fmod.finaltest.ui.books.BooksPresenter
import org.fmod.finaltest.ui.mine.MineFragment
import org.fmod.finaltest.ui.mine.MinePresenter
import org.fmod.finaltest.ui.newItem.NewItemFragment
import org.fmod.finaltest.ui.newItem.NewItemPresenter
import org.fmod.finaltest.util.toplevel.dp2px
import org.fmod.finaltest.util.toplevel.log
import org.fmod.finaltest.util.toplevel.statusBarHeight
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

class MainFragment: BaseFragment(), MainContract.View {

    companion object{
        fun newInstance() = MainFragment()
    }

    lateinit var presenter: MainContract.Presenter

    override fun getLayoutId(): Int = R.layout.fragment_main

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        EventBus.getDefault().register(this)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @SuppressLint("CheckResult")
    override fun setListener(){
        //FloatingActionButton
        main_fab.clicks()
            .throttleFirst(
            1L, TimeUnit.SECONDS)
            .subscribe {
            val toStart = NewItemFragment.newInstance().apply {
                injectPresenter(NewItemPresenter(this))
            }
            startFragment(toStart)
            //(activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)?.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
        }

        //Toolbar里选择账本的TextView
        main_book.clicks().throttleFirst(
            1L,TimeUnit.SECONDS
        ).subscribe {
            startFragment(BooksFragment.newInstance().apply {
                injectPresenter(BooksPresenter(this))
            })
        }

        //AppBottomBar的按钮
        main_bottom_bar.setOnMenuItemClickListener {
            when(it.itemId){
                //启动统计数据界面
                R.id.statistics -> {

                }
                //启动我的界面
                R.id.mine -> {
                    startFragment(MineFragment.newInstance().apply {
                        injectPresenter(MinePresenter(this))
                    })
                }
            }
            true
        }

        //Navigation 还不知道要用来启动什么界面
        main_bottom_bar.setNavigationOnClickListener{

        }

    }

    //初始化控件
    override fun initView() {
        main_toolbar.run{
            (activity as BaseActivity).setSupportActionBar(this)
            setHasOptionsMenu(true)
            (activity as BaseActivity).supportActionBar?.setDisplayShowTitleEnabled(false)

            layoutParams.height += statusBarHeight
            setPadding(0, statusBarHeight,0,0)

            presenter.loadItems()
        }

        main_fab.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                val offset = Resources.getSystem().displayMetrics.heightPixels - main_fab.top + dp2px(
                    16f
                )
                val decoration = DealDecoration(offset, dp2px(18f), dp2px(17f).toFloat(),object: DealDecoration.Callback {
                    override fun getGroupInfo(pos: Int): MainListGroupInfo {
                        return MainListGroupInfo(0, 0, "asd", 1)
                    }
                })


                main_fab.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        all_statistics_container.setPadding(0, statusBarHeight, 0, 0)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_toolbar_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun handleNewItem(event: MainDealItem) {
        log(event.item.toString())
    }

    override fun injectPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    override fun showItems(items: ArrayList<DealItem>) {
        main_list.adapter = MainListAdapter(items)
        main_list.layoutManager = LinearLayoutManager(context)
    }

}