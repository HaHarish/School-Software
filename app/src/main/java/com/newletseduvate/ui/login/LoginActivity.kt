package com.newletseduvate.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.newletseduvate.NewLetsEduvateApp
import com.newletseduvate.R
import com.newletseduvate.app
import com.newletseduvate.base.BaseActivity
import com.newletseduvate.ui.activities.MainActivity
import com.newletseduvate.utils.extensions.getIsUserLoggedIn
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    private lateinit var navController: NavController
//    lateinit var newLetsEduvateApp: NewLetsEduvateApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        app.appComponent.inject(this)

//        newLetsEduvateApp = application as NewLetsEduvateApp
        setContentView(R.layout.activity_login)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_login)
        init()
    }

    private fun init() {
        supportActionBar?.apply {
            title = getString(R.string.login)
            setDisplayHomeAsUpEnabled(false)
        }
        showLoginFragment()
    }

    private fun showLoginFragment() {
        navController.navigate(R.id.nav_login)
    }

    fun setSuccess() {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}