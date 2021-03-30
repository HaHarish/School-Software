package com.newletseduvate.ui.online_class

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterOnlineClassTeacherAttendanceListBinding
import com.newletseduvate.databinding.FragmentOnlineClassCreateClassBinding
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.model.online_class.*
import com.newletseduvate.ui.bottom_sheets.MultiSelectionBottomSheetFragment
import com.newletseduvate.ui.bottom_sheets.SingleSelectionBottomSheetFragment
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.Resource
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.extensions.*
import com.newletseduvate.viewmodels.OnlineClassCreateClassViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OnlineClassCreateClassFragment : BaseFragment(R.layout.fragment_online_class_create_class),
    SingleSelectionBottomSheetFragment.OnChooseReasonListener,
    MultiSelectionBottomSheetFragment.OnChooseReasonListener,
    View.OnClickListener {

    private lateinit var binding: FragmentOnlineClassCreateClassBinding
    private val viewModel by lazy { getViewModel<OnlineClassCreateClassViewModel>(this) }

    lateinit var adapter: RecyclerDynamicAdapter<AdapterOnlineClassTeacherAttendanceListBinding, OnlineClassTeacherAttendanceListModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnlineClassCreateClassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.moduleId.set(requireArguments().getInt(Constants.MODULE_ID))

        setClickListeners()
//        initTextForFilters()

        getInitialData()

        val classTypeList = ArrayList<ClassTypeModel>()
        classTypeList.add(ClassTypeModel(0, "Compulsory Class"))
        classTypeList.add(ClassTypeModel(1, "Optional Class"))
        classTypeList.add(ClassTypeModel(2, "Special Class"))
        classTypeList.add(ClassTypeModel(3, "Parent Class"))


        val days = ArrayList<BottomSheetItem>()
        days.add(BottomSheetItem(0, "Monday"))
        days.add(BottomSheetItem(1, "Tuesday"))
        days.add(BottomSheetItem(2, "Wednesday"))
        days.add(BottomSheetItem(3, "Thursday"))
        days.add(BottomSheetItem(4, "Friday"))
        days.add(BottomSheetItem(5, "Saturday"))
        days.add(BottomSheetItem(6, "Sunday"))

        viewModel.days.value = days

        viewModel.classTypeList.value = Resource.success(classTypeList)

        viewModel.startDate.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                viewModel.getCheckTutorTime()
            }
        })

        viewModel.duration.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                viewModel.getCheckTutorTime()
            }
        })

        viewModel.startTime.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                viewModel.getCheckTutorTime()
            }
        })
    }

    private fun getInitialData() {
        if (NetworkCheck.isInternetAvailable(requireContext())) {
            viewModel.getAcademicYear()
        } else {
            if (::binding.isInitialized) {
                binding.root.snackBar(
                    getString(R.string.check_internet),
                    getString(R.string.retry),
                    true
                ) {
                    getInitialData()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(R.string.menu_create_class))
    }


    override fun onClick(view: View?) {
        when (view?.id) {

            binding.etClassType.id -> {

                if (viewModel.classTypeList.value!!.data != null &&
                    viewModel.classTypeList.value!!.data!!.size > 0
                ) {
                    viewModel.classTypeList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            ClassTypeBottomSheetID,
                            viewModel.classTypeId.get()
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))

            }

            binding.etAcademicYear.id -> {

                if (viewModel.academicYearList.value?.data != null &&
                    viewModel.academicYearList.value?.data?.size!! > 0
                ) {
                    viewModel.academicYearList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            AcademicYearBottomSheetID,
                            viewModel.academicYearId.get()
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))

            }

            binding.etBranch.id -> {

                if (viewModel.branchList.value?.data != null &&
                    viewModel.branchList.value?.data?.size!! > 0
                ) {

                    showSingleSelectionBottomSheetWithArrayList(
                        viewModel.branchList.value!!.data!!.getBottomSheetList(),
                        childFragmentManager,
                        BranchBottomSheetID,
                        getSelectedItemFromBottomSheetList(viewModel.branchList.value!!.data!!.getBottomSheetList())
                    )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))

            }

            binding.etGrade.id -> {

                if (viewModel.gradeList.value?.data != null &&
                    viewModel.gradeList.value?.data?.size!! > 0
                ) {
                    viewModel.gradeList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            GradeBottomSheetID,
                            getSelectedItemFromBottomSheetList(
                                viewModel.gradeList.value!!.data!!.getBottomSheetList()
                            )
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))

            }

            binding.etSection.id -> {

                if (viewModel.sectionList.value?.data != null &&
                    viewModel.sectionList.value?.data!!.size > 0
                ) {
                    viewModel.sectionList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            SectionBottomSheetID,
                            getSelectedItemFromBottomSheetList(
                                viewModel.sectionList.value!!.data!!.getBottomSheetList()
                            )
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))
            }

            binding.etSubject.id -> {

                if (viewModel.subjectList.value?.data != null &&
                    viewModel.subjectList.value?.data!!.size > 0
                ) {
                    viewModel.subjectList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            SubjectBottomSheetID,
                            getSelectedItemFromBottomSheetList(
                                viewModel.subjectList.value!!.data!!.getBottomSheetList()
                            )
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))
            }

            binding.etCourse.id -> {

                if (viewModel.coursesList.value?.data != null &&
                    viewModel.coursesList.value?.data!!.size > 0
                ) {
                    viewModel.coursesList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            CourseBottomSheetID,
                            getSelectedItemFromBottomSheetList(
                                viewModel.coursesList.value!!.data!!.getBottomSheetList()
                            )
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))
            }

            binding.etTutorEmail.id -> {

                if (viewModel.tutorsList.value?.data != null &&
                    viewModel.tutorsList.value?.data!!.size > 0
                ) {
                    viewModel.tutorsList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            TutorBottomSheetID,
                            getSelectedItemFromBottomSheetList(
                                viewModel.tutorsList.value!!.data!!.getBottomSheetList()
                            )
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))
            }

            binding.etCoHosts.id -> {
                if (viewModel.coHostList.value?.data != null &&
                    viewModel.coHostList.value?.data!!.size > 0
                ) {
                    viewModel.coHostList.value!!.data!!.getBottomSheetList()
                        .showMultiSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            CoHostBottoSheetID
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))
            }

            binding.etFromDate.id -> view.datePickerDialogMinToday(viewModel.startDate)
            binding.etStartTime.id -> view.timePicker(viewModel.startTime)

            binding.etDays.id -> viewModel.days.value?.showMultiSelectionBottomSheetWithArrayList(
                childFragmentManager,
                DaysBottomSheetID
            )

            binding.btnApply.id -> {
                if (fieldsValid()) {

                    view.hideKeyboard()

                    val daysToKeyMap = mutableMapOf<String, String>()
                    daysToKeyMap.put("MONDAY", "M")
                    daysToKeyMap.put("TUESDAY", "T")
                    daysToKeyMap.put("WEDNESDAY", "W")
                    daysToKeyMap.put("THURSDAY", "TH")
                    daysToKeyMap.put("FRIDAY", "F")
                    daysToKeyMap.put("SATURDAY", "SA")
                    daysToKeyMap.put("SUNDAY", "S")


                    var sectionMappingID: Int? = -1
                    for (section in viewModel.sectionList.value?.data!!) {
                        if (section.selected) {
                            sectionMappingID = section.id
                        }
                    }

                    val requestDateFormat = SimpleDateFormat(
                        Constants.DateFormat.YYYYMMDD,
                        Locale.getDefault()
                    )
                    val day = Date(viewModel.startDate.get())
                    val startDate = requestDateFormat.format(day)

                    val calendar = Calendar.getInstance()
                    calendar.time = day
                    val dayOfWeek = calendar[Calendar.DAY_OF_WEEK]

                    val days = ArrayList<String>()
                    if (!viewModel.isRecurring.get()) {
                        days.add(daysToKeyMap.getOrDefault(dayOfWeek, "S"))
                    } else {
                        viewModel.days.value?.forEach {
                            if (it.selected)
                                days.add(
                                    daysToKeyMap.getOrDefault(
                                        it.name?.capitalize(Locale.ROOT),
                                        "S"
                                    )
                                )
                        }
                    }

                    var tutorEmails = viewModel.tutorsList.value?.data.getSelectedBottomSheetItemFromList().name

                    if(tutorEmails?.isNotEmpty() == true){
                        viewModel.coHostList.value?.data?.forEach {
                            if(it.selected)
                                tutorEmails += ", ${it.email}"
                        }
                    }

                    val createClassRequest = CreateClassRequest(
                        null,
                        binding.etTitle.text.toString(),
                        binding.etDuration.text.toString(),
                        viewModel.subjectList.value?.data.getSelectedItemsInString(),
                        viewModel.tutorsList.value?.data.getSelectedItemsInString().toInt(),
                        tutorEmails,
                        "Student",
                        "$startDate ${viewModel.startTime.get()}",
                        null,
                        0,
                        viewModel.classTypeId.get(),
                        sectionMappingID.toString(),
                        days,
                        null,
                        binding.etJoinLimit.text.toString(),
                        null,
                        null,
                        null
                    )

                    if (viewModel.isRecurring.get()) {
                        createClassRequest.noOfWeek = binding.etNoOfDays.text.toString().toInt()
                        createClassRequest.isRecurring = 1
                    }
                    if (viewModel.classTypeId.get() != 0) {
                        createClassRequest.course =
                            viewModel.coursesList.value?.data.getSelectedItemsInString().toInt()
                    }

                    val bundle = Bundle()
                    bundle.putSerializable(
                        OnlineClassAttendFragment.modelConstant,
                        createClassRequest
                    )
                    bundle.putInt(
                        "branchId",
                        viewModel.branchList.value?.data.getSelectedItemsInString().toInt()
                    )
                    bundle.putInt(
                        "gradeId",
                        viewModel.gradeList.value?.data.getSelectedItemsInString().toInt()
                    )
                    findNavController().navigate(R.id.nav_create_class_student_filter, bundle)
                }

            }
//
//            binding.etToDate.id -> view.datePickerDialog(viewModel.toDate)
        }
    }


    private fun setClickListeners() {
        binding.etClassType.setOnClickListener(this)
        binding.etAcademicYear.setOnClickListener(this)
        binding.etBranch.setOnClickListener(this)
        binding.etGrade.setOnClickListener(this)
        binding.etSection.setOnClickListener(this)
        binding.etSubject.setOnClickListener(this)
        binding.etCourse.setOnClickListener(this)
        binding.etTutorEmail.setOnClickListener(this)
        binding.etFromDate.setOnClickListener(this)
        binding.etStartTime.setOnClickListener(this)
        binding.etDays.setOnClickListener(this)
        binding.etCoHosts.setOnClickListener(this)

//        binding.btnReset.setOnClickListener(this)
        binding.btnApply.setOnClickListener(this)

        binding.tilClassType.addTextWatchers(binding.etClassType)
        binding.tilAcademicYear.addTextWatchers(binding.etAcademicYear)
        binding.tilBranch.addTextWatchers(binding.etBranch)
        binding.tilGrade.addTextWatchers(binding.etGrade)
        binding.tilTutorEmail.addTextWatchers(binding.etTutorEmail)
        binding.tilSection.addTextWatchers(binding.etSection)
        binding.tilCourse.addTextWatchers(binding.etCourse)
        binding.tilTitle.addTextWatchers(binding.etTitle)
        binding.tilDuration.addTextWatchers(binding.etDuration)
//        binding.tilBatchSize.addTextWatchers(binding.etBatchSize)
        binding.tilStartTime.addTextWatchers(binding.etStartTime)

        binding.tilDays.addTextWatchers(binding.etDays)
        binding.tilNoOfDays.addTextWatchers(binding.etNoOfDays)
    }

    private val BranchBottomSheetID = 0
    private val GradeBottomSheetID = 1
    private val CourseBottomSheetID = 2
    private val BatchLimitBottomSheetID = 3
    private val ClassTypeBottomSheetID = 4
    private val AcademicYearBottomSheetID = 5
    private val SectionBottomSheetID = 6
    private val SubjectBottomSheetID = 7
    private val TutorBottomSheetID = 8
    private val DaysBottomSheetID = 9
    private val CoHostBottoSheetID = 10

    override fun onChooseSingleItem(BOTTOM_SHEET_ID: Int, selectedItem: BottomSheetItem) {
        when (BOTTOM_SHEET_ID) {

            ClassTypeBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etClassType.setText(selectedItem.name)
                    viewModel.classTypeId.set(selectedItem.id)
                }
            }

            AcademicYearBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etAcademicYear.setText(selectedItem.name)
                    viewModel.academicYearId.set(selectedItem.id)

                    viewModel.getBranches()
                    binding.etBranch.setText("")
                }
            }


            BranchBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etBranch.setText(selectedItem.name)

                    val tempArray = ArrayList<BranchModel>()

                    viewModel.branchList.value?.data?.forEach {
                        if (it.id == selectedItem.id) {
                            it.selected = true
                            tempArray.add(it)
                        } else {
                            it.selected = false
                            tempArray.add(it)
                        }
                    }

                    viewModel.branchList.value = Resource.success(tempArray)

                    viewModel.getGrades()
                    binding.etGrade.setText("")
                }
            }

            GradeBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etGrade.setText(selectedItem.name)
                    val tempArray = ArrayList<GradeMappingModel>()

                    viewModel.gradeList.value?.data?.forEach {
                        if (it.gradeId == selectedItem.id) {
                            it.selected = true
                            tempArray.add(it)
                        } else {
                            it.selected = false
                            tempArray.add(it)
                        }
                    }

                    viewModel.gradeList.value = Resource.success(tempArray)

                    viewModel.getCourses()
                    viewModel.getTutorEmailList()

                    binding.etCourse.setText("")
                    binding.etTutorEmail.setText("")
                }
            }

            SectionBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etSection.setText(selectedItem.name)

                    val tempArray = ArrayList<SectionMappingModel>()

                    viewModel.sectionList.value?.data?.forEach {
                        if (it.sectionId == selectedItem.id) {
                            it.selected = true
                            tempArray.add(it)
                        } else {
                            it.selected = false
                            tempArray.add(it)
                        }
                    }

                    viewModel.sectionList.value = Resource.success(tempArray)

//                    viewModel.getSubjectMapping()
                }
            }


            SubjectBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etSubject.setText(selectedItem.name)
                    val tempArray = ArrayList<SubjectModel>()

                    viewModel.subjectList.value?.data?.forEach {
                        if (it.subjectId == selectedItem.id) {
                            it.selected = true
                            tempArray.add(it)
                        } else {
                            it.selected = false
                            tempArray.add(it)
                        }
                    }

                    viewModel.subjectList.value = Resource.success(tempArray)
                }
            }

            CourseBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etCourse.setText(selectedItem.name)
                    val tempArray = ArrayList<CourseModel>()

                    viewModel.coursesList.value?.data?.forEach {
                        if (it.id == selectedItem.id) {
                            it.selected = true
                            tempArray.add(it)
                        } else {
                            it.selected = false
                            tempArray.add(it)
                        }
                    }

                    viewModel.coursesList.value = Resource.success(tempArray)
                }
            }

            TutorBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etTutorEmail.setText(selectedItem.name)
                    val tempArray = ArrayList<CreateClassTutorsModel>()

                    viewModel.tutorsList.value?.data?.forEach {
                        if (it.tutorId == selectedItem.id) {
                            it.selected = true
                            tempArray.add(it)
                        } else {
                            it.selected = false
                            tempArray.add(it)
                        }
                    }

                    viewModel.tutorsList.value = Resource.success(tempArray)

                    val coHostTemp = ArrayList<CreateClassTutorsModel>()

                    viewModel.tutorsList.value?.data?.forEach {
                        val model = it.copy()

                        if (model.tutorId != selectedItem.id) {
                            model.selected = false
                            coHostTemp.add(model)
                        }
                    }
                    viewModel.coHostList.value = Resource.success(coHostTemp)

                    binding.etSection.setText("")
                    binding.etSubject.setText("")
                    binding.etCoHosts.setText("")
                    viewModel.getSubSectionList()
                    viewModel.getCheckTutorTime()
                }
            }
        }
    }

    private fun getSelectedItemFromBottomSheetList(bottomSheetList: ArrayList<BottomSheetItem>): Int {
        bottomSheetList.forEach {
            if (it.selected)
                return it.id!!
        }
        return -1
    }


    fun showSingleSelectionBottomSheetWithArrayList(
        bottomSheetList: ArrayList<BottomSheetItem>,
        fragmentManager: FragmentManager,
        bottomSheetId: Int,
        selectedItemId: Int
    ) {

        for (ele in bottomSheetList) {
            if (ele.id == selectedItemId) {
                ele.selected = true
                break
            }
        }
        val fragment = SingleSelectionBottomSheetFragment(bottomSheetId, bottomSheetList)
        fragment.show(fragmentManager, fragment.tag)

    }


    companion object {

        @JvmStatic
        fun newInstance() = OnlineClassCreateClassFragment()
    }

    override fun onChooseMultipleItem(
        BOTTOM_SHEET_ID: Int,
        originalList: ArrayList<BottomSheetItem>
    ) {
        when (BOTTOM_SHEET_ID) {
            DaysBottomSheetID -> {
                viewModel.days.value = originalList

                binding.etDays.setText(originalList.getSelectedItemsInString())
            }

            CoHostBottoSheetID -> {

                for(i in 0 until originalList.size){
                    viewModel.coHostList.value?.data?.let {
                        it[i].selected = originalList[i].selected
                    }
                }

                binding.etCoHosts.setText(originalList.getSelectedItemsInString())
            }
        }
    }


    private fun fieldsValid(): Boolean {

        var isValid = true

        if (binding.etClassType.text.toString().isEmpty()) {
            isValid = false
            binding.etClassType.error = resources.getString(R.string.error_mandatory_field)
        }

        if (binding.etAcademicYear.text.toString().isEmpty()) {
            isValid = false
            binding.etAcademicYear.error = resources.getString(R.string.error_mandatory_field)
        }

        if (binding.etBranch.text.toString().isEmpty()) {
            isValid = false
            binding.etBranch.error = resources.getString(R.string.error_mandatory_field)
        }

        if (binding.etGrade.text.toString().isEmpty()) {
            isValid = false
            binding.etGrade.error = resources.getString(R.string.error_blog_title_empty)
        }

//
        if (binding.etTutorEmail.text.toString().isEmpty()) {
            isValid = false
            binding.etTutorEmail.error = resources.getString(R.string.error_mandatory_field)
        }

        if (binding.etSection.text.toString().isEmpty()) {
            isValid = false
            binding.etSection.error = resources.getString(R.string.error_mandatory_field)
        }

        if (binding.etTitle.text.toString().isEmpty()) {
            isValid = false
            binding.etTitle.error = resources.getString(R.string.error_blog_title_empty)
        }

        if (binding.etDuration.text.toString().isEmpty()) {
            isValid = false
            binding.etDuration.error = resources.getString(R.string.error_mandatory_field)
        }

//        if (binding.etBatchSize.text.toString().isEmpty()) {
//            isValid = false
//            binding.etBatchSize.error = resources.getString(R.string.error_mandatory_field)
//        }

        if (binding.etStartTime.text.toString().isEmpty()) {
            isValid = false
            binding.etStartTime.error = resources.getString(R.string.error_mandatory_field)
        }

        if (viewModel.classTypeId.get() != 0) {
            if (binding.etCourse.text.toString().isEmpty()) {
                isValid = false
                binding.etCourse.error = resources.getString(R.string.error_mandatory_field)
            }
        }

        if (!viewModel.isTutorAvailable.get()) {
            binding.root.snackBar("Tutor not available")
            return false
        }

        if (viewModel.isRecurring.get()) {
            if (binding.etDays.text.toString().isEmpty()) {
                isValid = false
                binding.etDays.error = resources.getString(R.string.error_mandatory_field)
            }

            if (binding.etNoOfDays.text.toString().isEmpty()) {
                isValid = false
                binding.etNoOfDays.error = resources.getString(R.string.error_mandatory_field)
            }
        }

        return isValid
    }
}