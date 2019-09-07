package org.fmod.finaltest.helper.remote.api

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.fmod.finaltest.util.toplevel.timeOutSendCode
import org.fmod.finaltest.util.toplevel.urlMainFrame
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceProvider {

    companion object {

        private val client = OkHttpClient.Builder()
            .connectTimeout(timeOutSendCode, TimeUnit.SECONDS)
            .readTimeout(timeOutSendCode, TimeUnit.SECONDS)
            .writeTimeout(timeOutSendCode, TimeUnit.SECONDS)
            .build()

        private val gson = GsonBuilder()
            .setLenient()
            .create()

        private val retrofit = Retrofit.Builder()
            .baseUrl(urlMainFrame)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()

        fun registerService(): RegisterService {
            return retrofit.create(RegisterService::class.java)
        }

        fun loginService(): LoginService {
            return retrofit.create(LoginService::class.java)
        }

        fun mineService(): MineService {
            return retrofit.create(MineService::class.java)
        }

        fun qqLoginService(): QQLoginService {
            return retrofit.create(QQLoginService::class.java)
        }

        fun bookService(): BookService {
            return retrofit.create(BookService::class.java)
        }

    }
}