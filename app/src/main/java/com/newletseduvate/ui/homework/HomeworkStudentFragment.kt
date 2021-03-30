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
import com.newletseduvate.databinding.AdapterHomeworkStudentBinding
import com.newletseduvate.databinding.FragmentHomeworkStudentBinding
import com.newletseduvate.model.homeWork.HomeWorkStudentModel
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.utils.rx_component_observables.RxButtonObservable
import com.newletseduvate.viewmodels.HomeworkStudentViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class HomeworkStudentFragment : BaseFragment(R.layout.fragment_homework_student) {

    lateinit var binding: FragmentHomeworkStudentBinding
    private val viewModel by lazy { getViewModel<HomeworkStudentViewModel>(requireActivity()) }
    lateinit var adapter: RecyclerDynamicAdapter<AdapterHomeworkStudentBinding, HomeWorkStudentModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeworkStudentBinding.inflate(inflater, container, false)
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

        getStudentHomeWork()
    }

    private fun initDataObserver() {
        viewModel.studentHomeWorkList.observe(viewLifecycleOwner, {
            adapter.clearList()
            adapter.submitList(it.data!!)
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

        val callbacks = ArrayList<CallBackModel<HomeWorkStudentModel>>()

        callbacks.add(CallBackModel(R.id.ll_homework_not_opened) { model, position ->
            val bundle = Bundle()
            bundle.putInt(Constants.MODULE_ID, viewModel.moduleId.get())
            bundle.putSerializable(HomeworkStudentModelConstant, model)
            bundle.putString("heading", "Homework - ${viewModel.subjectName.get()}, ${model.classDate}")
            findNavController().navigate(R.id.nav_student_homework_details_opened, bundle)
        })

        callbacks.add(CallBackModel(R.id.ll_homework_submitted) { model, position ->
            val bundle = Bundle()
            bundle.putInt(Constants.MODULE_ID, viewModel.moduleId.get())
            bundle.putSerializable(HomeworkStudentModelConstant, model)
            bundle.putString("heading", "Homework - ${viewModel.subjectName.get()}, ${model.classDate}")
            findNavController().navigate(R.id.nav_student_homework_details, bundle)
        })
        callbacks.add(CallBackModel(R.id.ll_homework_opened) { model, position ->
            val bundle = Bundle()
            bundle.putInt(Constants.MODULE_ID, viewModel.moduleId.get())
            bundle.putSerializable(HomeworkStudentModelConstant, model)
            bundle.putString("heading", "Homework - ${viewModel.subjectName.get()}, ${model.classDate}")
            findNavController().navigate(R.id.nav_student_homework_details_opened, bundle)
        })
        callbacks.add(CallBackModel(R.id.ll_homework_evaluated) { model, position ->
            val bundle = Bundle()
            bundle.putInt(Constants.MODULE_ID, viewModel.moduleId.get())
            bundle.putSerializable(HomeworkStudentModelConstant, model)
            bundle.putString("heading", "Homework - ${viewModel.subjectName.get()}, ${model.classDate}")
            findNavController().navigate(R.id.nav_student_homework_details_evaluated, bundle)
        })

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvList.layoutManager = linearLayoutManager

        adapter = RecyclerDynamicAdapter(R.layout.adapter_homework_student, callbacks)
        binding.rvList.adapter = adapter
        binding.rvList.setHasFixedSize(true)
    }

    private fun addItemToRecycler() {
        val recyclerList = ArrayList<HomeWorkStudentModel>()
        viewModel.studentHomeWorkListResponse.value?.data?.data?.rows?.forEach { row ->

            var lastModel:HomeWorkStudentModel? = null

            for(i in row.hwDetails.indices){
                val model = row.hwDetails[i]

                if(model.subject == viewModel.subjectId.get()){
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val dateFormat2 = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                    model.classDate = dateFormat2.format(dateFormat.parse(row.classDate))
                    lastModel = model
                }

            }
            if (lastModel != null) {
                recyclerList.add(lastModel)
            }
        }

        if(recyclerList.size == 0)
            viewModel.isNoDataStudent.set(true)
        else
            viewModel.isNoDataStudent.set(false)

        viewModel.isPageLoadingStudent.observe(viewLifecycleOwner, {
            it?.let {
                if(it)
                    displayProgress()
                else
                    dismissProgress()
            }
        })

        viewModel.studentHomeWorkList.value = Resource.success(recyclerList)
    }

    private fun goToFilter() {
        val filterFragment = HomeworkStudentFilterFragment()
        filterFragment.setOnDone(object : HomeworkStudentFilterFragment.Filter {
            override fun onDone(
                filterClicked: Boolean
            ) {
                viewModel.filterClicked.set(filterClicked)
                if(filterClicked){
                    getStudentHomeWork()
                }
            }
        })

        filterFragment.show(
            (context as FragmentActivity).supportFragmentManager,
            filterFragment.tag
        )
    }

    private fun getStudentHomeWork() {
        if (NetworkCheck.isInternetAvailable(requireContext())) {
            viewModel.getStudentHomeWork()
        } else {
            if(::binding.isInitialized){
                binding.root.snackBar(getString(R.string.check_internet), getString(R.string.retry), true) {
                    getStudentHomeWork()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(R.string.menu_student_homework))
    }

    companion object {

        const val HomeworkStudentModelConstant = "HomeworkStudentModelConstant"

        @JvmStatic
        fun newInstance() = HomeworkStudentFragment()
    }
}