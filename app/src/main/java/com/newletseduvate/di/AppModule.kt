package com.newletseduvate.di

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger module for providing app-level services.
 * [AppComponent]
 */
@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    fun provideResources(application: Application): Resources = application.resources

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }

    /*@Provides
    @Singleton
    fun provideCustomProgressBar(application: Application): CustomProgressBar {
        return CustomProgressBar.getInstance(application)
    }*/
}
