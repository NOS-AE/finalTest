package org.fmod.finaltest.ui.main

import android.util.Log
import io.reactivex.Observable
import org.fmod.finaltest.R
import org.fmod.finaltest.base.activity.BaseActivity
import org.fmod.finaltest.manager.ActivityManager
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription


class MainActivity : BaseActivity() {
    companion object {
        const val TAG = "MyApp"
    }

    override fun beforeSetContentView() {
        ActivityManager.finishOnActivityCreate(this)
    }

    override fun getLayoutId(): Int = R.layout.activity_main


    override fun setListener() {

    }

    override fun initView() {
        transparentStatusBar()
        val fragment = findFragment(MainFragment::class.java)
        if (fragment == null) {
            loadRootFragment(R.id.fragment_container, MainFragment.newInstance().apply {
                injectPresenter(MainPresenter(this))
            })
        }
        attachKeyBoard()
    }

    override fun onDestroy() {
        detachKeyBoard()
        super.onDestroy()
    }



    //TODO 放在网络请求类或Json处理类中
    /**
     * fromJson拓展，reified配合inline使得在运行时泛型被擦除前就确定泛型类型
     *
     * 现用gson-converter替代
     */


    private fun retrofitTest(){
        /*val logInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger{
            override fun log(message: String) {
                networkLog(message)
            }
        })
        logInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val repo = retrofit.create(GetRequest::class.java)
        val call = repo.getCall("square","retrofit")
        call.enqueue(object: RemoteCallback<List<Reception>>("test"){
            override fun onResponse(call: Call<List<Reception>>, response: Response<List<Reception>>) {
                val list = response.body()
                list?.forEach {
                    networkLog("contributor: ${it.login}, contributions: ${it.contributions}")
                }
            }
        })*/
    }

    private fun rxtest() {
        val observable = Observable.create<String> {
            it.onNext("Hello")
            it.onNext("World")
            it.onComplete()
        }


        val disposable = observable.subscribe({
            Log.d(TAG, "onNext: $it")
        }, {
            Log.d(TAG, "onError: ${it.message}")
        }, {
            Log.d(TAG, "onComplete")
        }, {
            Log.d(TAG, "onSubscribe")
        })//.dispose()


        val subscriber = object : Subscriber<String> {
            override fun onComplete() {
                Log.d(TAG, "onComplete2")
            }

            override fun onError(e: Throwable) {
                Log.d(TAG, "onError2")
            }

            override fun onNext(t: String) {
                Log.d(TAG, "onNext2: $t")
            }

            override fun onSubscribe(s: Subscription?) {

                Log.d(TAG, "onSubscribe2")
            }
        }

        val publisher = Publisher<String> {
            it.onNext("Hello")
            it.onNext("World")
            it.onComplete()
        }
        publisher.subscribe(subscriber)
    }

}
