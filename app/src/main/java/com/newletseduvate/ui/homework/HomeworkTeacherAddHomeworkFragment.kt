package com.newletseduvate.ui.homework

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
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterHomeworkAddQuestionBinding
import com.newletseduvate.databinding.FragmentHomeworkTeacherAddHomeworkBinding
import com.newletseduvate.model.BottomSheetIconItem
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.model.homeWork.HomeworkTeacherCreateHomeworkQuestionModel
import com.newletseduvate.model.homeWork.HomeworkTeacherModel
import com.newletseduvate.ui.bottom_sheets.SingleSelectionIconBottomSheetFragment
import com.newletseduvate.ui.homework.HomeworkTeacherFragment.Companion.HomeworkTeacherModelConstant
import com.newletseduvate.utils.constants.CameraUtils
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.constants.RequestCode
import com.newletseduvate.utils.extensions.FileExtension
import com.newletseduvate.utils.extensions.addTextWatchers
import com.newletseduvate.utils.extensions.quality
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.HomeworkTeacherAddHomeworkViewModel
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class HomeworkTeacherAddHomeworkFragment : BaseFragment(R.layout.fragment_homework_teacher_add_homework), View.OnClickListener {

    lateinit var binding: FragmentHomeworkTeacherAddHomeworkBinding
    private val viewModel by lazy { getViewModel<HomeworkTeacherAddHomeworkViewModel>(requireActivity()) }
    lateinit var adapter : RecyclerDynamicAdapter<AdapterHomeworkAddQuestionBinding, HomeworkTeacherCreateHomeworkQuestionModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeworkTeacherAddHomeworkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.clear()

        viewModel.moduleId.set(requireArguments().getInt(Constants.MODULE_ID))
        viewModel.homeworkTeacherModel.value = requireArguments().getSerializable(HomeworkTeacherModelConstant) as HomeworkTeacherModel

        viewModel.subjectId.set(requireArguments().getString("subject_id",""))

        val callbacks = ArrayList<CallBackModel<HomeworkTeacherCreateHomeworkQuestionModel>>()

        callbacks.add(CallBackModel(R.id.iv_delete) { model, position ->
            adapter.removeItemAt(position)
            viewModel.filesList.removeAt(position)
        })
        callbacks.add(CallBackModel(R.id.btn_upload) { model, position ->
            showOptionMenu()
            viewModel.currentUploadFilesList = model.questionAttachments
        })

        adapter = RecyclerDynamicAdapter(R.layout.adapter_homework_add_question, callbacks)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        val fileArrayList = MutableLiveData<ArrayList<String>>(ArrayList())
        viewModel.filesList.add(fileArrayList)
        adapter.add(HomeworkTeacherCreateHomeworkQuestionModel(viewModel.filesList[0], viewModel = viewModel, lifecycleOwner = viewLifecycleOwner))

        initRecyclerView()
        initDataObserver()

        initClickListener()
    }

    private fun initClickListener() {
        binding.btnAddQuestion.setOnClickListener(this)
        binding.btnFinish.setOnClickListener(this)

        binding.tilTitle.addTextWatchers(binding.etTitle)
        binding.tilDescription.addTextWatchers(binding.etDescription)
    }

    private fun initDataObserver() {
        viewModel.questionList.observe(viewLifecycleOwner, {
            if(it.size != 0)
                adapter.replaceList(it)
        })

        viewModel.homeworkAddQuestion.observe(viewLifecycleOwner, {
            if(it){
                viewModel.homeworkAddQuestion.value = false
                findNavController().popBackStack()
            }
        })
    }


    private fun initRecyclerView() {

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvList.layoutManager = linearLayoutManager
        binding.rvList.adapter = adapter
        binding.rvList.setHasFixedSize(true)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeworkTeacherAddHomeworkFragment()
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
                    viewModel.postUploadQuestionFile(file)
                }
            }, {})

        compositeDisposable.add(disposable)
    }

    private var mHighQualityImageUri: Uri? = null

    //    private val dataPartList = ArrayList<UploadFileModel>()
    var compositeDisposable = CompositeDisposable()
//    lateinit var adapterFiles: RecyclerDynamicAdapter<AdapterStudentHomeworkUploadFileBinding, HomeworkUploadQuestionFileModel>


    override fun onClick(v: View?) {
        when(v!!.id){
            binding.btnAddQuestion.id ->{

                val fileArrayList = MutableLiveData<ArrayList<String>>(ArrayList())
                viewModel.filesList.add(fileArrayList)
                adapter.add(HomeworkTeacherCreateHomeworkQuestionModel(questionAttachments = fileArrayList, viewModel = viewModel, lifecycleOwner = viewLifecycleOwner))
            }

            binding.btnFinish.id ->{

                if(fieldsValid()){

                    viewModel.postUploadHomework(
                        binding.etTitle.text.toString(),
                        binding.etDescription.text.toString(),
                        adapter.getItemList()
                    )

                }

            }

        }
    }

    private fun fieldsValid(): Boolean {
        var isValid = true

        if (binding.etTitle.text.toString().isEmpty()) {
            isValid = false
            binding.etTitle.error = "Please enter title"
        }

        if (binding.etDescription.text.toString().isEmpty()) {
            isValid = false
            binding.etDescription.error = "Please enter description"
        }

        for (question in viewModel.questionList.value!!){
            if(question.question.get().toString().isEmpty()){
                binding.root.snackBar("Please enter all fields in each question")
                return  false
            }
            isValid = true
        }

        return isValid
    }

}