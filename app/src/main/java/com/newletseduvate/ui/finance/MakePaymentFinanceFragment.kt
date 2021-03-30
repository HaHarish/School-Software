package com.newletseduvate.ui.finance

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.airpay.airpaysdk_simplifiedotp.*
import com.newletseduvate.R
import com.newletseduvate.adapter.MakePaymentAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.utils.Status
import com.newletseduvate.ui.activities.MainActivity
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.ManageFinanceViewModel
import kotlinx.android.synthetic.main.fragment_fees_details_finance.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_make_payment_finance.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.zip.CRC32

class MakePaymentFinanceFragment: BaseFragment(R.layout.fragment_make_payment_finance),
                                    ChoosePaymentInterface, ResponseMessage {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: MakePaymentAdapter

    private val viewModel by lazy { getViewModel<ManageFinanceViewModel>(this) }

    private var totalPay: Float = 0F
    var resp: ResponseMessage? = null
    lateinit var transactionList: ArrayList<Transaction>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rb_makePayment_select_school.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                rv_list_make_payment.visibility = View.VISIBLE
                cb_makePayment_termsAndConditions.visibility = View.VISIBLE
                tv_makePayment_termsAndConditions.visibility = View.VISIBLE
                button_makePayment_pay.visibility = View.VISIBLE
            }else{
                rv_list_make_payment.visibility = View.INVISIBLE
                cb_makePayment_termsAndConditions.visibility = View.INVISIBLE
                tv_makePayment_termsAndConditions.visibility = View.INVISIBLE
                button_makePayment_pay.visibility = View.INVISIBLE
            }
        }

        tv_makePayment_termsAndConditions.setOnClickListener {
            showTermsAndConditions()
        }

        button_makePayment_pay.setOnClickListener {
            if(cb_makePayment_termsAndConditions.isChecked){
                callPaymentGateway()
            }else{
                layout_finance_makePayment.snackBar("Please accept the Terms and Conditions")
            }
        }

        viewModel.getMakePaymentDetails()
        viewModel.getMakePaymentResponse.observe(viewLifecycleOwner, {
            GlobalScope.launch {
                withContext(Dispatchers.Main) {
//                    dismissProgress()
                }
            }
            it?.let {
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        when (it.status) {
                            Status.Success -> {
                                rb_makePayment_select_school.text =
                                    it.data?.get(0)?.feeAccountName.toString()
                                adapter = MakePaymentAdapter(
                                    it.data?.get(0)?.installmentsData!!,
                                    this@MakePaymentFinanceFragment
                                )
                                rv_list_make_payment.adapter = adapter
                                adapter.notifyDataSetChanged()
                            }
                            Status.Error -> {
                                layout_login.snackBar(it.message!!)
                            }
                            else -> {
                                layout_login.snackBar(it.message!!)
                            }
                        }
                    }
                }
            }
        })

        linearLayoutManager = LinearLayoutManager(requireContext())
        rv_list_make_payment.layoutManager = linearLayoutManager
    }

    override fun onClickedCheckBox(position: Int, clicked: Boolean) {
        if(clicked){
            totalPay += viewModel.getMakePaymentResponse.value?.data?.get(0)?.installmentsData?.get(
                position
            )?.installmentAmount?.toFloat()!!
        }else{
            totalPay -= viewModel.getMakePaymentResponse.value?.data?.get(0)?.installmentsData?.get(
                position
            )?.installmentAmount?.toFloat()!!
        }

        button_makePayment_pay.text = "Pay Rs.${totalPay}"

        if(totalPay == 0F){
            button_makePayment_pay.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
            button_makePayment_pay.setTextColor(resources.getColor(android.R.color.black))
        }else{
            button_makePayment_pay.setBackgroundColor(resources.getColor(R.color.app_color))
            button_makePayment_pay.setTextColor(resources.getColor(android.R.color.white))
        }
    }

    fun callPaymentGateway(){

        val b = Bundle()


        // Please enter Merchant configuration value


        // Merchant Details


        // Please enter Merchant configuration value


        // Merchant Details
        b.putString("USERNAME", "5926256")
        b.putString("PASSWORD", "me65Pf2K")
        b.putString("SECRET", "A3brM5V9wjMWZh29")
        b.putString("MERCHANT_ID", "40594")



        b.putString("EMAIL", "harish.selvarasu@orchids.edu.in")

        // This is for dynamic phone no value code - Uncomment it

        // This is for dynamic phone no value code - Uncomment it
        b.putString("PHONE", "9994334629")
        /*//  Please enter phone no value
					b.putString("PHONE", "");*/
        /*//  Please enter phone no value
					b.putString("PHONE", "");*/
        b.putString("FIRSTNAME", "Harish")
        b.putString("LASTNAME", "Selvarasu")
        b.putString("ADDRESS", "K12 Techno Services")
        b.putString("CITY", "Bangalore")
        b.putString("STATE", "Karnataka")
        b.putString("COUNTRY", "India")
        b.putString("PIN_CODE", "560066")
        b.putString("ORDER_ID", "Test123")
        b.putString("AMOUNT", "10.00")
        b.putString("CURRENCY", "356")
        b.putString("ISOCURRENCY", "INR")
        b.putString("CHMOD", "")
        b.putString("CUSTOMVAR", "")
        b.putString("TXNSUBTYPE", "")
        b.putString("WALLET", "0")


        // Live Success URL Merchant Id - 1


        // Live Success URL Merchant Id - 1
        b.putString(
            "SUCCESS_URL",
            "https://erp.finance.letseduvate.com/qbox/airpay/airpayresponse"
        ) // Sandbox - SUCCESS

        b.putString("FAILURE_URL", "") // Sandbox - FAILURE


        b.putParcelable("RESPONSEMESSAGE", resp as Parcelable?)

        Log.e("PARAMS -->>", "PARAMS -->>$b")

     //   (activity as MainActivity).startPaymentGateway(b)
    }

    fun quit() {
        val start = Intent(Intent.ACTION_MAIN)
        start.addCategory(Intent.CATEGORY_HOME)
        start.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        start.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(start)
    }

    override fun callback(p0: ArrayList<Transaction>?, p1: Boolean) {

    }

}