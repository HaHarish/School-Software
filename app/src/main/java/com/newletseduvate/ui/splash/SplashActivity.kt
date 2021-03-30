package com.newletseduvate.ui.splash

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.newletseduvate.R
import com.newletseduvate.app
import com.newletseduvate.base.BaseActivity
import com.newletseduvate.ui.activities.MainActivity
import com.newletseduvate.ui.login.LoginActivity
import com.newletseduvate.utils.extensions.getIsUserLoggedIn
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashActivity: AppCompatActivity() {


    lateinit var sharedPreferences: SharedPreferences

    private val splashTimeOut: Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        app.appComponent.inject(this)
        setContentView(R.layout.activity_splash)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        GlobalScope.launch {
            delay(splashTimeOut)
            var intent: Intent? = null
            intent = if(sharedPreferences.getIsUserLoggedIn()){
                Intent(this@SplashActivity, MainActivity::class.java)
            }else{
                Intent(this@SplashActivity, LoginActivity::class.java)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
            finish()
        }
    }
}