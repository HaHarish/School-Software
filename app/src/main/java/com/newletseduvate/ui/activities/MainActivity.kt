package com.newletseduvate.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.airpay.airpaysdk_simplifiedotp.*
import com.google.gson.Gson
import com.mikepenz.fastadapter.ISubItem
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.nameText
import com.mikepenz.materialdrawer.util.addItems
import com.newletseduvate.R
import com.newletseduvate.app
import com.newletseduvate.base.BaseActivity
import com.newletseduvate.databinding.ActivityMainBinding
import com.newletseduvate.di.AppModule
import com.newletseduvate.di.DaggerAppComponent
import com.newletseduvate.di.NetworkModule
import com.newletseduvate.model.login.LoginSuccessResponse
import com.newletseduvate.ui.login.LoginActivity
import com.newletseduvate.utils.constants.Constants.MODULE_ID
import com.newletseduvate.utils.constants.ModulesConstant
import com.newletseduvate.utils.extensions.getBaseUrl
import com.newletseduvate.utils.extensions.getLoginResponse
import com.newletseduvate.utils.extensions.setIsUserLoggedIn
import java.util.ArrayList
import java.util.zip.CRC32

class MainActivity : BaseActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    val homeId = 100000L
    val logoutId = 100001L
    var currentModuleId = homeId

    lateinit var pref: SharedPreferences

    val PERMISSION_REQUEST_CODE = 101
    var resp: ResponseMessage? = null
    lateinit var transactionList: ArrayList<Transaction>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pref = PreferenceManager.getDefaultSharedPreferences(this)
        app.appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(application))
                .networkModule(NetworkModule(pref.getBaseUrl()))
                .build()

        app.appComponent.inject(this)

        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }

        setSupportActionBar(binding.toolbar)

        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            com.mikepenz.materialdrawer.R.string.material_drawer_open,
            com.mikepenz.materialdrawer.R.string.material_drawer_close
        )

        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)

        val home = PrimaryDrawerItem().apply { nameText = "Home"; identifier = homeId; }
        binding.slider.apply {
            itemAdapter.add(home)
        }

        val responseModel = getLoginResponseModel()
        populateNavigationDrawer(responseModel)
        binding.slider.setSelection(0)

        val logout = PrimaryDrawerItem().apply { nameText = "Logout"; identifier = logoutId; }
        binding.slider.apply {
            itemAdapter.add(logout)
        }

        binding.slider.onDrawerItemClickListener = { _, drawerItem, _ ->

            if (drawerItem.identifier != currentModuleId) {

                currentModuleId = drawerItem.identifier

                val id = ModulesConstant.getSavedId(drawerItem.identifier.toInt(), pref)

                if (drawerItem.identifier == logoutId) {
                    pref.setIsUserLoggedIn(false)
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val bundle = Bundle()
                    bundle.putInt(MODULE_ID, drawerItem.identifier.toInt())
                    navController.navigate(id, bundle)
                }
            }
            false
        }

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        val callback: OnBackPressedCallback =
                object : OnBackPressedCallback(true /* enabled by default */) {
                    override fun handleOnBackPressed() {
                        when (navController.currentDestination?.id) {
                            R.id.nav_home_fragment -> finish()
                            else -> findNavController(R.id.nav_host_fragment).navigateUp()
                        }

                    }
                }
        this.onBackPressedDispatcher.addCallback(this, callback)

    }



    private fun populateNavigationDrawer(responseModel: LoginSuccessResponse) {

        responseModel.result?.navigationData?.forEach { navigationData ->

            /*if (!navigationData?.parentModules.equals("Communication")){
                if(!navigationData?.parentModules.equals("Lesson Plan")){
                    val childMenuList = getSubMenuList(navigationData!!)
                    if (childMenuList.isNotEmpty()) {
                        binding.slider.apply {
                            addItems(getMenuItem(navigationData))
                        }
                    }
                }
            }*/

            val childMenuList = getSubMenuList(navigationData!!)
            if (childMenuList.isNotEmpty()) {
                binding.slider.apply {
                    addItems(getMenuItem(navigationData))
                }
            }
        }
    }

    private fun getMenuItem(navigationData: LoginSuccessResponse.Result.NavigationData): ExpandableDrawerItem {
        return ExpandableDrawerItem().apply {
            nameText = navigationData.parentModules!!
            identifier = navigationData.id!!.toLong() * 100
            isSelectable = false
            subItems = getSubMenuList(navigationData)
        }
    }

    private fun getSubMenuList(navigationData: LoginSuccessResponse.Result.NavigationData): MutableList<ISubItem<*>> {

        val subMenuList = mutableListOf<SecondaryDrawerItem>()
        navigationData.childModule?.forEach { childModule ->
            ModulesConstant.saveId(navigationData, childModule!!, pref)
            val subMenuItem = SecondaryDrawerItem().apply {
                nameText = childModule.childName!!
                level = 2
                identifier = childModule.childId!!.toLong()
            }

            subMenuList.add(subMenuItem)
        }
        return subMenuList.toMutableList()
    }

    private fun getLoginResponseModel(): LoginSuccessResponse {
        val gson = Gson()
        return gson.fromJson(pref.getLoginResponse(), LoginSuccessResponse::class.java)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        actionBarDrawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onResume() {
        super.onResume()
        actionBarDrawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        if (item.itemId == android.R.id.home) {
            navController.popBackStack()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(_outState: Bundle) {
        var outState = _outState
        outState = binding.slider.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    private fun visibilityNavElements(navController: NavController) {
        binding.toolbar.setNavigationOnClickListener {
            if (navController.currentDestination?.id != R.id.nav_home_fragment)
                findNavController(R.id.nav_host_fragment).navigateUp()
            else
                binding.drawerLayout.openDrawer(binding.slider)
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_home_fragment -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
                    binding.slider.setSelectionAtPosition(0, true)
                }
                else -> {
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        visibilityNavElements(navController)
        return super.onCreateOptionsMenu(menu)
    }


    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        fragment!!.onActivityResult(requestCode, resultCode, data)
    }*/

    override fun onBackPressed() {

        if (binding.drawerLayout.isDrawerOpen(binding.slider))
            binding.drawerLayout.closeDrawer(binding.slider)
        else
            super.onBackPressed()
    }

    fun provideToolbarTitle() {
        setToolbarTitle(R.string.menu_home)
        showHomeButton()
    }

    fun startPaymentGateway(bundle: Bundle){
        val configuration = AirpayConfiguration(
            AirpayConfigurationParams.STAGING,
            AirpayConfigurationParams.AIRPAY_KIT
        )
        val myIntent = Intent(this@MainActivity, AirpayActivity::class.java)
        myIntent.putExtra(Config.EXTRA_AIRPAY_CONFIGURATION, configuration)
    //    myIntent.putExtra("RESPONSEMESSAGE", bundle2)
        myIntent.putExtras(bundle)
        startActivityForResult(myIntent, 120)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        fragment!!.onActivityResult(requestCode, resultCode, data)

        try {
            val bundle = data?.extras
            transactionList = ArrayList<Transaction>()
            transactionList = bundle!!.getSerializable("DATA") as ArrayList<Transaction>
            if (transactionList != null) {
                Toast.makeText(
                    this,
                    transactionList.get(0).getSTATUS() + "\n" + transactionList.get(0)
                        .getSTATUSMSG(),
                    Toast.LENGTH_LONG
                ).show()
                if (transactionList.get(0).getSTATUS() != null) {
                    Log.e("STATUS -> ", "=" + transactionList.get(0).getSTATUS())
                }
                if (transactionList.get(0).getMERCHANTKEY() != null) {
                    Log.e("MERCHANT KEY -> ", "=" + transactionList.get(0).getMERCHANTKEY())
                }
                if (transactionList.get(0).getMERCHANTPOSTTYPE() != null) {
                    Log.e("MERCHANT POST TYPE ", "=" + transactionList.get(0).getMERCHANTPOSTTYPE())
                }
                if (transactionList.get(0).getSTATUSMSG() != null) {
                    Log.e(
                        "STATUS MSG -> ",
                        "=" + transactionList.get(0).getSTATUSMSG()
                    ) //  success or fail
                }
                if (transactionList.get(0).getTRANSACTIONAMT() != null) {
                    Log.e("TRANSACTION AMT -> ", "=" + transactionList.get(0).getTRANSACTIONAMT())
                }
                if (transactionList.get(0).getTXN_MODE() != null) {
                    Log.e("TXN MODE -> ", "=" + transactionList.get(0).getTXN_MODE())
                }
                if (transactionList.get(0).getMERCHANTTRANSACTIONID() != null) {
                    Log.e(
                        "MERCHANT_TXN_ID -> ",
                        "=" + transactionList.get(0).getMERCHANTTRANSACTIONID()
                    ) // order id
                }
                if (transactionList.get(0).getSECUREHASH() != null) {
                    Log.e("SECURE HASH -> ", "=" + transactionList.get(0).getSECUREHASH())
                }
                if (transactionList.get(0).getCUSTOMVAR() != null) {
                    Log.e("CUSTOMVAR -> ", "=" + transactionList.get(0).getCUSTOMVAR())
                }
                if (transactionList.get(0).getTRANSACTIONID() != null) {
                    Log.e("TXN ID -> ", "=" + transactionList.get(0).getTRANSACTIONID())
                }
                if (transactionList.get(0).getTRANSACTIONSTATUS() != null) {
                    Log.e("TXN STATUS -> ", "=" + transactionList.get(0).getTRANSACTIONSTATUS())
                }
                if (transactionList.get(0).getTXN_DATE_TIME() != null) {
                    Log.e("TXN_DATETIME -> ", "=" + transactionList.get(0).getTXN_DATE_TIME())
                }
                if (transactionList.get(0).getTXN_CURRENCY_CODE() != null) {
                    Log.e(
                        "TXN_CURRENCY_CODE -> ",
                        "=" + transactionList.get(0).getTXN_CURRENCY_CODE()
                    )
                }
                if (transactionList.get(0).getTRANSACTIONVARIANT() != null) {
                    Log.e(
                        "TRANSACTIONVARIANT -> ",
                        "=" + transactionList.get(0).getTRANSACTIONVARIANT()
                    )
                }
                if (transactionList.get(0).getCHMOD() != null) {
                    Log.e("CHMOD -> ", "=" + transactionList.get(0).getCHMOD())
                }
                if (transactionList.get(0).getBANKNAME() != null) {
                    Log.e("BANKNAME -> ", "=" + transactionList.get(0).getBANKNAME())
                }
                if (transactionList.get(0).getCARDISSUER() != null) {
                    Log.e("CARDISSUER -> ", "=" + transactionList.get(0).getCARDISSUER())
                }
                if (transactionList.get(0).getFULLNAME() != null) {
                    Log.e("FULLNAME -> ", "=" + transactionList.get(0).getFULLNAME())
                }
                if (transactionList.get(0).getEMAIL() != null) {
                    Log.e("EMAIL -> ", "=" + transactionList.get(0).getEMAIL())
                }
                if (transactionList.get(0).getCONTACTNO() != null) {
                    Log.e("CONTACTNO -> ", "=" + transactionList.get(0).getCONTACTNO())
                }
                if (transactionList.get(0).getMERCHANT_NAME() != null) {
                    Log.e("MERCHANT_NAME -> ", "=" + transactionList.get(0).getMERCHANT_NAME())
                }
                if (transactionList.get(0).getSETTLEMENT_DATE() != null) {
                    Log.e("SETTLEMENT_DATE -> ", "=" + transactionList.get(0).getSETTLEMENT_DATE())
                }
                if (transactionList.get(0).getSURCHARGE() != null) {
                    Log.e("SURCHARGE -> ", "=" + transactionList.get(0).getSURCHARGE())
                }
                if (transactionList.get(0).getBILLEDAMOUNT() != null) {
                    Log.e("BILLEDAMOUNT -> ", "=" + transactionList.get(0).getBILLEDAMOUNT())
                }
                if (transactionList.get(0).getISRISK() != null) {
                    Log.e("ISRISK -> ", "=" + transactionList.get(0).getISRISK())
                }
                val transid: String = transactionList.get(0).getMERCHANTTRANSACTIONID()
                val apTransactionID: String = transactionList.get(0).getTRANSACTIONID()
                val amount: String = transactionList.get(0).getTRANSACTIONAMT()
                val transtatus: String = transactionList.get(0).getTRANSACTIONSTATUS()
                val message: String = transactionList.get(0).getSTATUSMSG()
                val merchantid = "40594" // Please enter Merchant Id
                val username = "5926256" // Please enter Username
                val sParam =
                    "$transid:$apTransactionID:$amount:$transtatus:$message:$merchantid:$username"
                val crc = CRC32()
                crc.update(sParam.toByteArray())
                val sCRC = "" + crc.value
                Log.e("Verified Hash ==", "Verified Hash= $sCRC")
                if (sCRC.equals(transactionList.get(0).getSECUREHASH(), ignoreCase = true)) {
                    Log.e("Airpay Secure ->", " Secure hash matched")
                } else {
                    Log.e("Airpay Secure ->", " Secure hash mismatched")
                }


                //Log.e("Remaining Params-->>","Remaining Params-->>"+transactionList.get(0).getMyMap());

                // This code is to get remaining extra value pair.
                for (key in transactionList.get(0).getMyMap().keys) {
                    Log.e(
                        "EXTRA-->>",
                        "KEY: " + key + " VALUE: " + transactionList.get(0).getMyMap().get(key)
                    )
                    val extra_param: String = transactionList.get(0).getMyMap()
                        .get("PRI_ACC_NO_START")!! // To replace key value as you want
                    Log.e("Extra Param -->", "=$extra_param")
                    transactionList.get(0).getMyMap().get(key)
                }
            }
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            Log.e("Error Message --- >>>", "Error Message --- >>> " + e.message)
        }
    }
}