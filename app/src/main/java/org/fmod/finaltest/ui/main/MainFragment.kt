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
import org.fmod.finaltest.bean.bus.BatchDealItem
import org.fmod.finaltest.bean.bus.MainDealItem
import org.fmod.finaltest.bean.bus.NewItemDealItem
import org.fmod.finaltest.bean.bus.StaDealItems
import org.fmod.finaltest.ui.batch.BatchFragment
import org.fmod.finaltest.ui.batch.BatchPresenter
import org.fmod.finaltest.ui.books.BooksFragment
import org.fmod.finaltest.ui.books.BooksPresenter
import org.fmod.finaltest.ui.mine.MineFragment
import org.fmod.finaltest.ui.mine.MinePresenter
import org.fmod.finaltest.ui.newItem.NewItemFragment
import org.fmod.finaltest.ui.newItem.NewItemPresenter
import org.fmod.finaltest.ui.statistics.StaFragment
import org.fmod.finaltest.util.toplevel.dp2px
import org.fmod.finaltest.util.toplevel.log
import org.fmod.finaltest.util.toplevel.statusBarHeight
import org.fmod.finaltest.widget.StaticView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

class MainFragment: BaseFragment(), MainContract.View {

    companion object{
        fun newInstance() = MainFragment()

        private const val REQ_ITEM = 0
        private const val REQ_BATCH = 1

        const val RES_ITEM_OK = 1
        const val RES_BATCH_OK = 2
    }

    lateinit var presenter: MainContract.Presenter

    private lateinit var adapter: MainListAdapter

    private lateinit var staticView: StaticView

    private lateinit var dealItems: ArrayList<DealItem>

    /* 记录要修改的Item索引，用于通知adapter更新 */
    //private var modifyItemPos = 0

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
                EventBus.getDefault().postSticky(NewItemDealItem(null))
                startFragment(NewItemFragment.newInstance().apply {
                    injectPresenter(NewItemPresenter(this))
                })
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
                    //EventBus.getDefault().postSticky(StaDealItems(dealItems))
                    startFragment(StaFragment.newInstance(dealItems))
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

                main_list.addItemDecoration(decoration)

                main_fab.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        all_statistics_container.setPadding(0, statusBarHeight, 0, 0)

        staticView = StaticView(all_statistics_container)

        presenter.loadItems()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_toolbar_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.main_batches -> {
                EventBus.getDefault().postSticky(BatchDealItem(dealItems))
                startFragmentForResult(BatchFragment.newInstance().apply {
                    injectPresenter(BatchPresenter(this))
                }, REQ_BATCH)
            }
        }
        return true
    }


    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun handleNewItem(event: MainDealItem) {
        adapter.addNewItem(event.item)
        staticView.notifyStaticsChange()
    }

    override fun onFragmentResult(requestCode: Int, resultCode: Int, data: Bundle?) {
        super.onFragmentResult(requestCode, resultCode, data)
        //更新列表
        when {
            requestCode == REQ_ITEM && resultCode == RES_ITEM_OK -> {
                adapter.modifyItem()
                staticView.notifyStaticsChange()
            }
            requestCode == REQ_BATCH && resultCode == RES_BATCH_OK -> {
                log("batch ok")
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onBackPressedSupport(): Boolean {
        log("back")
        return true
        //return super.onBackPressedSupport()
    }

    /* MainContract.View Methods */
    override fun injectPresenter(presenter: MainContract.Presenter) {
        this.presenter = presenter
    }

    override fun showItems(items: ArrayList<DealItem>) {
        dealItems = items
        staticView.items = items
        staticView.notifyStaticsChange()
        adapter = MainListAdapter(items) {
            //modifyItemPos = adapter.getPosition(it)
            EventBus.getDefault().postSticky(NewItemDealItem(it))
            startFragmentForResult(NewItemFragment.newInstance().apply {
                injectPresenter(NewItemPresenter(this))
            }, REQ_ITEM)
        }
        main_list.adapter = adapter
        main_list.layoutManager = LinearLayoutManager(context)
    }

}