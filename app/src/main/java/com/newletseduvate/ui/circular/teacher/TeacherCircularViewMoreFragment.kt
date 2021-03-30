package com.newletseduvate.ui.circular.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newletseduvate.R
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentTeacherCircularViewmoreBinding
import com.newletseduvate.model.circular.teacher.CircularTeacherFilterResponse
import com.newletseduvate.utils.extensions.getBranchName
import kotlinx.android.synthetic.main.fragment_teacher_circular_viewmore.*

class TeacherCircularViewMoreFragment: BaseFragment(R.layout.fragment_teacher_circular_viewmore) {

    lateinit var resultModel: CircularTeacherFilterResponse.Result

    lateinit var binding: FragmentTeacherCircularViewmoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherCircularViewmoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resultModel =
            requireArguments().getSerializable("ViewMore") as CircularTeacherFilterResponse.Result

        binding.model = resultModel
        binding.branchName = sharedPreferences.getBranchName().toString()
        binding.teacherCircular = true
        tv_teacherCircular_viewMore_title.text = resultModel.circularName
        tv_teacherCircular_viewMore_description.text = resultModel.description

    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(resources.getString(R.string.circular_details))
    }

}