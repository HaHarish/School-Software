package com.newletseduvate.ui.blogs

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
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.newletseduvate.R
import com.newletseduvate.adapter.dynamic_recycler_adapter.CallBackModel
import com.newletseduvate.adapter.dynamic_recycler_adapter.RecyclerDynamicAdapter
import com.newletseduvate.base.BaseFragment
import com.newletseduvate.databinding.AdapterUploadFileBinding
import com.newletseduvate.databinding.FragmentBlogStudentCreateNewBinding
import com.newletseduvate.model.BottomSheetIconItem
import com.newletseduvate.model.BottomSheetItem
import com.newletseduvate.model.general.UploadFileModel
import com.newletseduvate.ui.bottom_sheets.SingleSelectionBottomSheetFragment
import com.newletseduvate.ui.bottom_sheets.SingleSelectionIconBottomSheetFragment
import com.newletseduvate.utils.NetworkCheck
import com.newletseduvate.utils.constants.CameraUtils
import com.newletseduvate.utils.constants.RequestCode.RC_CAMERA_PERMISSION
import com.newletseduvate.utils.constants.RequestCode.RC_STORAGE_PERMISSION
import com.newletseduvate.utils.constants.RequestCode.REQUEST_CODE_GALLERY
import com.newletseduvate.utils.constants.RequestCode.REQUEST_CODE_IMAGE_CAPTURE
import com.newletseduvate.utils.extensions.*
import com.newletseduvate.utils.rich_text_editor.RichEditor
import com.newletseduvate.viewmodels.BlogStudentCreateNewViewModel
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class BlogStudentCreateNewFragment : BaseFragment(R.layout.fragment_blog_student_create_new),
    SingleSelectionBottomSheetFragment.OnChooseReasonListener,
    View.OnClickListener {

    private lateinit var binding: FragmentBlogStudentCreateNewBinding
    private val viewModel by lazy { getViewModel<BlogStudentCreateNewViewModel>(this) }
    lateinit var adapter: RecyclerDynamicAdapter<AdapterUploadFileBinding, UploadFileModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlogStudentCreateNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()
        initTextForFilters()
        getStudentBlogGenre()
        initRecyclerView()

        RichEditor(
            requireContext(),
            binding.etBlogText.areToolbar,
            binding.etBlogText.arEditText,
            "Write the blog with at least 10 words"
        )

        viewModel.postCreateBlogResponse.observe(viewLifecycleOwner, {
            it.message?.let { it1 -> binding.root.snackBar(it1) }
                ?: findNavController().popBackStack()
        })
    }

    private fun initRecyclerView() {

        val callbacks = ArrayList<CallBackModel<UploadFileModel>>()
        callbacks.add(CallBackModel(R.id.iv_delete_file) { model, position ->
            adapter.removeItemAt(position)
        })

        adapter = RecyclerDynamicAdapter(R.layout.adapter_upload_file, callbacks)
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvList.layoutManager = layoutManager
        binding.rvList.adapter = adapter
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

            })
        bottomSheet.show(
            childFragmentManager,
            bottomSheet.tag
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var uri: Uri?
        var imageFile: String?
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GALLERY -> if (data!!.clipData != null) {
                    var i = 0
                    while (i < data.clipData!!.itemCount) {
                        uri = data.clipData!!.getItemAt(i).uri
                        imageFile = CameraUtils.getPath(requireActivity(), uri)
                        if (FileExtension.isImageFile(imageFile!!)) {
                            createBitmap(imageFile)
                        } else binding.root.snackBar("This type of file is not supported")
                        i++
                    }
                } else {
                    uri = data.data
                    imageFile = CameraUtils.getPath(requireActivity(), uri!!)
                    if (FileExtension.isImageFile(imageFile!!)) {
                        createBitmap(imageFile)
                    } else binding.root.snackBar("This type of file is not supported")
                }
                REQUEST_CODE_IMAGE_CAPTURE -> try {
                    val urifile = File(mHighQualityImageUri!!.path!!)
                    imageFile = urifile.path
                    if (FileExtension.isImageFile(imageFile)) {
                        createBitmap(imageFile)
                    } else binding.root.snackBar("This type of file is not supported")
                } catch (e: java.lang.Exception) {
                    Toast.makeText(activity, "Image is too large. Choose other", Toast.LENGTH_LONG)
                        .show()
                }
            }
            checkFileAdapter(dataPartList)
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
                    RC_CAMERA_PERMISSION
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
            REQUEST_CODE_IMAGE_CAPTURE
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
                    RC_STORAGE_PERMISSION
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
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(
            intent,
            REQUEST_CODE_GALLERY
        )
    }

    private fun getStudentBlogGenre() {
        if (NetworkCheck.isInternetAvailable(requireContext())) {
            viewModel.getStudentBlogGenre()
        } else {
            if (::binding.isInitialized) {
                binding.root.snackBar(
                    getString(R.string.check_internet),
                    getString(R.string.retry),
                    true
                ) {
                    getStudentBlogGenre()
                }
            }
        }
    }

    override fun onChooseSingleItem(BOTTOM_SHEET_ID: Int, selectedItem: BottomSheetItem) {
        when (BOTTOM_SHEET_ID) {

            GenreBottomSheetID -> {
                if (selectedItem.id != null) {
                    binding.etGenre.setText(selectedItem.name)
                    viewModel.genreId.set(selectedItem.id)
                }
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {

            binding.etGenre.id -> {

                if (viewModel.genreList.value != null &&
                    viewModel.genreList.value!!.data != null &&
                    viewModel.genreList.value!!.data!!.size > 0
                ) {
                    viewModel.genreList.value!!.data!!.getBottomSheetList()
                        .showSingleSelectionBottomSheetWithArrayList(
                            childFragmentManager,
                            GenreBottomSheetID,
                            viewModel.genreId.get()
                        )
                } else binding.root.snackBar(resources.getString(R.string.please_wait))

            }

            binding.btnSubmit.id -> {
                if (fieldsValid()) {
                    val contentHtml = binding.etBlogText.arEditText.html
                    viewModel.postCreateBlog(
                        "8",
                        binding.etBlogTitle.text.toString(),
                        contentHtml.replace("\n", ""),
                        wordCount,
                        dataPartList[0]
                    )
                }
            }

            binding.btnDraft.id -> {
                if (fieldsValid()) {

                    val contentHtml = binding.etBlogText.arEditText.html
                    viewModel.postCreateBlog(
                        "2",
                        binding.etBlogTitle.text.toString(),
                        contentHtml.replace("\n", ""),
                        wordCount,
                        dataPartList[0]
                    )

                }
            }

            binding.btnUploadPhoto.id -> {
                showOptionMenu()
            }
        }
    }

    private fun createBitmap(path: String) {
        val disposable = Compressor(activity)
            .setMaxWidth(640)
            .setMaxHeight(480)
            .setQuality(requireContext().quality(path))
            .compressToFileAsFlowable(File(path))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                prepareFilePart(getMimeType(it.path)!!, it.path).let { file ->
                    dataPartList.clear()
                    dataPartList.add(
                        file
                    )
                }
                checkFileAdapter(dataPartList)
            }, {
                it.printStackTrace()
            })
        compositeDisposable.add(disposable)
    }

    private fun checkFileAdapter(dataPartList: ArrayList<UploadFileModel>) {
        adapter.replaceList(dataPartList)
    }


    private fun setClickListeners() {
        binding.etGenre.setOnClickListener(this)
        binding.btnUploadPhoto.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        binding.btnDraft.setOnClickListener(this)

        binding.tilGenre.addTextWatchers(binding.etGenre)
        binding.tilBlogTitle.addTextWatchers(binding.etBlogTitle)
    }

    private val GenreBottomSheetID = 0

    private fun initTextForFilters() {
        viewModel.genreList.value?.let {
            onChooseSingleItem(
                GenreBottomSheetID,
                viewModel.genreList.value!!.data.getBottomSheetItem(viewModel.genreId.get())
            )
        }
    }

    private fun fieldsValid(): Boolean {
        var isValid = true

        if (binding.etGenre.text.toString().isEmpty()) {
            isValid = false
            binding.etGenre.error = resources.getString(R.string.error_blog_title_empty)
        }

        if (binding.etBlogTitle.text.toString().isEmpty()) {
            isValid = false
            binding.etBlogTitle.error = resources.getString(R.string.error_blog_title_empty)
        }

        wordCount = binding.etBlogText.arEditText.text!!.trim().split(" ").size
        if (wordCount < 10) {
            isValid = false
            binding.root.snackBar(resources.getString(R.string.error_create_blog_text))
        }

        if (dataPartList.size == 0) {
            isValid = false
            binding.root.snackBar(resources.getString(R.string.please_upload_thumbnail))
        }

        return isValid
    }

    private var mHighQualityImageUri: Uri? = null
    private val dataPartList = ArrayList<UploadFileModel>()
    var compositeDisposable = CompositeDisposable()
    private var wordCount = 0

    companion object {
        @JvmStatic
        fun newInstance() = BlogStudentCreateNewFragment()
    }
}