package com.newletseduvate.ui.homework

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.BuildConfig.MEDIA_HOMEWORK_URL
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterHomeworkDetailsQuestionOpenedBinding
import com.newletseduvate.databinding.AdapterStudentHomeworkUploadFileBinding
import com.newletseduvate.databinding.FragmentHomeworkStudentDetailsOpenedBinding
import com.newletseduvate.model.BottomSheetIconItem
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.model.homeWork.HomeWorkStudentModel
import com.newletseduvate.model.homeWork.HomeworkStudentDetailsOpenedModifiedModel
import com.newletseduvate.model.homeWork.HomeworkUploadQuestionFileModel
import com.newletseduvate.ui.bottom_sheets.SingleSelectionIconBottomSheetFragment
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.constants.CameraUtils
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.constants.RequestCode
import com.newletseduvate.utils.extensions.*
import com.newletseduvate.utils.oes_tool.NewOesToolViewModel
import com.newletseduvate.viewmodels.HomeworkStudentDetailOpenedViewModel
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class HomeworkStudentDetailsOpenedFragment :
    BaseFragment(R.layout.fragment_homework_student_details_opened), View.OnClickListener {

    lateinit var binding: FragmentHomeworkStudentDetailsOpenedBinding
    private val viewModel by lazy {
        getViewModel<HomeworkStudentDetailOpenedViewModel>(requireActivity())
    }
    lateinit var adapter: RecyclerDynamicAdapter<AdapterHomeworkDetailsQuestionOpenedBinding, HomeworkStudentDetailsOpenedModifiedModel>
    private val oesViewModel by lazy { getViewModel<NewOesToolViewModel>(requireActivity()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeworkStudentDetailsOpenedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.clear()

        viewModel.moduleId.set(requireArguments().getInt(Constants.MODULE_ID))
        viewModel.homeworkStudentModel.value =
            requireArguments().getSerializable(HomeworkStudentFragment.HomeworkStudentModelConstant) as HomeWorkStudentModel

        val callbacks = ArrayList<CallBackModel<HomeworkStudentDetailsOpenedModifiedModel>>()
        callbacks.add(CallBackModel(R.id.btn_upload) { model, position ->
            if (viewModel.isUploadQuestionWise.get())
                viewModel.currentUploadFilesList = model.uploadHomeworkList
            showOptionMenu()
        })


        adapter =
            RecyclerDynamicAdapter(
                R.layout.adapter_homework_details_question_opened,
                callbacks
            )

        getHomeworkStudentDetails()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        binding.tvHeading.text = requireArguments().getString("heading", "")

        initClickListener()
        initRecyclerView()
        initRecyclerViewBulkUploadRecycler()
        initDataObserver()

        binding.btnBulkUpload.setOnClickListener {
            if (!viewModel.isUploadQuestionWise.get())
                viewModel.currentUploadFilesList = viewModel.uploadFilesList
            showOptionMenu()
        }

    }

    private fun initClickListener() {
        binding.btnSubmit.setOnClickListener(this)

        binding.tilComment.addTextWatchers(binding.etComment)
    }

    private fun initDataObserver() {

        viewModel.homeworkStudentDetailsListResponse.observe(
            viewLifecycleOwner,
            { listResource ->

                val arrayList = listResource.data

                arrayList?.let {

                    val tempList = ArrayList<HomeworkStudentDetailsOpenedModifiedModel>()

//                    val questionFileUploadModelList = ArrayList<MutableLiveData<ArrayList<HomeworkUploadQuestionFileModel>>>()
                    if (viewModel.questionFileUploadModelList.size == 0) {
                        for (i in 0 until arrayList.size) {
                            val upf = MutableLiveData<ArrayList<HomeworkUploadQuestionFileModel>>(
                                ArrayList()
                            )
                            viewModel.questionFileUploadModelList.add(upf)
                        }
                    }

                    for (i in 0 until arrayList.size) {

                        val model = arrayList[i]

                        val ufp = viewModel.questionFileUploadModelList[i]

                        tempList.add(
                            HomeworkStudentDetailsOpenedModifiedModel(
                                model.homeworkId,
                                model.id,
                                model.question,
                                model.questionFiles,
                                model.maxAttachment,
                                model.isPenEditorEnable,
                                viewModel.isUploadQuestionWise,
                                viewLifecycleOwner,
                                viewModel,
                                ufp
                            )
                        )
                    }

                    adapter.replaceList(tempList)
                }

                binding.tvInstruction.text =
                    viewModel.homeworkStudentDetailsOpened.value?.data?.data?.description
            })

        viewModel.homeworkSubmissionResponse.observe(viewLifecycleOwner, {
            if (it) {
                viewModel.homeworkSubmissionResponse.value = false
                findNavController().popBackStack()
            }
        })

        viewModel.uploadFilesList.observe(viewLifecycleOwner, {

            if (!viewModel.isUploadQuestionWise.get())
                adapterFiles.replaceList(it)
        })

        oesViewModel.isSaved.observe(viewLifecycleOwner, {

            if (it == true) {
                Handler(Looper.getMainLooper()).postDelayed({
                    val path = oesViewModel.imageFiles[0].absolutePath
                    prepareFilePart(path, path).let { uploadFileModel ->
                        viewModel.homeworkUploadFileResponse.value = true
                        viewModel.postUploadQuestionFile(uploadFileModel)
                    }
                    oesViewModel.isSaved.value = false
                }, 200)
            }
        })

        viewModel.homeworkUploadFileResponse.observe(viewLifecycleOwner, {

            if (it)
                displayProgress()
            else
                dismissProgress()

        })
    }

    private fun initRecyclerView() {

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvList.layoutManager = linearLayoutManager

        binding.rvList.adapter = adapter
        binding.rvList.setHasFixedSize(true)
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
                            compressImageBitmap(imageFile)
                        } else binding.root.snackBar("This type of file is not supported")
                        i++
                    }
                } else {
                    uri = data.data
                    imageFile = CameraUtils.getPath(requireActivity(), uri!!)
                    if (FileExtension.isImageFile(imageFile!!)) {
                        compressImageBitmap(imageFile)
                    } else binding.root.snackBar("This type of file is not supported")
                }
                RequestCode.REQUEST_CODE_IMAGE_CAPTURE -> try {
                    val urifile = File(mHighQualityImageUri!!.path!!)
                    imageFile = urifile.path
                    if (FileExtension.isImageFile(imageFile)) {
                        compressImageBitmap(imageFile)
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


    private fun getHomeworkStudentDetails() {
        if (NetworkCheck.isInternetAvailable(requireContext())) {
            viewModel.getHomeworkStudentDetails()
        } else {
            if (::binding.isInitialized) {
                binding.root.snackBar(
                    getString(R.string.check_internet),
                    getString(R.string.retry),
                    true
                ) {
                    getHomeworkStudentDetails()
                }
            }
        }
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
                    viewModel.homeworkUploadFileResponse.value = true
                    viewModel.postUploadQuestionFile(file)
                }
            }, {})

        compositeDisposable.add(disposable)
    }

    private fun initRecyclerViewBulkUploadRecycler() {

        val callbacks = ArrayList<CallBackModel<HomeworkUploadQuestionFileModel>>()
        callbacks.add(CallBackModel(R.id.iv_delete_file) { model, position ->
            adapterFiles.removeItemAt(position)
            model.url?.let { viewModel.postDeleteFile(it) }
        })

        callbacks.add(CallBackModel(R.id.tv_file_item) { model, position ->
            model.url?.let {
                requireContext().openMediaFile("$MEDIA_HOMEWORK_URL${model.url}")
            }
        })

        adapterFiles =
            RecyclerDynamicAdapter(R.layout.adapter_student_homework_upload_file, callbacks)
        val layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvListUploadedFiles.layoutManager = layoutManager
        binding.rvListUploadedFiles.adapter = adapterFiles
    }


    private
    var mHighQualityImageUri: Uri? = null

    //    private val dataPartList = ArrayList<UploadFileModel>()
    var compositeDisposable = CompositeDisposable()
    lateinit var adapterFiles: RecyclerDynamicAdapter<AdapterStudentHomeworkUploadFileBinding, HomeworkUploadQuestionFileModel>

    companion object {

        @JvmStatic
        fun newInstance() =
            HomeworkStudentDetailsOpenedFragment()
    }

    private fun fieldsValid(): Boolean {
        var isValid = true

        if (binding.etComment.text.toString().isEmpty()) {
            isValid = false
            binding.etComment.error = resources.getString(R.string.error_blog_title_empty)
        }

        if (!viewModel.isUploadQuestionWise.get()) {
//            if (viewModel.uploadFilesList.value!!.size == 0) {
//                binding.root.snackBar("Please select at least one file to submit")
//                return false
//            }

            var sumOfFilesLimitCount = 0

            for(s in adapter.getItemList())
                sumOfFilesLimitCount += s.maxAttachment

            if(sumOfFilesLimitCount < adapterFiles.getItemList().size){
                binding.root.snackBar("Maximum attachments limited to $sumOfFilesLimitCount")
                return  false
            }

        } else {

            for (i in 0 until adapter.getItemList().size){

                val question = adapter.getItemList()[i]

                if(question.maxAttachment < question.uploadHomeworkList.value?.size!!){
                    binding.root.snackBar("Maximum attachments limited to ${question.maxAttachment} for question number ${i+1}")
                    return  false
                }
            }
        }

        return isValid
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.btnSubmit.id -> {

                if (fieldsValid()) {
                    viewModel.postHomeworkSubmission(
                        binding.etComment.text.toString(),
                        adapter.getItemList()
                    )
                }

            }
        }
    }
}