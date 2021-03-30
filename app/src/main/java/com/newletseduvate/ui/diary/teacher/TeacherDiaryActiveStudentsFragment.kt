package com.newletseduvate.ui.diary.teacher

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.newletseduvate.R
import com.newletseduvate.adapter.CircularUploadFileAdapter
import com.newletseduvate.adapter.TeacherDiaryActiveStudentsAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.model.diary.teacher.TeacherDiaryActiveStudentsResponse
import com.newletseduvate.utils.Status
import com.newletseduvate.utils.extensions.getBranchName
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.CircularTeacherViewModel
import com.newletseduvate.viewmodels.TeacherDiaryCreateFilterViewModel
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_teacher_diary_active_students.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TeacherDiaryActiveStudentsFragment: BaseFragment(R.layout.fragment_teacher_diary_active_students),
                TeacherDiaryActiveStudentsInterface {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: TeacherDiaryActiveStudentsAdapter

    lateinit var dataSet: ArrayList<TeacherDiaryActiveStudentsResponse.Result.NameResults>

    private val viewModel by lazy { getViewModel<TeacherDiaryCreateFilterViewModel>(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rv_teacherDiaryCreateFilter_activeStudents.layoutManager = linearLayoutManager

        viewModel.teacherDiaryActiveStudentsList()
        viewModel.teacherDiaryActiveStudentsResponse.observe(viewLifecycleOwner,{
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
                                dataSet = it.data?.result?.results!!
                                adapter = TeacherDiaryActiveStudentsAdapter(dataSet, requireContext(),
                                    this@TeacherDiaryActiveStudentsFragment,
                                    false)
                                rv_teacherDiaryCreateFilter_activeStudents.adapter = adapter
                                adapter.notifyDataSetChanged()
                            }
                            Status.Error -> {

                            }
                            else -> {

                            }
                        }
                    }
                }
            }
        })

        button_teacher_diary_active_students_next.setOnClickListener {
            findNavController().navigate(R.id.nav_teacher_diary_create_general)
        }

        checkBox_teacher_diary_active_students_select_all.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                adapter = TeacherDiaryActiveStudentsAdapter(dataSet, requireContext(),
                    this@TeacherDiaryActiveStudentsFragment,
                    true)
                rv_teacherDiaryCreateFilter_activeStudents.adapter = adapter
                adapter.notifyDataSetChanged()
            }else{
                adapter = TeacherDiaryActiveStudentsAdapter(dataSet, requireContext(),
                    this@TeacherDiaryActiveStudentsFragment,
                    false)
                rv_teacherDiaryCreateFilter_activeStudents.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCheckBoxClicked(position: Int) {

    }

}