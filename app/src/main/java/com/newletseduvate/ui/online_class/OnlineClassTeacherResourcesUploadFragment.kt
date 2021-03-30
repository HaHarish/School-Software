package com.newletseduvate.ui.online_class

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.*
import com.newletseduvate.model.BottomSheetIconItem
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.model.online_class.TeacherViewClassModel
import com.newletseduvate.ui.bottom_sheets.SingleSelectionIconBottomSheetFragment
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.constants.CameraUtils
import com.newletseduvate.utils.constants.RequestCode
import com.newletseduvate.utils.extensions.FileExtension
import com.newletseduvate.utils.extensions.quality
import com.newletseduvate.utils.extensions.snackBar
import com.newletseduvate.viewmodels.OnlineClassTeacherResourcesUploadViewModel
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OnlineClassTeacherResourcesUploadFragment :
    BaseFragment(R.layout.fragment_online_class_teacher_resources_upload),
    View.OnClickListener {

//    var courseId: Int = 0
    private lateinit var binding: FragmentOnlineClassTeacherResourcesUploadBinding
    private val viewModel by lazy { getViewModel<OnlineClassTeacherResourcesUploadViewModel>(requireActivity()) }

    private lateinit var adapter: RecyclerDynamicAdapter<AdapterTeacherResourceUploadFileBinding, String>
    private lateinit var adapterUpload: RecyclerDynamicAdapter<AdapterTeacherResourceUploadFileBinding, String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentOnlineClassTeacherResourcesUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.clear()

        viewModel.onlineClassModel =
            requireArguments().getSerializable(OnlineClassAttendFragment.modelConstant) as TeacherViewClassModel

        val date = requireArguments().getString("date", "")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateFormat2 = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        viewModel.date = dateFormat2.format(dateFormat.parse(date))

        getResourceFiles()

        val callbacks =
            ArrayList<CallBackModel<String>>()

        callbacks.add(CallBackModel(R.id.iv_delete_file) { model, position ->
            viewModel.uploadedFiles.value?.removeAt(position)
            viewModel.uploadedFiles.value = viewModel.uploadedFiles.value

            viewModel.postDeleteFile(model)
        })

        adapter =
            RecyclerDynamicAdapter(
                R.layout.adapter_teacher_resource_upload_file,
                callbacks
            )

        val callbacks2 =
            ArrayList<CallBackModel<String>>()

        callbacks.add(CallBackModel(R.id.iv_delete_file) { model, position ->
            viewModel.uploadFilesList.value?.removeAt(position)
            viewModel.uploadFilesList.value = viewModel.uploadFilesList.value

            viewModel.postDeleteFile(model)
        })

        adapterUpload =
            RecyclerDynamicAdapter(
                R.layout.adapter_teacher_resource_upload_file,
                callbacks2
            )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        setClickListeners()
        initUploadedAdapter()
        initUploadAdapter()

        viewModel.uploadedFiles.observe(viewLifecycleOwner, {
            if(adapter.getItemList().size == 0)
                adapter.replaceList(it)
        })

        viewModel.uploadFilesList.observe(viewLifecycleOwner, {
            if(adapterUpload.getItemList().size == 0)
                adapterUpload.replaceList(it)
        })

        viewModel.homeworkUploadFileResponse.observe(viewLifecycleOwner, {

            if (it)
                displayProgress()
            else
                dismissProgress()

        })

        viewModel.homeworkUploadSubmitResponse.observe(viewLifecycleOwner, {

            if (it){
                viewModel.homeworkUploadSubmitResponse.value = false
                findNavController().popBackStack()
            }

        })
    }

    private fun initUploadedAdapter() {

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvList.layoutManager = linearLayoutManager

        binding.rvList.adapter = adapter
        binding.rvList.setHasFixedSize(true)
    }

    private fun initUploadAdapter() {

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvListUpload.layoutManager = linearLayoutManager

        binding.rvListUpload.adapter = adapterUpload
        binding.rvListUpload.setHasFixedSize(true)
    }

    private fun getResourceFiles() {
        if (NetworkCheck.isInternetAvailable(requireContext())) {
            viewModel.getResourceFiles()
        } else {
            if (::binding.isInitialized) {
                binding.root.snackBar(
                    getString(R.string.check_internet),
                    getString(R.string.retry),
                    true
                ) {
                    getResourceFiles()
                }
            }
        }
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
                    viewModel.homeworkUploadFileResponse.value = true
                    viewModel.postUploadQuestionFile(file)
                }
            }, {})

        compositeDisposable.add(disposable)
    }

    private
    var mHighQualityImageUri: Uri? = null

    //    private val dataPartList = ArrayList<UploadFileModel>()
    var compositeDisposable = CompositeDisposable()
//    lateinit var adapterFiles: RecyclerDynamicAdapter<AdapterStudentHomeworkUploadFileBinding, HomeworkUploadQuestionFileModel>


    private fun setClickListeners() {
        binding.btnSubmit.setOnClickListener(this)
        binding.btnUploadFile.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OnlineClassTeacherResourcesUploadFragment()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            binding.btnSubmit.id -> {
                viewModel.postUploadResourceFile(adapterUpload.getItemList())
            }

            binding.btnUploadFile.id -> {
                showOptionMenu()
            }
        }
    }
}