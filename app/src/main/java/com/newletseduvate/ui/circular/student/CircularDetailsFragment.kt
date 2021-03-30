package com.newletseduvate.ui.circular.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.newletseduvate.R
import com.newletseduvate.adapter.CircularAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentCircularDetailsBinding
import com.newletseduvate.model.circular.student.CircularResultModel
import com.newletseduvate.utils.extensions.getBranchName
import com.newletseduvate.viewmodels.CircularDetailsViewModel
import kotlinx.android.synthetic.main.fragment_circular_details.*

class CircularDetailsFragment: BaseFragment(R.layout.fragment_circular_details) {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: CircularAdapter

    private val viewModel by lazy { getViewModel<CircularDetailsViewModel>(requireActivity()) }

    private lateinit var circularResultModel: CircularResultModel

    lateinit var binding: FragmentCircularDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCircularDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        circularResultModel =
            requireArguments().getSerializable(CircularFragment.circularModel) as CircularResultModel

        binding.model = circularResultModel
        binding.branchName = sharedPreferences.getBranchName().toString()
        binding.teacherCircular = false

        binding.tvCircularDetailsTitle.text = circularResultModel.circularName
        binding.tvCircularDetailsDescription.text = circularResultModel.description

        /*linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCircularDetailsMedia.layoutManager = linearLayoutManager

        if(circularResultModel.media?.size != 0){
            adapter = CircularAdapter(circularResultModel.media!!, requireContext(), sharedPreferences.getBranchName()!!)
            binding.recyclerViewCircularDetailsMedia.adapter = adapter
            adapter.notifyDataSetChanged()
        }*/

    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(R.string.circular_details))
    }
}