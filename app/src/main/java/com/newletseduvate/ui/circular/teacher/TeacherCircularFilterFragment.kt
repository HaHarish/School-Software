package com.newletseduvate.ui.circular.teacher

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.newletseduvate.R
import com.newletseduvate.adapter.CircularAdapter
import com.newletseduvate.adapter.CircularUploadFileAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentTeacherCircularFilterBinding
import com.newletseduvate.model.BottomSheetIconItem
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.ui.bottom_sheets.SingleSelectionBottomSheetFragment
import com.newletseduvate.ui.bottom_sheets.SingleSelectionIconBottomSheetFragment
import com.newletseduvate.utils.Status
import com.newletseduvate.utils.constants.CameraUtils
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.constants.RequestCode
import com.newletseduvate.utils.extensions.*
import com.newletseduvate.viewmodels.CircularTeacherViewModel
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_teacher_circular_filter.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class TeacherCircularFilterFragment: BaseFragment(R.layout.fragment_teacher_circular_filter),
    SingleSelectionBottomSheetFragment.OnChooseReasonListener,
    CircularTeacherUploadFileInterface {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: CircularUploadFileAdapter

    private lateinit var binding: FragmentTeacherCircularFilterBinding

    private val viewModel by lazy { getViewModel<CircularTeacherViewModel>(requireActivity()) }

    private val academicYearBottomSheetID = 0
    private val branchBottomSheetID = 1
    private val gradeBottomSheetID = 2
    private val sectionBottomSheetID = 3
    private val roleBottomSheetID = 4

    private var academicYearId = 0

    private var imageStr: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.clear()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherCircularFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.isAttachClicked.set(false)

        initTextForFilters()
        setUpViews()
        viewModel.circularAcademicYear()

        /* Academic Year */
        binding.editTextCircularTeacherFilterAcademicYear.setOnClickListener {
            val arrayList = ArrayList<BottomSheetItem>()
            viewModel.academicYearList.value?.forEach {
                arrayList.add(it)
            }

            arrayList.showSingleSelectionBottomSheetWithArrayList(
                childFragmentManager,
                academicYearBottomSheetID,
                viewModel.academicYearListId.get()
            )
        }

        binding.editTextCircularTeacherFilterFromDate.setOnClickListener {
            view.datePickerDialog(viewModel.fromDate)
        }
        binding.editTextCircularTeacherFilterToDate.setOnClickListener {
            view.datePickerDialog(viewModel.toDate)
        }

        /* Branch */
        binding.editTextCircularTeacherFilterBranch.setOnClickListener {
            val arrayList = ArrayList<BottomSheetItem>()
            viewModel.branchList.value?.forEach {
                arrayList.add(it)
            }

            arrayList.showSingleSelectionBottomSheetWithArrayList(
                childFragmentManager,
                branchBottomSheetID,
                viewModel.branchListId.get()
            )
        }

        /* Grade */
        binding.editTextCircularTeacherFilterGrade.setOnClickListener {
            val arrayList = ArrayList<BottomSheetItem>()
            viewModel.gradeList.value?.forEach {
                arrayList.add(it)
            }

            arrayList.showSingleSelectionBottomSheetWithArrayList(
                childFragmentManager,
                gradeBottomSheetID,
                viewModel.gradeListId.get()
            )
        }

        /* Section */
        binding.editTextCircularTeacherFilterSection.setOnClickListener {
            val arrayList = ArrayList<BottomSheetItem>()
            viewModel.sectionList.value?.forEach {
                arrayList.add(it)
            }

            arrayList.showSingleSelectionBottomSheetWithArrayList(
                childFragmentManager,
                sectionBottomSheetID,
                viewModel.sectionListId.get()
            )
        }

        /* Role */
        binding.editTextCircularTeacherFilterRole.setOnClickListener {
            val arrayList = ArrayList<BottomSheetItem>()
            viewModel.roleList.value?.forEach {
                arrayList.add(it)
            }

            arrayList.showSingleSelectionBottomSheetWithArrayList(
                childFragmentManager,
                roleBottomSheetID,
                viewModel.roleListId.get()
            )
        }

        /* Bottom Filter apply values */
        binding.buttonTeacherCircularFilterFilter.setOnClickListener {

            if(binding.editTextCircularTeacherFilterAcademicYear.text?.trim().toString().isEmpty()){
                binding.root.snackBar("Select Academic Year")
            }else if(binding.editTextCircularTeacherFilterFromDate.text?.trim().toString().isEmpty()){
                binding.root.snackBar("Select From Date")
            }else if(binding.editTextCircularTeacherFilterToDate.text?.trim().toString().isEmpty()){
                binding.root.snackBar("Select To Date")
            }else if(binding.editTextCircularTeacherFilterBranch.text?.trim().toString().isEmpty()){
                binding.root.snackBar("Select Branch")
            }else if(binding.editTextCircularTeacherFilterGrade.text?.trim().toString().isEmpty()){
                binding.root.snackBar("Select Grade")
            }else if(binding.editTextCircularTeacherFilterSection.text?.trim().toString().isEmpty()){
                binding.root.snackBar("Select Section")
            }else{
                viewModel.circularTeacherFilterResults()
                findNavController().popBackStack()
            }
        }

        binding.buttonTeacherCircularCreateSubmit.setOnClickListener {
            /*if(viewModel.isCreateClassClicked.get()){
                viewModel.circularTitleDesc = binding.editTextCircularTeacherFilterTitle.text?.trim().toString()
                viewModel.circularDescription = binding.editTextCircularTeacherFilterDescription.text?.trim().toString()
                viewModel.circularTeacherCreate()
            }else{
                viewModel.isCreateClassClicked.set(true)
                setUpViews()
            }*/

            viewModel.circularTitleDesc = binding.editTextCircularTeacherFilterTitle.text?.trim().toString()
            viewModel.circularDescription = binding.editTextCircularTeacherFilterDescription.text?.trim().toString()
            viewModel.circularTeacherCreate()

        }

        viewModel.circularCreateResponse.observe(viewLifecycleOwner, {
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
                                binding.root.snackBar(it.data?.message!!)
                                findNavController().popBackStack()
                                viewModel.circularCreateResponse.value = null
                            }
                            Status.Error -> {
                                binding.root.snackBar(it.message!!)
                            }
                            else -> {
                                binding.root.snackBar(it.message!!)
                            }
                        }
                    }
                }
            }
        })

        /* Attach Files Code starts here*/
        binding.buttonTeacherCircularFilterUploadFiles.setOnClickListener {
            viewModel.isAttachClicked.set(true)
            showOptionMenu()
        }

        linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvTeacherCircularFilterListUploadedFiles.layoutManager = linearLayoutManager

        viewModel.listImageUrl.observe(viewLifecycleOwner, {
            adapter = CircularUploadFileAdapter(viewModel.listImageUrl.value!!, requireContext(),
                        this,
                        sharedPreferences.getBranchName())
            binding.rvTeacherCircularFilterListUploadedFiles.adapter = adapter
            adapter.notifyDataSetChanged()
        })

        viewModel.circularTeacherUploadFileResponse.observe(viewLifecycleOwner, {
            GlobalScope.launch {
                withContext(Dispatchers.Main) {

                }
            }
            it?.let {
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        when (it.status) {
                            Status.Success -> {
                                binding.root.snackBar(it.data?.message!!)
                            }
                            Status.Error -> {
                                binding.root.snackBar(it.message!!)
                            }
                            else -> {
                                binding.root.snackBar(it.message!!)
                            }
                        }
                    }
                }
            }
        })

        /* Delete */
        viewModel.circularTeacherDeleteFileResponse.observe(viewLifecycleOwner, {
            GlobalScope.launch {
                withContext(Dispatchers.Main) {

                }
            }
            it?.let {
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        when (it.status) {
                            Status.Success -> {
                                viewModel.listImageUrl.value?.remove(imageStr)
                                viewModel.listImageUrl.value = viewModel.listImageUrl.value
                                adapter.notifyDataSetChanged()
                                binding.root.snackBar(it.data?.message!!)
                            }
                            Status.Error -> {
                                binding.root.snackBar(it.message!!)
                            }
                            else -> {
                                binding.root.snackBar(it.message!!)
                            }
                        }
                    }
                }
            }
        })

        /* Attach Files code ends here */
    }

    private fun showOptionMenu() {
        val optionsArray = ArrayList<BottomSheetIconItem>()

        optionsArray.add(
            BottomSheetIconItem(
                0,
                R.drawable.ic_camera,
                resources.getString(R.string.camera)
            )
        )

        optionsArray.add(
            BottomSheetIconItem(
                1,
                R.drawable.ic_menu_gallery,
                resources.getString(R.string.gallery)
            )
        )
        val bottomSheet = SingleSelectionIconBottomSheetFragment(
            0,
            optionsArray,
            resources.getString(R.string.select_file_from),
            object : SingleSelectionIconBottomSheetFragment.OnChooseReasonListener {

                override fun onChooseSingleItem(
                    BOTTOM_SHEET_ID: Int,
                    selectedItem: BottomSheetIconItem,
                    bottomSheetItemPostion: Int
                ) {
                    when (selectedItem.id) {
                        0 -> openCamera()
                        1 -> openGallery()
                    }
                }

            }
        )
        bottomSheet.show(
            childFragmentManager,
            bottomSheet.tag
        )
    }

    private fun openCamera() {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        if (Build.VERSION.SDK_INT >= 23) {
            if (requireActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    RequestCode.RC_CAMERA_PERMISSION
                )
            } else {
                cameraOpen()

            }
        } else {
            cameraOpen()
        }
    }

    private fun cameraOpen() {
        mHighQualityImageUri = CameraUtils.generateTimeStampPhotoFileUri(requireActivity())
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mHighQualityImageUri)
        startActivityForResult(
            intent,
            RequestCode.REQUEST_CODE_IMAGE_CAPTURE
        )
    }

    private fun openGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    RequestCode.RC_STORAGE_PERMISSION
                )
            } else {
                galleryOpen()
            }
        } else {
            galleryOpen()
        }
    }

    private fun galleryOpen() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(
            intent,
            RequestCode.REQUEST_CODE_GALLERY
        )
    }


    private fun compressImageBitmap(path: String) {
        val disposable = Compressor(requireContext())
            .setMaxWidth(640)
            .setMaxHeight(480)
            .setQuality(requireContext().quality(path))
            .compressToFileAsFlowable(File(path))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                prepareFilePart(getMimeType(it.path)!!, it.path).let { file ->
//                    viewModel.homeworkUploadFileResponse.value = true
                    // viewModel.postUploadQuestionFile(it)
                    // I guess, Upload to server here
                    Log.d("TAGY","UPLOADY: "+file.toString())

                    viewModel.circularTeacherUploadFile(file)
                }
            }, {})

        compositeDisposable.add(disposable)
    }

    private var mHighQualityImageUri: Uri? = null

    //    private val dataPartList = ArrayList<UploadFileModel>()
    var compositeDisposable = CompositeDisposable()
//    lateinit var adapterFiles: RecyclerDynamicAdapter<AdapterStudentHomeworkUploadFileBinding, HomeworkUploadQuestionFileModel>

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        var uri: Uri?
        var imageFile: String?
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                RequestCode.REQUEST_CODE_GALLERY -> if (data!!.clipData != null) {
                    var i = 0
                    while (i < data.clipData!!.itemCount) {
                        uri = data.clipData!!.getItemAt(i).uri
                        imageFile = CameraUtils.getPath(requireActivity(), uri)
                        if (FileExtension.isImageFile(imageFile!!)) {
                            if(viewModel.isAttachClicked.get()){
                                compressImageBitmap(imageFile)
                            }
                        } else binding.root.snackBar("This type of file is not supported")
                        i++
                    }
                } else {
                    uri = data.data
                    imageFile = CameraUtils.getPath(requireActivity(), uri!!)
                    if (FileExtension.isImageFile(imageFile!!)) {
                        if(viewModel.isAttachClicked.get()){
                            compressImageBitmap(imageFile)
                        }
                    } else binding.root.snackBar("This type of file is not supported")
                }
                RequestCode.REQUEST_CODE_IMAGE_CAPTURE -> try {
                    val urifile = File(mHighQualityImageUri!!.path!!)
                    imageFile = urifile.path
                    if (FileExtension.isImageFile(imageFile)) {
                        if(viewModel.isAttachClicked.get()){
                            compressImageBitmap(imageFile)
                        }
                    } else binding.root.snackBar("This type of file is not supported")
                } catch (e: Exception) {
                    binding.root.snackBar("Image is too large. Choose other")
                }
            }
        }
    }

    private fun prepareFilePart(
        partName: String,
        fileUri: String
    ): UploadFileModel {
        val file = File(fileUri)
        val requestFile = file.asRequestBody(getMimeType(fileUri)?.toMediaTypeOrNull())
        return UploadFileModel(file, requestFile)
    }

    private fun getMimeType(url: String): String? {
        try {
            var type: String? = null
            val extension = url.substring(url.lastIndexOf(".") + 1, url.length)
            Log.i("extension", "ext : $extension")
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    extension.toLowerCase(
                        Locale.getDefault()
                    )
                )
            }
            return type
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /* Attach Files code ends here */
    /* Main Code Starts Here */
    private fun setUpViews() {
        if(viewModel.isCreateClassClicked.get()){
            binding.layoutTeacherCircularFilterFromToDate.visibility = View.GONE
            binding.textInputLayoutCircularTeacherFilterRole.visibility = View.VISIBLE
            binding.layoutTeacherCircularFilterFromToDate.visibility = View.GONE
            binding.layoutTeacherCircularFilterClearFilter.visibility = View.GONE
            binding.textInputLayoutCircularTeacherFilterTitle.visibility = View.VISIBLE
            binding.textInputLayoutCircularTeacherFilterDescription.visibility = View.VISIBLE
            binding.buttonTeacherCircularFilterUploadFiles.visibility = View.VISIBLE
            binding.rvTeacherCircularFilterListUploadedFiles.visibility = View.VISIBLE
            binding.buttonTeacherCircularCreateSubmit.visibility = View.VISIBLE
        }else{
            binding.layoutTeacherCircularFilterFromToDate.visibility = View.VISIBLE
            binding.textInputLayoutCircularTeacherFilterRole.visibility = View.GONE
            binding.layoutTeacherCircularFilterFromToDate.visibility = View.VISIBLE
            binding.layoutTeacherCircularFilterClearFilter.visibility = View.VISIBLE
            binding.textInputLayoutCircularTeacherFilterTitle.visibility = View.GONE
            binding.textInputLayoutCircularTeacherFilterDescription.visibility = View.GONE
            binding.buttonTeacherCircularFilterUploadFiles.visibility = View.GONE
            binding.rvTeacherCircularFilterListUploadedFiles.visibility = View.GONE
            binding.buttonTeacherCircularCreateSubmit.visibility = View.GONE
        }
    }

    override fun onChooseSingleItem(BOTTOM_SHEET_ID: Int, selectedItem: BottomSheetItem) {
        when(BOTTOM_SHEET_ID){
            academicYearBottomSheetID -> {
                if (selectedItem.id != null) {

                    editText_circularTeacherFilter_academicYear.setText(selectedItem.name)
                    viewModel.academicYearId = selectedItem.id
                    viewModel.circularBranch()
                }
            }

            branchBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_circularTeacherFilter_branch.setText(selectedItem.name)
                    viewModel.branchId = selectedItem.id
                    viewModel.circularGrade()
                    viewModel.circularRole()
                }
            }

            gradeBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_circularTeacherFilter_grade.setText(selectedItem.name)
                    viewModel.gradeId = selectedItem.id
                    viewModel.circularSection()
                }
            }

            sectionBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_circularTeacherFilter_section.setText(selectedItem.name)
                    viewModel.sectionId = selectedItem.id
                }
            }

            roleBottomSheetID -> {
                if(selectedItem.id != null){
                    editText_circularTeacherFilter_role.setText(selectedItem.name)

                }
            }
        }
    }

    private fun initTextForFilters() {
        /* Academic Year */
        viewModel.academicYearList.value?.let {
            onChooseSingleItem(
                academicYearBottomSheetID,
                getBottomSheetItem(viewModel.academicYearList.value, viewModel.academicYearListId.get())
            )
        }

        /* Branch */
        viewModel.branchList.value?.let {
            onChooseSingleItem(
                branchBottomSheetID,
                getBottomSheetItem(viewModel.branchList.value, viewModel.branchListId.get())
            )
        }

        /* Grade */
        viewModel.gradeList.value?.let {
            onChooseSingleItem(
                gradeBottomSheetID,
                getBottomSheetItem(viewModel.gradeList.value, viewModel.gradeListId.get())
            )
        }

        /* Section */
        viewModel.sectionList.value?.let {
            onChooseSingleItem(
                sectionBottomSheetID,
                getBottomSheetItem(viewModel.sectionList.value, viewModel.sectionListId.get())
            )
        }

        /* Role */
        viewModel.roleList.value?.let {
            onChooseSingleItem(
                roleBottomSheetID,
                getBottomSheetItem(viewModel.roleList.value, viewModel.roleListId.get())
            )
        }
    }

    private fun getBottomSheetItem(subjectList: ArrayList<BottomSheetItem>?, id: Int): BottomSheetItem {
        subjectList?.forEach {
            if (it.id == id)
                return it
        }
        return BottomSheetItem(-1,"", false)
    }

    override fun onResume() {
        super.onResume()
        setToolBarTitle(getString(R.string.circular_filter))
    }

    companion object {

        @JvmStatic
        fun newInstance() = TeacherCircularFilterFragment()
    }

    override fun onClickedDeleteButton(position: Int) {
        imageStr = viewModel.listImageUrl.value?.get(position)
        viewModel.circularTeacherDeleteFile("dev/circular_files/${sharedPreferences.getBranchName()}/${viewModel.listImageUrl.value?.get(position)}")
    }
}