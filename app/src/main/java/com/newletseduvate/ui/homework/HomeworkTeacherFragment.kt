package com.newletseduvate.ui.homework

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterHomeworkTeacherBinding
import com.newletseduvate.databinding.FragmentHomeworkTeacherBinding
import com.newletseduvate.model.homeWork.HomeworkTeacherModel
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.utils.rx_component_observables.RxButtonObservable
import com.newletseduvate.viewmodels.HomeworkTeacherViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class HomeworkTeacherFragment : BaseFragment(R.layout.fragment_homework_teacher) {

    lateinit var binding: FragmentHomeworkTeacherBinding
    private val viewModel by lazy { getViewModel<HomeworkTeacherViewModel>(requireActivity()) }
    lateinit var adapter: RecyclerDynamicAdapter<AdapterHomeworkTeacherBinding, HomeworkTeacherModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeworkTeacherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.moduleId.set(requireArguments().getInt(Constants.MODULE_ID))

        initRecyclerView()
        initDataObserver()

        CompositeDisposable().add(
            RxButtonObservable.fromView(binding.ivFilter)
                .debounce(Constants.FILTER_DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    goToFilter()
                },{

                })
        )

        getTeacherHomeWork()
    }

    private fun initDataObserver() {
        viewModel.studentHomeWorkList.observe(viewLifecycleOwner, {
            adapter.replaceList(it.data!!)
        })

        viewModel.subjectList.observe(viewLifecycleOwner, {
            if(viewModel.subjectId.get() == -1 && it.size > 0){
                viewModel.subjectId.set(it[0].id!!)
                viewModel.subjectName.set(it[0].name)
            }
            addItemToRecycler()
        })

    }

    private fun initRecyclerView() {

        val callbacks = ArrayList<CallBackModel<HomeworkTeacherModel>>()

        callbacks.add(CallBackModel(R.id.ll_homework_add_homework) { model, _ ->
            val bundle = Bundle()
            bundle.putInt(Constants.MODULE_ID, viewModel.moduleId.get())
            bundle.putString("subject_id", viewModel.subjectId.get().toString())
            bundle.putSerializable(HomeworkTeacherModelConstant, model)
            bundle.putString("heading", "Homework - ${viewModel.subjectName.get()}, ${model.classDate}")
            findNavController().navigate(R.id.nav_teacher_homework_add_homework, bundle)
        })

        callbacks.add(CallBackModel(R.id.ll_homework_teacher_evaluated) { model, _ ->
            val bundle = Bundle()
            bundle.putString("subject_id", viewModel.subjectId.get().toString())
            bundle.putSerializable(HomeworkTeacherModelConstant, model)
            bundle.putString("heading", "Homework - ${viewModel.subjectName.get()}, ${model.classDate}")
            findNavController().navigate(R.id.nav_teacher_homework_details_hw_evaluated, bundle)
        })

        callbacks.add(CallBackModel(R.id.ll_homework_student_submitted) { model, _ ->
            val bundle = Bundle()
            bundle.putString("subject_id", viewModel.subjectId.get().toString())
            bundle.putSerializable(HomeworkTeacherModelConstant, model)
            bundle.putString("heading", "Homework - ${viewModel.subjectName.get()}, ${model.classDate}")
            findNavController().navigate(R.id.nav_teacher_homework_details_hw_submitted, bundle)
        })

        callbacks.add(CallBackModel(R.id.ll_homework_submitted) { model, position ->
            val bundle = Bundle()
            bundle.putString("hw_details_id", model.id.toString())
            bundle.putSerializable(HomeworkTeacherModelConstant, model)
            bundle.putString("heading", "Homework - ${viewModel.subjectName.get()}, ${model.classDate}")
            findNavController().navigate(R.id.nav_teacher_homework_details_submitted, bundle)
        })


        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvList.layoutManager = linearLayoutManager

        adapter = RecyclerDynamicAdapter(R.layout.adapter_homework_teacher, callbacks)
        binding.rvList.adapter = adapter
        binding.rvList.setHasFixedSize(true)
    }

    private fun addItemToRecycler() {
        val recyclerList = ArrayList<HomeworkTeacherModel>()
        viewModel.studentHomeWorkListResponse.value?.data?.data?.rows?.forEach { row ->

            if(row.hwDetails?.size != 0){

                var isAtLeastOneAdded = false
                row.hwDetails?.forEach { model ->
                    if(model.subject == viewModel.subjectId.get()){
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val dateFormat2 = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        model.classDate = dateFormat2.format(dateFormat.parse(row.classDate))
//                        model.classDate = row.classDate
                        recyclerList.add(model)
                        isAtLeastOneAdded = isAtLeastOneAdded || true
                    }
                }
                if(!isAtLeastOneAdded)
                    recyclerList.add(HomeworkTeacherModel(null, null, null, row.classDate, true, row.canUpload))

            }else{
                recyclerList.add(HomeworkTeacherModel(null, null, null, row.classDate, true, row.canUpload))
            }

        }

        if(recyclerList.size == 0)
            viewModel.isNoDataStudent.set(true)
        else
            viewModel.isNoDataStudent.set(false)

        viewModel.isPageLoadingStudent.observe(viewLifecycleOwner, {
            it?.let {
                if(it) {
                    displayProgress()
                    viewModel.isPageLoadingStudent.value = false
                }
                else
                    dismissProgress()
            }
        })

        viewModel.studentHomeWorkList.value = Resource.success(recyclerList)
    }

    private fun goToFilter() {
        val filterFragment = HomeworkTeacherFilterFragment()
        filterFragment.setOnDone(object : HomeworkTeacherFilterFragment.Filter {
            override fun onDone(
                filterClicked: Boolean
            ) {
                viewModel.filterClicked.set(filterClicked)
                if(filterClicked){
                    getTeacherHomeWork()
                }
            }
        })

        filterFragment.show(
            (context as FragmentActivity).supportFragmentManager,
            filterFragment.tag
        )
    }

    private fun getTeacherHomeWork() {
        if (NetworkCheck.isInternetAvailable(requireContext())) {
            viewModel.getTeacherHomeWork()
        } else {
            if(::binding.isInitialized){
                binding.root.snackBar(getString(R.string.check_internet), getString(R.string.retry), true) {
                    getTeacherHomeWork()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(R.string.menu_teacher_homework))
    }

    companion object {

        const val HomeworkTeacherModelConstant = "HomeworkTeacherModelConstant"

        @JvmStatic
        fun newInstance() = HomeworkTeacherFragment()
    }
}