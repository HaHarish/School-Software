package com.newletseduvate.ui.finance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.newletseduvate.R
import com.newletseduvate.adapter.MyViewPagerAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentFinanceFeesViewPagerBinding
import com.newletseduvate.utils.constants.Constants


class FeeStructureViewPager : BaseFragment(R.layout.fragment_finance_fees_view_pager) {

    lateinit var binding: FragmentFinanceFeesViewPagerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentFinanceFeesViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()

    }

    private fun initViewPager() {
        binding.tabLayoutNewEbookClass.tabMode = TabLayout.MODE_FIXED
        val adapter = MyViewPagerAdapter(childFragmentManager);
        adapter.addFragment(DefaultViewWiseFragment(), "Default View Wise")
        adapter.addFragment(DueDateWiseFragment(), "Due Date Wise")
        binding.viewPagerNewEbookClass.adapter = adapter
        binding.tabLayoutNewEbookClass.setupWithViewPager(binding.viewPagerNewEbookClass)
        Constants.allotEachTabWithEqualWidth(binding.tabLayoutNewEbookClass)
        binding.viewPagerNewEbookClass.offscreenPageLimit = 2
    }

    companion object {
        @JvmStatic
        fun newInstance() = FeeStructureViewPager()
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(R.string.menu_fee_structure))
    }
}