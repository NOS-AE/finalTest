package org.fmod.finaltest.helper.local

import android.annotation.SuppressLint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.fmod.finaltest.MyApp
import org.fmod.finaltest.R
import org.fmod.finaltest.bean.BigKind
import org.fmod.finaltest.bean.Book
import org.fmod.finaltest.bean.DealItem
import org.litepal.LitePal

class LocalHelper {

    companion object {

        //第一次启动App时，先创建固定的大分类
        fun firstApp(){

            LitePal.saveAll(getFixedBigKinds())

            Book().save()
        }

        @SuppressLint("CheckResult")
        fun init() {
            Observable.just(ArrayList(LitePal.findAll(BigKind::class.java)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    MyApp.bigKinds = it
                }
        }

        /**
         * 获取固定的大分类
         */
        private fun getFixedBigKinds(): ArrayList<BigKind> {

            val icons = MyApp.appContext.resources.obtainTypedArray(R.array.bigkinds)
            val names = MyApp.appContext.resources.obtainTypedArray(R.array.bigkinds_name)

            val res = ArrayList<BigKind>()

            for (i in 0 until icons.length()){
                res.add(BigKind().apply {
                    iconResId = icons.getResourceId(i, -1)
                    name = names.getString(i) ?: ""
                })
            }

            icons.recycle()
            names.recycle()

            return res
        }

        fun loadBigKinds(): Observable<ArrayList<BigKind>> {
            return Observable.just(ArrayList(LitePal.findAll(BigKind::class.java)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnNext {
                    MyApp.bigKinds = it
                }
        }

        fun loadBooks(): Observable<ArrayList<Book>> {
            return Observable.just(ArrayList(LitePal.findAll(Book::class.java)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnNext {
                    MyApp.books = it
                }
        }

        fun loadItems(): Observable<ArrayList<DealItem>> {
            return Observable.just(ArrayList(LitePal.findAll(DealItem::class.java)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnNext { list ->
                    list.sortByDescending {
                        it.dateNum
                    }
                    //通过大分类名字
                    //将Item与大分类关联起来
                    for(i in list){
                        i.bigKind = MyApp.bigKinds.find {
                            it.name == i.type
                        } as BigKind
                    }
                }
        }

    }
}