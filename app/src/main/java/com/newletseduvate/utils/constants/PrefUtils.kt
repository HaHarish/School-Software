package com.newletseduvate.utils.constants

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import javax.inject.Singleton

/**
 * class to provide functions to access shared preference values
 */
@Singleton
class PrefUtils private constructor(){

    companion object {
        private val sharePref = PrefUtils()
        private lateinit var sharedPreferences: SharedPreferences

        private val PLACE_OBJ = "place_obj"

        fun getInstance(context: Context): PrefUtils {
            if (!Companion::sharedPreferences.isInitialized) {
                synchronized(PrefUtils::class.java) {
                    if (!Companion::sharedPreferences.isInitialized) {
                        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                    }
                }
            }
            return sharePref
        }
    }

    fun setLoginResponse(value: String?){
        val editor = sharedPreferences.edit()
        editor.putString(PrefKeys.LOGIN_RESPONSE, value)
        editor.apply()
    }

    fun getLoginResponse(): String?{
        return sharedPreferences.getString(PrefKeys.LOGIN_RESPONSE, null)
    }
}