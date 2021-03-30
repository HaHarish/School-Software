package com.newletseduvate.ui.finance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
//import com.android.volley.DefaultRetryPolicy
//import com.android.volley.VolleyError
//import com.android.volley.toolbox.JsonObjectRequest
//import com.android.volley.toolbox.RequestFuture
//import com.android.volley.toolbox.Volley
//import com.example.ceaselez.entunircorp.adapter.MyViewpager
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.newletseduvate.R
import com.newletseduvate.adapter.MyViewPagerAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentManagePaymentViewpagerBinding
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.utils.Status
import com.newletseduvate.ui.bottom_sheets.SingleSelectionBottomSheetFragment
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.getBottomSheetList
import com.newletseduvate.utils.extensions.showSingleSelectionBottomSheetWithArrayList
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.FeeStructureViewModel
import com.newletseduvate.viewmodels.ManageFinanceViewModel
//import com.k12.postman.NewSideMenuActivity
//import com.k12.postman.databinding.FragmentManagePaymentViewpagerBinding
//import com.k12.postman.utils.Constant
//import com.k12.postman.utils.Constant.printErrorMessage
//import com.k12.postman.utils.NetworkCheck
//import com.k12.postman.utils.toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.reflect.Type
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.set

private const val ARG_PARAM1 = "param1"

class ManagePaymentFragmentViewPager : BaseFragment(R.layout.fragment_manage_payment_viewpager),
    SingleSelectionBottomSheetFragment.OnChooseReasonListener {
//    , AcademicYearBottomFragment.OnChooseReasonListener


    private val viewModel by lazy { getViewModel<ManageFinanceViewModel>(requireActivity()) }

    private var binding: FragmentManagePaymentViewpagerBinding? = null
    private lateinit var pageChangeListener: ViewPager.OnPageChangeListener
    private var mutableLiveData = MutableLiveData<String>()
    private var selectedstring = ""
    private var string = ArrayList<String>()
    private lateinit var disposable: CompositeDisposable
    private var gson = Gson()
    private var calendar = Calendar.getInstance()
    private var currentYear = ""
    private var nextYear = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposable = CompositeDisposable()

        viewModel.getManageFinanceYear()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentManagePaymentViewpagerBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    private val yearTypeBottomSheetID = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        binding!!.classFilter.setOnClickListener {

            val yearListTemp = ArrayList<BottomSheetItem>()

            viewModel.yearList.value!!.forEach { model ->
                yearListTemp.add(BottomSheetItem(0,model,false))
            }

            showSingleSelectionBottomSheetWithArrayList(childFragmentManager, 0, yearListTemp)
        }

        viewModel.getManageFinanceYearResponse.observe(viewLifecycleOwner, {
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
//                    dismissProgress()
                }
            }
            it?.let {
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        when (it.status) {
                            /*Status.Success -> {

                            }
                            Status.Error -> {
                                layout_login.snackBar(it.message!!)
                            }
                            else -> {
                                layout_login.snackBar(it.message!!)
                            }*/
                        }
                    }
                }
            }
        })

        try {
            val year = calendar[Calendar.YEAR]
            currentYear = year.toString()
            nextYear = (year + 1).toString().substring(2)
            selectedstring = "$currentYear-$nextYear"
            Log.e("selected", "___year___" + currentYear + "_____" + nextYear + "_____" + selectedstring)
            binding!!.classFilter.setText(selectedstring)

        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun onChooseSingleItem(BOTTOM_SHEET_ID: Int, selectedItem: BottomSheetItem) {
        when (BOTTOM_SHEET_ID) {

            yearTypeBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding!!.classFilter.setText(selectedItem.name)
                    viewModel.yearSelected.set(selectedItem.name)
                    viewModel.getFeeDetails()
                    viewModel.getMakePaymentDetails()
                    viewModel.getAllTransactions()
                }
            }

        }
    }

    fun showSingleSelectionBottomSheetWithArrayList(
        fragmentManager: FragmentManager,
        i: Int,
        yearListTemp: ArrayList<BottomSheetItem>
    ){

        for (ele in yearListTemp) {
            if (ele.equals(viewModel.yearSelected.get())) {
                ele.selected = true
                break
            }
        }
        val fragment = SingleSelectionBottomSheetFragment(yearTypeBottomSheetID, yearListTemp)
        fragment.show(fragmentManager, fragment.tag)

    }

    private fun initViewPager() {
        binding!!.tabLayoutNewEbookClass.tabMode = TabLayout.MODE_FIXED
        val adapter = MyViewPagerAdapter(childFragmentManager);
        adapter.addFragment(FeesDetailsFinanceFragment(), "FEE DETAILS")
        adapter.addFragment(MakePaymentFinanceFragment(), "MAKE PAYMENT")
        adapter.addFragment(AllTransactionsFinanceFragment(), "ALL TRANSACTIONS")
        binding!!.viewPagerNewEbookClass.adapter = adapter
        binding!!.tabLayoutNewEbookClass.setupWithViewPager(binding!!.viewPagerNewEbookClass)
        Constants.allotEachTabWithEqualWidth(binding!!.tabLayoutNewEbookClass)
        binding!!.viewPagerNewEbookClass.offscreenPageLimit = 2
    }

    companion object {
        @JvmStatic
        fun newInstance() = ManagePaymentFragmentViewPager()
    }
}