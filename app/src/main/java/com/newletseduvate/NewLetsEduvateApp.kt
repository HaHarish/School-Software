package com.newletseduvate

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.newletseduvate.di.AppComponent
import com.newletseduvate.di.AppModule
import androidx.multidex.MultiDex

class NewLetsEduvateApp: Application() {

    companion object {
        lateinit var INSTANCE: NewLetsEduvateApp
    }

//    val appComponent: AppComponent by lazy {
//        DaggerAppComponent
//            .builder()
//            .appModule(AppModule(this))
//            .build()
//    }

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        INSTANCE = this
    }
}

val AppCompatActivity.app: NewLetsEduvateApp
    get() = application as NewLetsEduvateApp