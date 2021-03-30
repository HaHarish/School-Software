package com.newletseduvate.di

import android.app.Application
import com.newletseduvate.base.BaseBottomSheetFragment
import com.newletseduvate.base.BaseDialogFragment
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.ui.activities.MainActivity
import com.newletseduvate.ui.login.LoginActivity
import com.newletseduvate.ui.splash.SplashActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, NetworkModule::class, ViewModelModule::class]
)
@Suppress("TooManyFunctions")
interface AppComponent {

    fun getApplication(): Application

    fun inject(baseFragment: BaseFragment)

    fun inject(baseBottomSheetFragment: BaseBottomSheetFragment)

    fun inject(baseDialogFragment: BaseDialogFragment)

    fun inject(splashActivity: SplashActivity)

    fun inject(loginActivity: LoginActivity)

    fun inject(mainActivity: MainActivity)

}