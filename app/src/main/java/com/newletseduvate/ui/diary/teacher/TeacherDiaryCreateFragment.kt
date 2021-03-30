package com.newletseduvate.ui.diary.teacher

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.newletseduvate.R
import com.newletseduvate.adapter.DiaryUploadFileAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.FragmentTeacherDiaryGeneralBinding
import com.newletseduvate.model.BottomSheetIconItem
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.ui.activities.MainActivity
import com.newletseduvate.ui.bottom_sheets.SingleSelectionIconBottomSheetFragment
import com.newletseduvate.ui.circular.teacher.CircularTeacherUploadFileInterface
import com.newletseduvate.utils.Status
import com.newletseduvate.utils.constants.CameraUtils
import com.newletseduvate.utils.constants.Constants
import com.newletseduvate.utils.constants.RequestCode
import com.newletseduvate.utils.extensions.FileExtension
import com.newletseduvate.utils.extensions.getBranchName
import com.newletseduvate.utils.extensions.quality
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.TeacherDiaryCreateFilterViewModel
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_teacher_diary_general.*
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class TeacherDiaryCreateFragment: BaseFragment(R.layout.fragment_teacher_diary_general),
    CircularTeacherUploadFileInterface {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: DiaryUploadFileAdapter

    private lateinit var binding: FragmentTeacherDiaryGeneralBinding

    private var imageStr: String? = null

    private val viewModel by lazy { getViewModel<TeacherDiaryCreateFilterViewModel>(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeacherDiaryGeneralBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViews()

        /* Attach Files Code starts here*/
        binding.buttonTeacherDiaryGeneralUploadFiles.setOnClickListener {
            showOptionMenu()
        }

        binding.buttonTeacherDiaryGeneralSubmit.setOnClickListener {
            viewModel.title = binding.editTextTeacherDiaryGeneralTitle.text?.trim().toString()
            viewModel.description = binding.editTextTeacherDiaryGeneralDescription.text?.trim().toString()

            viewModel.recapOfPreviousClass = binding.editTextTeacherDiaryCreateFilterRecapOfPreviousClass.text?.trim().toString()
            viewModel.detailsOfClasswork = binding.editTextTeacherDiaryCreateFilterDetailsOfClassWork.text?.trim().toString()
            viewModel.summary = binding.editTextTeacherDiaryCreateFilterSummary.text?.trim().toString()
            viewModel.toolsUsed = binding.editTextTeacherDiaryCreateFilterToolsUsed.text?.trim().toString()
            viewModel.homework = binding.editTextTeacherDiaryCreateFilterHomeWork.text?.trim().toString()

            if(viewModel.isGeneralClicked.get()){

                if(binding.editTextTeacherDiaryGeneralTitle.text?.trim().toString().isEmpty()){
                    binding.root.snackBar("Please enter title")
                }else if(binding.editTextTeacherDiaryGeneralDescription.text?.trim().toString().isEmpty()){
                    binding.root.snackBar("Please enter description")
                }else{
                    viewModel.teacherDiaryCreate()
                }
            }else if(viewModel.isDailyClicked.get()){
                if(binding.editTextTeacherDiaryCreateFilterRecapOfPreviousClass.text?.trim().toString().isEmpty()){
                    binding.root.snackBar("Please enter Recap of Previous Class")
                }else if(binding.editTextTeacherDiaryCreateFilterDetailsOfClassWork.text?.trim().toString().isEmpty()){
                    binding.root.snackBar("Please enter details of Classwork")
                }else if(binding.editTextTeacherDiaryCreateFilterSummary.text?.trim().toString().isEmpty()){
                    binding.root.snackBar("Please enter Summary")
                }else if(binding.editTextTeacherDiaryCreateFilterToolsUsed.text?.trim().toString().isEmpty()){
                    binding.root.snackBar("Please enter Tools Used")
                }else if(binding.editTextTeacherDiaryCreateFilterHomeWork.text?.trim().toString().isEmpty()){
                    binding.root.snackBar("Please enter Homework")
                }else{
                    viewModel.teacherDiaryCreateDaily()
                }
            }
        }

        linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvTeacherDiaryGeneralListUploadedFiles.layoutManager = linearLayoutManager

        viewModel.listImageUrl.observe(viewLifecycleOwner, {
            adapter = DiaryUploadFileAdapter(viewModel.listImageUrl.value!!, requireContext(),
                this@TeacherDiaryCreateFragment,
                sharedPreferences.getBranchName())
            binding.rvTeacherDiaryGeneralListUploadedFiles.adapter = adapter
            adapter.notifyDataSetChanged()
        })

        viewModel.teacherDiaryFileUploadResponse.observe(viewLifecycleOwner, {
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

        viewModel.teacherDiaryCreateDailyResponse.observe(viewLifecycleOwner, {
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

                                delay(2000)

                                var bundle = Bundle()
                                bundle.putInt(Constants.MODULE_ID, viewModel.moduleId)

                                val intent = Intent(requireContext(), MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra(Constants.MODULE_ID, bundle)
                                startActivity(intent)
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

        /* Create */
        viewModel.teacherDiaryCreateResponse.observe(viewLifecycleOwner, {
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

                                delay(2000)

                                var bundle = Bundle()
                                bundle.putInt(Constants.MODULE_ID, viewModel.moduleId)

                                val intent = Intent(requireContext(), MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra(Constants.MODULE_ID, bundle)
                                startActivity(intent)
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

    private fun setUpViews() {
        if(viewModel.isGeneralClicked.get()){
            binding.layoutGeneralDiary.visibility = View.VISIBLE
            binding.layoutDailyDiary.visibility = View.GONE
        }else if(viewModel.isDailyClicked.get()){
            binding.layoutGeneralDiary.visibility = View.GONE
            binding.layoutDailyDiary.visibility = View.VISIBLE
        }
    }

    override fun onClickedDeleteButton(position: Int) {
        imageStr = viewModel.listImageUrl.value?.get(position)
        viewModel.listImageUrl.value?.remove(imageStr)
        viewModel.listImageUrl.value = viewModel.listImageUrl.value
        adapter.notifyDataSetChanged()
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

                    viewModel.teacherDiaryFileUpload(file)
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

    /* Attach Files code ends here */

}